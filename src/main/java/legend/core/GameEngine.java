package legend.core;

import legend.core.audio.AudioThread;
import legend.core.audio.EffectsOverTimeGranularity;
import legend.core.audio.InterpolationPrecision;
import legend.core.audio.PitchResolution;
import legend.core.audio.SampleRate;
import legend.core.gpu.Gpu;
import legend.core.gte.Gte;
import legend.core.opengl.Mesh;
import legend.core.opengl.Shader;
import legend.core.opengl.ShaderManager;
import legend.core.opengl.ShaderOptions;
import legend.core.opengl.ShaderType;
import legend.core.opengl.SimpleShaderOptions;
import legend.core.opengl.Texture;
import legend.core.opengl.TmdObjLoader;
import legend.core.opengl.Window;
import legend.core.opengl.fonts.Font;
import legend.core.opengl.fonts.FontManager;
import legend.core.opengl.fonts.TextStream;
import legend.core.spu.Spu;
import legend.game.EngineStateEnum;
import legend.game.Main;
import legend.game.Scus94491BpeSegment_8002;
import legend.game.fmv.Fmv;
import legend.game.input.Input;
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
import legend.game.unpacker.FileData;
import legend.game.unpacker.Loader;
import legend.game.unpacker.Unpacker;
import legend.game.unpacker.UnpackerException;
import legend.game.unpacker.UnpackerStoppedRuntimeException;
import legend.game.unpacker.scripts.ScriptPatcher;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Matrix4f;
import org.legendofdragoon.modloader.ModManager;
import org.legendofdragoon.modloader.events.EventManager;
import org.legendofdragoon.modloader.i18n.LangManager;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

import static legend.game.SItem.albertXpTable_801138c0;
import static legend.game.SItem.dartXpTable_801135e4;
import static legend.game.SItem.haschelXpTable_801136d8;
import static legend.game.SItem.kongolXpTable_801134f0;
import static legend.game.SItem.lavitzXpTable_801138c0;
import static legend.game.SItem.loadMenuAssets;
import static legend.game.SItem.meruXpTable_801137cc;
import static legend.game.SItem.mirandaXpTable_80113aa8;
import static legend.game.SItem.roseXpTable_801139b4;
import static legend.game.SItem.shanaXpTable_80113aa8;
import static legend.game.Scus94491BpeSegment.battleUiParts;
import static legend.game.Scus94491BpeSegment.gameLoop;
import static legend.game.Scus94491BpeSegment.startSound;
import static legend.game.Scus94491BpeSegment_8002.initTextboxGeometry;
import static legend.game.Scus94491BpeSegment_8003.GsInitGraph;
import static legend.game.Scus94491BpeSegment_800b.shadowModel_800bda10;
import static org.lwjgl.opengl.GL11C.GL_BLEND;
import static org.lwjgl.opengl.GL11C.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11C.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11C.GL_TRIANGLE_STRIP;
import static org.lwjgl.opengl.GL11C.glBlendFunc;
import static org.lwjgl.opengl.GL11C.glDisable;
import static org.lwjgl.opengl.GL11C.glEnable;
import static org.lwjgl.opengl.GL11C.glViewport;

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

  private static Window.Events.Resize onResize;
  private static Window.Events.Key onKeyPress;
  private static Window.Events.Click onMouseRelease;
  private static Window.Events.OnPressedThisFrame onPressedThisFrame;
  private static Runnable onShutdown;

  private static final ShaderType<EyeShaderOptions> EYE_SHADER = new ShaderType<>(
    options -> RenderEngine.loadShader("simple", "loading", options),
    shader -> {
      shader.bindUniformBlock("transforms", Shader.UniformBuffer.TRANSFORM);
      shader.bindUniformBlock("transforms2", Shader.UniformBuffer.TRANSFORM2);
      final Shader<EyeShaderOptions>.UniformFloat alpha = shader.new UniformFloat("alpha");
      final Shader<EyeShaderOptions>.UniformFloat ticks = shader.new UniformFloat("ticks");
      return () -> new EyeShaderOptions(alpha, ticks);
    }
  );

  private static Shader<SimpleShaderOptions> shader;
  private static SimpleShaderOptions shaderOptions;
  private static Texture title1Texture;
  private static Texture title2Texture;
  private static Mesh fullScrenMesh;

  private static Shader<EyeShaderOptions> eyeShader;
  private static EyeShaderOptions eyeOptions;
  private static Texture eye;
  private static Mesh eyeMesh;

  private static Texture loadingTexture;
  private static Mesh loadingMesh;

  private static Font font;

  private static final Object statusTextLock = new Object();
  private static String newStatusText;
  private static TextStream statusText;
  private static float screenWidth;

  private static Shader.UniformBuffer transforms2;
  private static final FloatBuffer transforms2Buffer = BufferUtils.createFloatBuffer(4 * 4 + 4);
  private static final Matrix4f identity = new Matrix4f();
  private static final Matrix4f textTransforms = new Matrix4f();
  private static final Matrix4f eyeTransforms = new Matrix4f();
  private static final Matrix4f loadingTransforms = new Matrix4f();

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
          Unpacker.setStatusListener(status -> {
            synchronized(statusTextLock) {
              newStatusText = status;
            }
          });

          try {
            Unpacker.unpack();
          } catch(final UnpackerException e) {
            newStatusText = "Failed to unpack files: " + e.getMessage();
            LOGGER.error("Failed to unpack files", e);
            skip();
            return;
          } catch(final UnpackerStoppedRuntimeException e) {
            LOGGER.info("Unpacking stopped");
            return;
          }

          new ScriptPatcher(Path.of("./patches"), Path.of("./files"), Path.of("./files/patches/cache"), Path.of("./files/patches/backups")).apply();

          loadXpTables();

          Scus94491BpeSegment_8002.start();

          synchronized(UPDATER_LOCK) {
            if(!UPDATE_CHECK_FINISHED) {
              newStatusText = "Checking for updates...";
            }
          }

          loading = false;
        }
      } catch(final Exception e) {
        throw new RuntimeException(e);
      }
    });

    time = System.nanoTime();
    thread.start();

    // Find and load all mods so their global config can be shown in the title screen options menu
    MOD_ACCESS.findMods(Path.of("./mods"), Version.VERSION);
    bootMods(MODS.getAllModIds());

    ConfigStorage.loadConfig(CONFIG, ConfigStorageLocation.GLOBAL, Path.of("config.dcnf"));

    AUDIO_THREAD.init();
    AUDIO_THREAD.getSequencer().setVolume(CONFIG.getConfig(CoreMod.MUSIC_VOLUME_CONFIG.get()));
    AUDIO_THREAD.changeInterpolationBitDepth(CONFIG.getConfig(CoreMod.MUSIC_INTERPOLATION_PRECISION_CONFIG.get()));
    AUDIO_THREAD.changePitchResolution(CONFIG.getConfig(CoreMod.MUSIC_PITCH_RESOLUTION_CONFIG.get()));
    AUDIO_THREAD.changeSampleRate(CONFIG.getConfig(CoreMod.MUSIC_SAMPLE_RATE_CONFIG.get()));
    AUDIO_THREAD.changeEffectsOverTimeGranularity(CONFIG.getConfig(CoreMod.MUSIC_EFFECTS_OVER_TIME_GRANULARITY_CONFIG.get()));

    SPU.init();
    RENDERER.init();
    RENDERER.events().onShutdown(Loader::shutdownLoader);
    Input.init();
    GPU.init();

    try {
      RENDERER.run();
    } finally {
      AUDIO_THREAD.destroy();
      RENDERER.delete();
      Input.destroy();
      UPDATER.delete();
    }
  }

  /** Returns missing mod IDs, if any */
  public static Set<String> bootMods(final Set<String> modIds) {
    LOGGER.info("Booting mods...");

    MOD_ACCESS.reset();
    LANG_ACCESS.reset();
    EVENT_ACCESS.reset();
    REGISTRY_ACCESS.reset();

    LOGGER.info("Loading mods %s...", modIds);

    final Set<String> missingMods = MOD_ACCESS.loadMods(modIds);

    // Initialize language
    LANG_ACCESS.initialize(MODS, Main.ORIGINAL_LOCALE);

    // Initialize event bus and find all event handlers
    EVENT_ACCESS.initialize(MODS);

    // Initialize config registry and fire off config registry events
    REGISTRY_ACCESS.initialize(REGISTRIES.config);

    MOD_ACCESS.loadingComplete();

    return missingMods;
  }

  public static void bootRegistries() {
    REGISTRY_ACCESS.initializeRemaining();
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
    identity.get(transforms2Buffer);
    transforms2.set(transforms2Buffer);

    shaderOptions = null;

    if(shader != null) {
      shader = null;
    }

    if(title1Texture != null) {
      title1Texture.delete();
      title1Texture = null;
    }

    if(title2Texture != null) {
      title2Texture.delete();
      title2Texture = null;
    }

    if(fullScrenMesh != null) {
      fullScrenMesh.delete();
      fullScrenMesh = null;
    }

    eyeOptions = null;

    if(eyeShader != null) {
      eyeShader.delete();
      eyeShader = null;
    }

    if(eye != null) {
      eye.delete();
      eye = null;
    }

    if(eyeMesh != null) {
      eyeMesh.delete();
      eyeMesh = null;
    }

    if(loadingTexture != null) {
      loadingTexture.delete();
      loadingTexture = null;
    }

    if(loadingMesh != null) {
      loadingMesh.delete();
      loadingMesh = null;
    }

    if(font != null) {
      font = null;
    }

    if(onResize != null) {
      RENDERER.events().removeOnResize(onResize);
      onResize = null;
    }

    if(onKeyPress != null) {
      RENDERER.events().removeKeyPress(onKeyPress);
      onKeyPress = null;
    }

    if(onMouseRelease != null) {
      RENDERER.events().removeMouseRelease(onMouseRelease);
      onMouseRelease = null;
    }

    if(onPressedThisFrame != null) {
      RENDERER.events().removePressedThisFrame(onPressedThisFrame);
      onPressedThisFrame = null;
    }

    if(onShutdown != null) {
      RENDERER.events().removeShutdown(onShutdown);
      onShutdown = null;
    }

    RENDERER.usePs1Gpu = true;
    openalThread.start();

    synchronized(INIT_LOCK) {
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

    final Path vsh = Paths.get("gfx", "shaders", "simple.vsh");
    shader = ShaderManager.getShader(RenderEngine.SIMPLE_SHADER);
    shaderOptions = shader.makeOptions();

    title1Texture = Texture.filteredPng(Path.of(".", "gfx", "textures", "intro", "title1.png"));
    title2Texture = Texture.filteredPng(Path.of(".", "gfx", "textures", "intro", "title2.png"));
    loadingTexture = Texture.png(Path.of(".", "gfx", "textures", "intro", "loading.png"));
    eye = Texture.png(Path.of(".", "gfx", "textures", "loading.png"));

    eyeShader = ShaderManager.addShader(EYE_SHADER);
    eyeOptions = eyeShader.makeOptions();

    eyeMesh = new Mesh(GL_TRIANGLE_STRIP, new float[] {
       0.0f,  0.0f, 1.0f, 0, 0,
       0.0f, 32.0f, 1.0f, 0, 1,
      32.0f,  0.0f, 1.0f, 1, 0,
      32.0f, 32.0f, 1.0f, 1, 1,
    }, 4);
    eyeMesh.attribute(0, 0L, 3, 5);
    eyeMesh.attribute(1, 3L, 2, 5);

    loadingMesh = new Mesh(GL_TRIANGLE_STRIP, new float[] {
        0.0f,  0.0f, 1.0f, 0, 0,
        0.0f, 32.0f, 1.0f, 0, 1,
      130.0f,  0.0f, 1.0f, 1, 0,
      130.0f, 32.0f, 1.0f, 1, 1,
    }, 4);
    loadingMesh.attribute(0, 0L, 3, 5);
    loadingMesh.attribute(1, 3L, 2, 5);

    transforms2 = ShaderManager.getUniformBuffer("transforms2");
    identity.identity();

    font = FontManager.get("default");

    onResize = RENDERER.events().onResize(GameEngine::windowResize);
    windowResize(RENDERER.window(), (int)(RENDERER.window().getWidth() * RENDERER.window().getScale()), (int)(RENDERER.window().getHeight() * RENDERER.window().getScale()));

    RENDERER.usePs1Gpu = false;
    RENDERER.setRenderCallback(GameEngine::renderIntro);
    RENDERER.window().setWindowIcon(Path.of("gfx/textures/icon.png"));

    onKeyPress = RENDERER.events().onKeyPress((window, key, scancode, mods) -> skip());
    onMouseRelease = RENDERER.events().onMouseRelease((window, x, y, button, mods) -> skip());
    onPressedThisFrame = RENDERER.events().onPressedThisFrame((window, inputAction) -> skip());
    onShutdown = RENDERER.events().onShutdown(Unpacker::stop);
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

  private static void renderIntro() {
    RENDERER.setProjectionMode(ProjectionMode._2D);
    glViewport(0, 0, RENDERER.window().getWidth(), RENDERER.window().getHeight());

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

    shader.use();
    shaderOptions.shiftUv(0.0f, 0.0f);
    shaderOptions.apply();

    identity.get(transforms2Buffer);
    transforms2.set(transforms2Buffer);
    shaderOptions.recolour(fade1 * fade1 * fade1);
    shaderOptions.apply();
    title1Texture.use();
    fullScrenMesh.draw();

    textTransforms.get(transforms2Buffer);
    transforms2.set(transforms2Buffer);
    shaderOptions.recolour(fade2 * fade2 * fade2);
    shaderOptions.apply();
    title2Texture.use();
    fullScrenMesh.draw();

    if(loading) {
      eyeTransforms.get(transforms2Buffer);
      transforms2.set(transforms2Buffer);
      eyeShader.use();
      eyeOptions.alpha(eyeFade);
      eyeOptions.ticks(deltaMs / 10_000.0f);
      eyeOptions.apply();
      eye.use();
      eyeMesh.draw();

      loadingTransforms.get(transforms2Buffer);
      transforms2.set(transforms2Buffer);
      shader.use();
      shaderOptions.recolour(loadingFade);
      shaderOptions.apply();
      loadingTexture.use();
      loadingMesh.draw();
    }

    synchronized(statusTextLock) {
      if(newStatusText != null) {
        if(statusText != null) {
          statusText.delete();
        }

        statusText = font.text(stream -> stream.text(newStatusText));
      }

      newStatusText = null;
    }

    if(statusText != null && loadingFade != 0.0f) {
      statusText.setColour(loadingFade, loadingFade, loadingFade);
      statusText.draw((screenWidth - statusText.width()) / 2, 100.0f);
    }
  }

  private static void windowResize(final Window window, final int width, final int height) {
    if(fullScrenMesh != null) {
      fullScrenMesh.delete();
    }

    final float aspect = 4.0f / 3.0f;

    float w = width;
    float h = w / aspect;

    if(h > height) {
      h = height;
      w = h * aspect;
    }

    screenWidth = w;

    final float l = (width - w) / 2;
    final float t = (height - h) / 2;
    final float r = l + w;
    final float b = t + h;

    fullScrenMesh = new Mesh(GL_TRIANGLE_STRIP, new float[] {
      l, t, 1.0f, 0, 0,
      l, b, 1.0f, 0, 1,
      r, t, 1.0f, 1, 0,
      r, b, 1.0f, 1, 1,
    }, 4);
    fullScrenMesh.attribute(0, 0L, 3, 5);
    fullScrenMesh.attribute(1, 3L, 2, 5);

    eyeTransforms.translation(10.0f, height - 42.0f, 0.0f);
    loadingTransforms.translation(46.0f, height - 42.0f, 0.0f);

    GsInitGraph(width, height);
  }

  private static class EyeShaderOptions implements ShaderOptions<EyeShaderOptions> {
    private final Shader<EyeShaderOptions>.UniformFloat alphaUniform;
    private final Shader<EyeShaderOptions>.UniformFloat ticksUniform;

    private float alpha;
    private float ticks;

    private EyeShaderOptions(final Shader<EyeShaderOptions>.UniformFloat alphaUniform, final Shader<EyeShaderOptions>.UniformFloat ticksUniform) {
      this.alphaUniform = alphaUniform;
      this.ticksUniform = ticksUniform;
    }

    public EyeShaderOptions alpha(final float alpha) {
      this.alpha = alpha;
      return this;
    }

    public EyeShaderOptions ticks(final float ticks) {
      this.ticks = ticks;
      return this;
    }

    @Override
    public void apply() {
      this.alphaUniform.set(this.alpha);
      this.ticksUniform.set(this.ticks);
    }
  }
}
