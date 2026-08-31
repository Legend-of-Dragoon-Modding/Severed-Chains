package legend.core;

import discord.DiscordRichPresence;
import legend.core.audio.AudioThread;
import legend.core.audio.EffectsOverTimeGranularity;
import legend.core.audio.InterpolationPrecision;
import legend.core.audio.PitchResolution;
import legend.core.font.Font;
import legend.core.font.FontManager;
import legend.core.gpu.Bpp;
import legend.core.gpu.Gpu;
import legend.core.gte.Gte;
import legend.core.gte.MV;
import legend.core.lang.I18nText;
import legend.core.opengl.Obj;
import legend.core.opengl.QuadBuilder;
import legend.core.opengl.Texture;
import legend.core.platform.PlatformManager;
import legend.core.platform.SdlPlatformManager;
import legend.core.platform.WindowEvents;
import legend.core.platform.input.InputBindings;
import legend.core.spu.Spu;
import legend.game.Main;
import legend.game.Scus94491BpeSegment;
import legend.game.fmv.Fmv;
import legend.game.fmv.VideoPlayer;
import legend.game.i18n.I18n;
import legend.game.inventory.ItemIcon;
import legend.game.inventory.screens.FontOptions;
import legend.game.inventory.screens.TextColour;
import legend.game.modding.coremod.CoreEngineStateTypes;
import legend.game.modding.coremod.CoreMod;
import legend.game.saves.ConfigCollection;
import legend.game.saves.ConfigStorage;
import legend.game.saves.ConfigStorageLocation;
import legend.game.saves.SaveManager;
import legend.game.saves.SaveVersion;
import legend.game.saves.serializers.V10Serializer;
import legend.game.scripting.ScriptManager;
import legend.game.sound.Sequencer;
import legend.game.textures.Image;
import legend.game.textures.RegisterAtlasTexturesEvent;
import legend.game.textures.TextureAtlas;
import legend.game.textures.TexturePacker;
import legend.game.tmd.TmdObjLoader;
import legend.game.types.Translucency;
import legend.game.ui.GameOverlay;
import legend.game.unpacker.Unpacker;
import legend.game.unpacker.UnpackerException;
import legend.game.unpacker.UnpackerStoppedRuntimeException;
import legend.game.unpacker.scripts.ScriptPatcher;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Vector3f;
import org.legendofdragoon.modloader.ModManager;
import org.legendofdragoon.modloader.events.EventManager;
import org.legendofdragoon.modloader.i18n.LangManager;
import org.legendofdragoon.modloader.registries.RegistryId;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import static legend.game.SItem.UI_WHITE;
import static legend.game.SItem.loadMenuAssets;
import static legend.game.SItem.renderMenuCentredText;
import static legend.game.Scus94491BpeSegment.battleUiParts;
import static legend.game.Scus94491BpeSegment.bindRendererEvents;
import static legend.game.Scus94491BpeSegment_800b.shadowModel_800bda10;
import static legend.game.Text.initTextboxGeometry;
import static legend.game.Text.renderText;
import static legend.game.Text.textZ_800bdf00;
import static legend.game.sound.Audio.startSound;
import static org.lwjgl.opengl.GL11C.GL_BLEND;
import static org.lwjgl.opengl.GL11C.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11C.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11C.glBlendFunc;
import static org.lwjgl.opengl.GL11C.glDisable;
import static org.lwjgl.opengl.GL11C.glEnable;

public final class GameEngine {
  private GameEngine() { }

  private static final Logger LOGGER = LogManager.getFormatterLogger(GameEngine.class);

  public static final boolean IS_WINDOWS = System.getProperty("os.name").startsWith("Windows");

  private static ModManager.Access MOD_ACCESS;
  private static LangManager.Access LANG_ACCESS;
  private static EventManager.Access EVENT_ACCESS;
  private static Registries.Access REGISTRY_ACCESS;
  public static final ModManager MODS = new ModManager(access -> MOD_ACCESS = access, "lod", "lod_core");
  public static final LangManager LANG = new LangManager(access -> LANG_ACCESS = access);
  public static final EventManager EVENTS = new EventManager(access -> EVENT_ACCESS = access, GameEngine::onModError);
  public static final Registries REGISTRIES = new Registries(EVENTS, access -> REGISTRY_ACCESS = access);

  public static final ScriptManager SCRIPTS = new ScriptManager(List.of(Path.of("./patches/libs"), Path.of("./patches/scripts")), Path.of("./patches"));
  public static final Sequencer SEQUENCER = new Sequencer();

  public static final ConfigCollection CONFIG = new ConfigCollection();
  public static final SaveManager SAVES = new SaveManager(SaveVersion.V10, V10Serializer::toV10);

  public static final PlatformManager PLATFORM = new SdlPlatformManager();
  public static final RenderEngine RENDERER = new RenderEngine();

  public static final FontManager FONTS = new FontManager();
  public static Font DEFAULT_FONT = FONTS.get(Path.of("./gfx/fonts/default.json"));

  private static TextureAtlas TEXTURE_ATLAS;
  private static Texture UI_TEXTURE;

  public static final Gte GTE;
  public static final Gpu GPU;
  public static final Spu SPU;
  public static final AudioThread AUDIO_THREAD;

  public static final DiscordRichPresence DISCORD = new DiscordRichPresence();

  public static final Thread hardwareThread;
  public static final Thread openalThread;

  private static final Updater UPDATER = new Updater();

  static {
    try {
      if(!Config.exists()) {
        Config.save();
      } else {
        Config.load();
      }
    } catch(final IOException e) {
      LOGGER.warn("Failed to load config", e);
    }

    GTE = new Gte();
    GPU = new Gpu();
    SPU = new Spu();
    AUDIO_THREAD = new AudioThread(true, 24, InterpolationPrecision.Double, PitchResolution.Quadruple, EffectsOverTimeGranularity.Double);

    hardwareThread = Thread.currentThread();
    hardwareThread.setName("Hardware");
    openalThread = new Thread(AUDIO_THREAD);
    openalThread.setName("OPEN_AL");
  }

  private static final Object INIT_LOCK = new Object();
  private static final Object UPDATER_LOCK = new Object();

  private static Updater.Release UPDATE;
  private static boolean UPDATE_CHECK_FINISHED;

  private static WindowEvents.Resize onResize;
  private static WindowEvents.KeyPressed onKeyPressed;
  private static WindowEvents.ButtonPressed onButtonPressed;
  private static WindowEvents.Click onMouseRelease;
  private static Runnable onShutdown;

  private static boolean cinematicFinished;
  private static Texture eyeTexture;
  private static Obj texturedObj;

  private static final FontOptions fontOptions = new FontOptions().colour(TextColour.WHITE).noShadow().size(0.75f);

  private static String statusText = "";

  private static boolean engineLoading = true;
  private static boolean unpackerLoading = true;

  public static boolean isLoading() {
    return engineLoading || unpackerLoading;
  }

  public static Updater.Release getUpdate() {
    synchronized(UPDATER_LOCK) {
      return UPDATE;
    }
  }

  public static void start() throws IOException {
    UPDATE_CHECK_FINISHED = false;
    UPDATE = null;
    UPDATER.check(release -> {
      synchronized(UPDATER_LOCK) {
        UPDATE_CHECK_FINISHED = true;
        UPDATE = release;
      }
    });

    loadLangOverrides(Main.ORIGINAL_LOCALE);

    final Thread thread = new Thread(() -> {
      try {
        LOGGER.info("Severed Chains %s commit %s built %s starting", Version.FULL_VERSION, Version.HASH, Version.TIMESTAMP);

        RENDERER.setRenderCallback(GameEngine::loadGfx);

        Files.createDirectories(Path.of("saves"));

        synchronized(INIT_LOCK) {
          Unpacker.setStatusListener(status -> statusText = status);

          try {
            Unpacker.unpack();
          } catch(final UnpackerException e) {
            statusText = I18n.translate("unpacker.failed", e.getMessage());
            LOGGER.error("Failed to unpack files", e);
            skip();
            return;
          } catch(final UnpackerStoppedRuntimeException e) {
            LOGGER.info("Unpacking stopped");
            return;
          }

          statusText = I18n.translate("unpacker.patching_scripts");
          try {
            new ScriptPatcher(Path.of("./patches"), Path.of("./files"), Path.of("./files/patches/cache"), Path.of("./files/patches/backups")).apply();
          } catch(final Exception e) {
            statusText = I18n.translate("unpacker.patching_failed");
            throw e;
          }

          statusText = "";

          synchronized(UPDATER_LOCK) {
            if(!UPDATE_CHECK_FINISHED) {
              statusText = I18n.translate("unpacker.checking_for_updates");
            }
          }

          unpackerLoading = false;
        }
      } catch(final Exception e) {
        throw new RuntimeException(e);
      }
    });

    PLATFORM.init();

    thread.start();

    // Find and load all mods so their global config can be shown in the title screen options menu
    MOD_ACCESS.findMods(Path.of("./mods"), Version.VERSION);
    bootMods(MODS.getAllModIds());

    ConfigStorage.loadConfig(CONFIG, ConfigStorageLocation.GLOBAL, Path.of("config.dcnf"));

    DEFAULT_FONT = FONTS.get(Path.of("gfx", "fonts", CONFIG.getConfig(CoreMod.RETAIL_FONT_CONFIG.get())));

    AUDIO_THREAD.init();
    AUDIO_THREAD.setMusicPlayerVolume(CONFIG.getConfig(CoreMod.MUSIC_VOLUME_CONFIG.get()) * CONFIG.getConfig(CoreMod.MASTER_VOLUME_CONFIG.get()));
    AUDIO_THREAD.changeInterpolationBitDepth(CONFIG.getConfig(CoreMod.MUSIC_INTERPOLATION_PRECISION_CONFIG.get()));
    AUDIO_THREAD.changePitchResolution(CONFIG.getConfig(CoreMod.MUSIC_PITCH_RESOLUTION_CONFIG.get()));
    AUDIO_THREAD.changeEffectsOverTimeGranularity(CONFIG.getConfig(CoreMod.MUSIC_EFFECTS_OVER_TIME_GRANULARITY_CONFIG.get()));

    SPU.init();
    RENDERER.init();
    RENDERER.events().onClose(Async::shutdown);
    GPU.init();
    DISCORD.init();
    openalThread.start();

    time = System.nanoTime();

    try {
      RENDERER.run();
    } finally {
      DISCORD.destroy();
      AUDIO_THREAD.destroy();
      RENDERER.delete();
      UPDATER.delete();
      PLATFORM.destroy();
    }
  }

  public static TextureAtlas getTextureAtlas() {
    return TEXTURE_ATLAS;
  }

  public static Texture getUiTexture() {
    return UI_TEXTURE;
  }

  private static void loadLangOverrides(final Locale locale) {
    try {
      LANG_ACCESS.loadLangOverrides(Path.of("lang"), locale);
    } catch(final IOException e) {
      LOGGER.warn("Failed to load lang overrides", e);
    }
  }

  public static void loadLangOverrides(final Path path) {
    try {
      LANG_ACCESS.loadLangOverrides(path, CONFIG.getConfig(CoreMod.LANGUAGE_CONFIG.get()));
    } catch(final IOException e) {
      LOGGER.warn("Failed to load lang overrides", e);
    }
  }

  public static void addLangOverrides(final Map<String, String> lang) {
    LANG_ACCESS.addLangOverrides(lang);
  }

  /** Returns missing mod IDs, if any */
  public static Set<String> bootMods(final Set<String> modIds) {
    LOGGER.info("Booting mods...");

    MOD_ACCESS.reset();
    EVENT_ACCESS.reset();
    REGISTRY_ACCESS.reset();

    LOGGER.info("Loading mods %s...", modIds);

    final Set<String> missingMods = MOD_ACCESS.loadMods(modIds);

    // Initialize event bus and find all event handlers
    EVENT_ACCESS.initialize(MODS);

    // Load mod registries
    EVENTS.postEvent(new AddRegistryEvent(REGISTRIES));

    // Initialize registries needed on the menu and fire off config registry events
    REGISTRY_ACCESS.initialize(REGISTRIES.config);
    REGISTRY_ACCESS.initialize(REGISTRIES.campaignTypes);
    REGISTRY_ACCESS.initialize(REGISTRIES.engineStateTypes);
    REGISTRY_ACCESS.initialize(REGISTRIES.inputActions);
    REGISTRY_ACCESS.initialize(REGISTRIES.characterTemplates);
    REGISTRY_ACCESS.initialize(REGISTRIES.statTypes);
    REGISTRY_ACCESS.initialize(REGISTRIES.statModTypes);

    // We need to boot the goods registry for save cards on the title screen
    REGISTRY_ACCESS.initialize(REGISTRIES.goods);

    MOD_ACCESS.loadingComplete();

    // Load default bindings for input actions
    InputBindings.initBindings();

    loadLang();

    for(final String modId : MODS.getFailedToLoad().keySet()) {
      GameOverlay.addNotification(7, new I18nText("lod_core.ui.mods.error", modId));
    }

    for(final String modId : MODS.getWrongVersions().keySet()) {
      GameOverlay.addNotification(7, new I18nText("lod_core.ui.mods.wrong_version", modId));
    }

    return missingMods;
  }

  private static void onModError(final String modId, final Throwable t) {
    GameOverlay.addNotification(7, new I18nText("lod_core.ui.mods.error", modId));
  }

  public static void loadLang() {
    final Locale locale = CONFIG.getConfig(CoreMod.LANGUAGE_CONFIG.get());
    LANG_ACCESS.reset();
    LANG_ACCESS.initialize(MODS, locale);
    loadLangOverrides(locale);
  }

  public static void bootRegistries() {
    REGISTRY_ACCESS.initializeRemaining();
    ItemIcon.loadIconMap();

    LOGGER.info("Creating texture atlas...");
    final long t = System.nanoTime();

    if(TEXTURE_ATLAS != null) {
      TEXTURE_ATLAS.delete();
    }

    final Map<RegistryId, Image> images = new HashMap<>();
    EVENTS.postEvent(new RegisterAtlasTexturesEvent(images));

    final TexturePacker packer = new TexturePacker("Mod atlas");
    images.forEach(packer::add);

    TEXTURE_ATLAS = packer.pack(512, 512);
    TEXTURE_ATLAS.setPersistent(true);

    LOGGER.info("Texture atlas created in %.02fs", (System.nanoTime() - t) / 1_000_000_000.0f);
  }

  private static void transitionToGame() {
    glDisable(GL_BLEND);

    if(eyeTexture != null) {
      eyeTexture.delete();
      eyeTexture = null;
    }

    if(texturedObj != null) {
      texturedObj.delete();
      texturedObj = null;
    }

    if(onResize != null) {
      RENDERER.events().removeOnResize(onResize);
      onResize = null;
    }

    if(onKeyPressed != null) {
      RENDERER.events().removeKeyPress(onKeyPressed);
      onKeyPressed = null;
    }

    if(onMouseRelease != null) {
      RENDERER.events().removeMouseRelease(onMouseRelease);
      onMouseRelease = null;
    }

    if(onButtonPressed != null) {
      RENDERER.events().removeButtonPress(onButtonPressed);
      onButtonPressed = null;
    }

    if(onShutdown != null) {
      RENDERER.events().removeClose(onShutdown);
      onShutdown = null;
    }

    synchronized(INIT_LOCK) {
      Scus94491BpeSegment.main();

      TmdObjLoader.fromModel("Shadow", shadowModel_800bda10);
      for(int i = 0; i < shadowModel_800bda10.modelParts_00.length; i++) {
        shadowModel_800bda10.modelParts_00[i].tmd_08.getObj().persistent = true;
      }

      loadMenuAssets();
      initTextboxGeometry();
      battleUiParts.init();
      startSound();
      bindRendererEvents();
      Fmv.playCurrentFmv(0, CoreEngineStateTypes.TITLE.get());
    }
  }

  private static void loadGfx() {
    glEnable(GL_BLEND);
    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

    UI_TEXTURE = Texture.png("UI", Path.of("gfx", "ui", "ui.png"));
    UI_TEXTURE.persistent = true;

    eyeTexture = Texture.png("Loading eye", Path.of("gfx", "textures", "loading.png"));

    texturedObj = new QuadBuilder("Textured Obj")
      .bpp(Bpp.BITS_24)
      .size(1.0f, 1.0f)
      .uvSize(1.0f, 1.0f)
      .build();

    RENDERER.window().setWindowIcon(Path.of("gfx/textures/icon.png"));

    try {
      VideoPlayer.play(Path.of("gfx/intro.mp4"), GameEngine::renderIntro, () -> {
        cinematicFinished = true;
        RENDERER.setRenderCallback(GameEngine::renderIntro);
      });
    } catch(final IOException e) {
      LOGGER.warn("Failed to play intro", e);
      RENDERER.setRenderCallback(GameEngine::renderIntro);
    }

    onKeyPressed = RENDERER.events().onKeyPress((window, key, scancode, mods, repeat) -> {
      if(mods.isEmpty()) {
        skip();
      }
    });

    onButtonPressed = RENDERER.events().onButtonPress((window, button, repeat) -> skip());

    onMouseRelease = RENDERER.events().onMouseRelease((window, x, y, button, mods) -> skip());
    onShutdown = RENDERER.events().onClose(Unpacker::stop);

    engineLoading = false;
  }

  private static void skip() {
    if(time == 0) {
      synchronized(UPDATER_LOCK) {
        UPDATE_CHECK_FINISHED = true;
      }
    }

    time = 0;
    loadingFade = 1.0f;
    eyeFade = 1.0f;
  }

  private static long time;
  private static float loadingFade;
  private static float eyeFade;
  private static float eyeColour;

  private static void renderIntro() {
    final long deltaMs = (System.nanoTime() - time) / 1_000_000;

    if(deltaMs < 5000) {
      eyeFade += 0.005f;
      if(eyeFade > 1.0f) {
        eyeFade = 1.0f;
      }
    }

    if(cinematicFinished) {
      if(unpackerLoading) {
        loadingFade += 0.02f;
        if(loadingFade > 1.0f) {
          loadingFade = 1.0f;
        }
      } else {
        synchronized(UPDATER_LOCK) {
          if(UPDATE_CHECK_FINISHED) {
            transitionToGame();
            return;
          }
        }
      }
    }

    final int oldTextZ = textZ_800bdf00;
    textZ_800bdf00 = 5;

    if(unpackerLoading) {
      // Offset sine wave delta to quickly shift between colours and then wait for a moment before repeating
      eyeColour += Math.max(0.0f, MathHelper.sin(deltaMs / 300.0f % MathHelper.TWO_PI) * 0.75f + 0.25f) / 500.0f;

      final Vector3f colour = new Vector3f();
      MathHelper.hsvToRgb(eyeColour, 1.0f, 1.0f, colour);

      final MV transforms = new MV();
      transforms.scaling(16.0f, 16.0f, 1.0f);
      transforms.transfer.set(4.0f, 220.0f, 29.0f);
      RENDERER.queueOrthoModel(texturedObj, transforms, QueuedModelStandard.class)
        .translucency(Translucency.HALF_B_PLUS_HALF_F)
        .texture(eyeTexture)
        .useTextureAlpha()
        .alpha(eyeFade)
        .colour(colour)
      ;

      renderText(I18n.translate("unpacker.loading"), 24.0f, 223.0f, UI_WHITE, (model, shadow) -> model.alpha(loadingFade).translucency(Translucency.HALF_B_PLUS_HALF_F));
    }

    if(!statusText.isBlank() && loadingFade != 0.0f) {
      renderMenuCentredText(DEFAULT_FONT, statusText, 160, 30, 300, fontOptions, (model, shadow) -> model.alpha(loadingFade).translucency(Translucency.HALF_B_PLUS_HALF_F));
    }

    textZ_800bdf00 = oldTextZ;
  }
}
