package legend.core;

import legend.core.audio.AudioThread;
import legend.core.audio.EffectsOverTimeGranularity;
import legend.core.audio.InterpolationPrecision;
import legend.core.audio.PitchResolution;
import legend.core.audio.SampleRate;
import legend.core.gpu.Bpp;
import legend.core.gpu.Gpu;
import legend.core.gte.Gte;
import legend.core.gte.MV;
import legend.core.opengl.Obj;
import legend.core.opengl.QuadBuilder;
import legend.core.opengl.Texture;
import legend.core.opengl.TmdObjLoader;
import legend.core.platform.PlatformManager;
import legend.core.platform.SdlPlatformManager;
import legend.core.platform.WindowEvents;
import legend.core.platform.input.InputBindings;
import legend.core.spu.Spu;
import legend.game.EngineStateEnum;
import legend.game.Main;
import legend.game.Scus94491BpeSegment_8002;
import legend.game.fmv.Fmv;
import legend.game.i18n.I18n;
import legend.game.inventory.ItemIcon;
import legend.game.inventory.screens.FontOptions;
import legend.game.inventory.screens.TextColour;
import legend.game.modding.coremod.CoreMod;
import legend.game.saves.ConfigCollection;
import legend.game.saves.ConfigStorage;
import legend.game.saves.ConfigStorageLocation;
import legend.game.saves.SaveManager;
import legend.game.saves.serializers.RetailSerializer;
import legend.game.saves.serializers.V1Serializer;
import legend.game.saves.serializers.V2Serializer;
import legend.game.saves.serializers.V3Serializer;
import legend.game.saves.serializers.V4Serializer;
import legend.game.scripting.ScriptManager;
import legend.game.sound.Sequencer;
import legend.game.types.Translucency;
import legend.game.unpacker.FileData;
import legend.game.unpacker.Loader;
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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

import static legend.game.SItem.UI_WHITE;
import static legend.game.SItem.albertXpTable_801138c0;
import static legend.game.SItem.dartXpTable_801135e4;
import static legend.game.SItem.haschelXpTable_801136d8;
import static legend.game.SItem.kongolXpTable_801134f0;
import static legend.game.SItem.lavitzXpTable_801138c0;
import static legend.game.SItem.loadMenuAssets;
import static legend.game.SItem.meruXpTable_801137cc;
import static legend.game.SItem.mirandaXpTable_80113aa8;
import static legend.game.SItem.renderMenuCentredText;
import static legend.game.SItem.roseXpTable_801139b4;
import static legend.game.SItem.shanaXpTable_80113aa8;
import static legend.game.Scus94491BpeSegment.battleUiParts;
import static legend.game.Scus94491BpeSegment.gameLoop;
import static legend.game.Scus94491BpeSegment.startSound;
import static legend.game.Scus94491BpeSegment_8002.initTextboxGeometry;
import static legend.game.Scus94491BpeSegment_8002.renderText;
import static legend.game.Scus94491BpeSegment_800b.shadowModel_800bda10;
import static legend.game.Scus94491BpeSegment_800b.textZ_800bdf00;
import static org.lwjgl.opengl.GL11C.GL_BLEND;
import static org.lwjgl.opengl.GL11C.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11C.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11C.glBlendFunc;
import static org.lwjgl.opengl.GL11C.glDisable;
import static org.lwjgl.opengl.GL11C.glEnable;

public final class GameEngine {
  private GameEngine() { }

  private static final Logger LOGGER = LogManager.getFormatterLogger(GameEngine.class);

  private static ModManager.Access MOD_ACCESS;
  private static LangManager.Access LANG_ACCESS;
  private static EventManager.Access EVENT_ACCESS;
  private static Registries.Access REGISTRY_ACCESS;
  public static final ModManager MODS = new ModManager(access -> MOD_ACCESS = access, "lod", "lod_core");
  public static final LangManager LANG = new LangManager(access -> LANG_ACCESS = access);
  public static final EventManager EVENTS = new EventManager(access -> EVENT_ACCESS = access);
  public static final Registries REGISTRIES = new Registries(EVENTS, access -> REGISTRY_ACCESS = access);

  public static final ScriptManager SCRIPTS = new ScriptManager(Path.of("./patches"));
  public static final Sequencer SEQUENCER = new Sequencer();

  public static final ConfigCollection CONFIG = new ConfigCollection();
  public static final SaveManager SAVES = new SaveManager(V4Serializer.MAGIC_V4, V4Serializer::toV4);

  public static final PlatformManager PLATFORM = new SdlPlatformManager();
  public static final RenderEngine RENDERER = new RenderEngine();

  public static final Gte GTE;
  public static final Gpu GPU;
  public static final Spu SPU;
  public static final AudioThread AUDIO_THREAD;

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
    AUDIO_THREAD = new AudioThread(true, 24, InterpolationPrecision.Double, PitchResolution.Quadruple, SampleRate._48000, EffectsOverTimeGranularity.Finer);

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

  private static Texture title1Texture;
  private static Texture title2Texture;
  private static Texture eyeTexture;
  private static Obj texturedObj;

  private static final FontOptions fontOptions = new FontOptions().colour(TextColour.WHITE).noShadow().size(0.75f);

  private static String statusText = "";

  private static boolean loading;

  public static boolean isLoading() {
    return loading;
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

    loadUnpackerLang();

    final Thread thread = new Thread(() -> {
      try {
        LOGGER.info("Severed Chains %s commit %s built %s starting", Version.FULL_VERSION, Version.HASH, Version.TIMESTAMP);

        loading = true;
        RENDERER.setRenderCallback(GameEngine::loadGfx);

        Files.createDirectories(Path.of("saves"));
        SAVES.registerDeserializer(RetailSerializer::fromRetailMatcher, RetailSerializer::fromRetail);
        SAVES.registerDeserializer(V1Serializer::fromV1Matcher, V1Serializer::fromV1);
        SAVES.registerDeserializer(V2Serializer::fromV2Matcher, V2Serializer::fromV2);
        SAVES.registerDeserializer(V3Serializer::fromV3Matcher, V3Serializer::fromV3);
        SAVES.registerDeserializer(V4Serializer::fromV4Matcher, V4Serializer::fromV4);

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
          new ScriptPatcher(Path.of("./patches"), Path.of("./files"), Path.of("./files/patches/cache"), Path.of("./files/patches/backups")).apply();

          loadXpTables();

          synchronized(UPDATER_LOCK) {
            if(!UPDATE_CHECK_FINISHED) {
              statusText = I18n.translate("unpacker.checking_for_updates");
            }
          }

          loading = false;
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

    AUDIO_THREAD.init();
    AUDIO_THREAD.setMusicPlayerVolume(CONFIG.getConfig(CoreMod.MUSIC_VOLUME_CONFIG.get()) * CONFIG.getConfig(CoreMod.MASTER_VOLUME_CONFIG.get()));
    AUDIO_THREAD.changeInterpolationBitDepth(CONFIG.getConfig(CoreMod.MUSIC_INTERPOLATION_PRECISION_CONFIG.get()));
    AUDIO_THREAD.changePitchResolution(CONFIG.getConfig(CoreMod.MUSIC_PITCH_RESOLUTION_CONFIG.get()));
    AUDIO_THREAD.changeSampleRate(CONFIG.getConfig(CoreMod.MUSIC_SAMPLE_RATE_CONFIG.get()));
    AUDIO_THREAD.changeEffectsOverTimeGranularity(CONFIG.getConfig(CoreMod.MUSIC_EFFECTS_OVER_TIME_GRANULARITY_CONFIG.get()));

    SPU.init();
    RENDERER.init();
    RENDERER.events().onClose(Loader::shutdownLoader);
    GPU.init();

    try {
      time = System.nanoTime();
      RENDERER.run();
    } finally {
      AUDIO_THREAD.destroy();
      RENDERER.delete();
      UPDATER.delete();
      PLATFORM.destroy();
    }
  }

  private static void loadUnpackerLang() {
    try {
      LANG_ACCESS.loadLang(LANG_ACCESS.getLangPath(Path.of("lang", "unpacker"), Main.ORIGINAL_LOCALE));
    } catch(final IOException e) {
      LOGGER.warn("Failed to load unpacker lang", e);
    }
  }

  /** Returns missing mod IDs, if any */
  public static Set<String> bootMods(final Set<String> modIds) {
    LOGGER.info("Booting mods...");

    MOD_ACCESS.reset();
    LANG_ACCESS.reset();
    EVENT_ACCESS.reset();
    REGISTRY_ACCESS.reset();
    loadUnpackerLang();

    LOGGER.info("Loading mods %s...", modIds);

    final Set<String> missingMods = MOD_ACCESS.loadMods(modIds);

    // Initialize language
    LANG_ACCESS.initialize(MODS, Main.ORIGINAL_LOCALE);

    // Initialize event bus and find all event handlers
    EVENT_ACCESS.initialize(MODS);

    // Initialize config and input registries
    REGISTRY_ACCESS.initialize(REGISTRIES.config);
    REGISTRY_ACCESS.initialize(REGISTRIES.inputActions);

    MOD_ACCESS.loadingComplete();

    // Load default bindings for input actions
    InputBindings.loadBindings();

    return missingMods;
  }

  public static void bootRegistries() {
    REGISTRY_ACCESS.initializeRemaining();
    InputBindings.loadBindings();
    ItemIcon.loadIconMap();
  }

  private static void loadXpTables() throws IOException {
    final FileData dart = new FileData(Files.readAllBytes(Paths.get("./files/characters/dart/xp")));
    final FileData lavitz = new FileData(Files.readAllBytes(Paths.get("./files/characters/lavitz/xp")));
    final FileData albert = new FileData(Files.readAllBytes(Paths.get("./files/characters/albert/xp")));
    final FileData shana = new FileData(Files.readAllBytes(Paths.get("./files/characters/shana/xp")));
    final FileData miranda = new FileData(Files.readAllBytes(Paths.get("./files/characters/miranda/xp")));
    final FileData rose = new FileData(Files.readAllBytes(Paths.get("./files/characters/rose/xp")));
    final FileData haschel = new FileData(Files.readAllBytes(Paths.get("./files/characters/haschel/xp")));
    final FileData kongol = new FileData(Files.readAllBytes(Paths.get("./files/characters/kongol/xp")));
    final FileData meru = new FileData(Files.readAllBytes(Paths.get("./files/characters/meru/xp")));

    for(int i = 0; i < dartXpTable_801135e4.length; i++) {
      dartXpTable_801135e4[i] = dart.readInt(i * 4);
    }

    for(int i = 0; i < lavitzXpTable_801138c0.length; i++) {
      lavitzXpTable_801138c0[i] = lavitz.readInt(i * 4);
    }

    for(int i = 0; i < albertXpTable_801138c0.length; i++) {
      albertXpTable_801138c0[i] = albert.readInt(i * 4);
    }

    for(int i = 0; i < shanaXpTable_80113aa8.length; i++) {
      shanaXpTable_80113aa8[i] = shana.readInt(i * 4);
    }

    for(int i = 0; i < mirandaXpTable_80113aa8.length; i++) {
      mirandaXpTable_80113aa8[i] = miranda.readInt(i * 4);
    }

    for(int i = 0; i < roseXpTable_801139b4.length; i++) {
      roseXpTable_801139b4[i] = rose.readInt(i * 4);
    }

    for(int i = 0; i < haschelXpTable_801136d8.length; i++) {
      haschelXpTable_801136d8[i] = haschel.readInt(i * 4);
    }

    for(int i = 0; i < kongolXpTable_801134f0.length; i++) {
      kongolXpTable_801134f0[i] = kongol.readInt(i * 4);
    }

    for(int i = 0; i < meruXpTable_801137cc.length; i++) {
      meruXpTable_801137cc[i] = meru.readInt(i * 4);
    }
  }

  private static void transitionToGame() {
    glDisable(GL_BLEND);

    if(title1Texture != null) {
      title1Texture.delete();
      title1Texture = null;
    }

    if(title2Texture != null) {
      title2Texture.delete();
      title2Texture = null;
    }

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

    openalThread.start();

    synchronized(INIT_LOCK) {
      Scus94491BpeSegment_8002.start();

      TmdObjLoader.fromModel("Shadow", shadowModel_800bda10);
      for(int i = 0; i < shadowModel_800bda10.modelParts_00.length; i++) {
        shadowModel_800bda10.modelParts_00[i].obj.persistent = true;
      }

      loadMenuAssets();
      initTextboxGeometry();
      battleUiParts.init();
      startSound();
      gameLoop();
      Fmv.playCurrentFmv(0, EngineStateEnum.TITLE_02);
    }
  }

  private static void loadGfx() {
    glEnable(GL_BLEND);
    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

    title1Texture = Texture.filteredPng(Path.of(".", "gfx", "textures", "intro", "title1.png"));
    title2Texture = Texture.filteredPng(Path.of(".", "gfx", "textures", "intro", "title2.png"));
    eyeTexture = Texture.png(Path.of(".", "gfx", "textures", "loading.png"));

    texturedObj = new QuadBuilder("Textured Obj")
      .bpp(Bpp.BITS_24)
      .size(1.0f, 1.0f)
      .uvSize(1.0f, 1.0f)
      .build();

    RENDERER.setRenderCallback(GameEngine::renderIntro);
    RENDERER.window().setWindowIcon(Path.of("gfx/textures/icon.png"));

    onKeyPressed = RENDERER.events().onKeyPress((window, key, scancode, mods, repeat) -> {
      if(mods.isEmpty()) {
        skip();
      }
    });

    onButtonPressed = RENDERER.events().onButtonPress((window, button, repeat) -> skip());

    onMouseRelease = RENDERER.events().onMouseRelease((window, x, y, button, mods) -> skip());
    onShutdown = RENDERER.events().onClose(Unpacker::stop);
  }

  private static void skip() {
    if(time == 0) {
      synchronized(UPDATER_LOCK) {
        UPDATE_CHECK_FINISHED = true;
      }
    }

    time = 0;
    fade1 = 0.0f;
    fade2 = 0.0f;
    loadingFade = 1.0f;
    eyeFade = 1.0f;
  }

  private static long time;
  private static float fade1;
  private static float fade2;
  private static float loadingFade;
  private static float eyeFade;
  private static float eyeColour;

  private static void renderIntro() {
    final long deltaMs = (System.nanoTime() - time) / 1_000_000;

    if(deltaMs < 5000) {
      fade1 += 0.005f;
      if(fade1 > 1.0f) {
        fade1 = 1.0f;
      }
      fade2 = fade1;
      eyeFade = fade1;
    } else if(deltaMs < 13000) {
      fade1 -= 0.0015f;
      if(fade1 < 0) {
        fade1 = 0;
      }
    } else if(deltaMs < 17000) {
      fade1 -= 0.01f;
      if(fade1 < 0) {
        fade1 = 0;
      }

      fade2 -= 0.02f;
      if(fade2 < 0) {
        fade2 = 0;
      }

      loadingFade += 0.02f;
      if(loadingFade > 1.0f) {
        loadingFade = 1.0f;
      }
    } else if(!loading) {
      synchronized(UPDATER_LOCK) {
        if(UPDATE_CHECK_FINISHED) {
          transitionToGame();
          return;
        }
      }
    }

    final int oldTextZ = textZ_800bdf00;
    textZ_800bdf00 = 5;

    final MV transforms = new MV();
    transforms.scaling(320.0f, 240.0f, 1.0f);
    transforms.transfer.z = 30.0f;
    RENDERER.queueOrthoModel(texturedObj, transforms, QueuedModelStandard.class)
      .monochrome(fade1 * fade1 * fade1)
      .texture(title1Texture)
    ;

    transforms.scaling(320.0f, 240.0f, 1.0f);
    transforms.transfer.z = 29.0f;
    RENDERER.queueOrthoModel(texturedObj, transforms, QueuedModelStandard.class)
      .translucency(Translucency.HALF_B_PLUS_HALF_F)
      .monochrome(fade2 * fade2 * fade2)
      .texture(title2Texture)
      .useTextureAlpha()
    ;

    if(loading) {
      // Offset sine wave delta to quickly shift between colours and then wait for a moment before repeating
      eyeColour += Math.max(0.0f, MathHelper.sin(deltaMs / 300.0f % MathHelper.TWO_PI) * 0.75f + 0.25f) / 500.0f;

      final Vector3f colour = new Vector3f();
      MathHelper.hsvToRgb(eyeColour, 1.0f, 1.0f, colour);

      transforms.scaling(16.0f, 16.0f, 1.0f);
      transforms.transfer.set(4.0f, 220.0f, 29.0f);
      RENDERER.queueOrthoModel(texturedObj, transforms, QueuedModelStandard.class)
        .translucency(Translucency.HALF_B_PLUS_HALF_F)
        .texture(eyeTexture)
        .useTextureAlpha()
        .alpha(eyeFade)
        .colour(colour)
      ;

      renderText(I18n.translate("unpacker.loading"), 24.0f, 223.0f, UI_WHITE, model -> model.alpha(loadingFade).translucency(Translucency.HALF_B_PLUS_HALF_F));
    }

    if(!statusText.isBlank() && loadingFade != 0.0f) {
      renderMenuCentredText(statusText, 160, 30, 300, fontOptions, model -> model.alpha(loadingFade).translucency(Translucency.HALF_B_PLUS_HALF_F));
    }

    textZ_800bdf00 = oldTextZ;
  }
}
