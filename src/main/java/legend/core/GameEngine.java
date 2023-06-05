package legend.core;

import legend.core.gpu.Gpu;
import legend.core.memory.Memory;
import legend.core.memory.Value;
import legend.core.memory.segments.RamSegment;
import legend.core.opengl.Mesh;
import legend.core.opengl.Shader;
import legend.core.opengl.ShaderManager;
import legend.core.opengl.Texture;
import legend.core.opengl.Window;
import legend.core.opengl.fonts.Font;
import legend.core.opengl.fonts.FontManager;
import legend.core.opengl.fonts.TextStream;
import legend.core.spu.Spu;
import legend.game.Scus94491BpeSegment;
import legend.game.Scus94491BpeSegment_8002;
import legend.game.Scus94491BpeSegment_8003;
import legend.game.Scus94491BpeSegment_8004;
import legend.game.Scus94491BpeSegment_800e;
import legend.game.fmv.Fmv;
import legend.game.i18n.LangManager;
import legend.game.modding.ModManager;
import legend.game.modding.events.EventManager;
import legend.game.modding.registries.Registries;
import legend.game.saves.ConfigCollection;
import legend.game.saves.ConfigStorage;
import legend.game.saves.ConfigStorageLocation;
import legend.game.saves.SaveManager;
import legend.game.saves.SaveSerialization;
import legend.game.scripting.ScriptManager;
import legend.game.sound.Sequencer;
import legend.game.unpacker.FileData;
import legend.game.unpacker.Unpacker;
import legend.game.unpacker.UnpackerException;
import legend.game.unpacker.UnpackerStoppedRuntimeException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Matrix4f;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.Set;

import static legend.game.SItem.albertXpTable_801138c0;
import static legend.game.SItem.dartXpTable_801135e4;
import static legend.game.SItem.haschelXpTable_801136d8;
import static legend.game.SItem.kongolXpTable_801134f0;
import static legend.game.SItem.lavitzXpTable_801138c0;
import static legend.game.SItem.meruXpTable_801137cc;
import static legend.game.SItem.mirandaXpTable_80113aa8;
import static legend.game.SItem.roseXpTable_801139b4;
import static legend.game.SItem.shanaXpTable_80113aa8;
import static legend.game.Scus94491BpeSegment.gameLoop;
import static org.lwjgl.opengl.GL11C.GL_BLEND;
import static org.lwjgl.opengl.GL11C.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11C.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11C.GL_TRIANGLE_STRIP;
import static org.lwjgl.opengl.GL11C.glBlendFunc;
import static org.lwjgl.opengl.GL11C.glDisable;
import static org.lwjgl.opengl.GL11C.glEnable;

public final class GameEngine {
  private GameEngine() { }

  private static final Logger LOGGER = LogManager.getFormatterLogger(GameEngine.class);

  public static final Memory MEMORY = new Memory();

  private static ModManager.Access MOD_ACCESS;
  private static LangManager.Access LANG_ACCESS;
  private static EventManager.Access EVENT_ACCESS;
  private static Registries.Access REGISTRY_ACCESS;
  public static final ModManager MODS = new ModManager(access -> MOD_ACCESS = access);
  public static final LangManager LANG = new LangManager(access -> LANG_ACCESS = access);
  public static final EventManager EVENTS = new EventManager(access -> EVENT_ACCESS = access);
  public static final Registries REGISTRIES = new Registries(access -> REGISTRY_ACCESS = access);

  public static final ScriptManager SCRIPTS = new ScriptManager();
  public static final Sequencer SEQUENCER = new Sequencer();

  public static final ConfigCollection CONFIG = new ConfigCollection();
  public static final SaveManager SAVES = new SaveManager(SaveSerialization.MAGIC_V2, SaveSerialization::toV2);

  public static final Cpu CPU;
  public static final Gpu GPU;
  public static final Spu SPU;

  public static final Thread hardwareThread;
  public static final Thread spuThread;

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

    // --- User memory ------------------------

    MEMORY.addSegment(new RamSegment(0x0001_0000L, 0x8f_0000));
    MEMORY.addSegment(new RamSegment(0x1f80_0000L, 0x400));

    CPU = new Cpu();
    GPU = new Gpu();
    SPU = new Spu();

    hardwareThread = Thread.currentThread();
    hardwareThread.setName("Hardware");
    spuThread = new Thread(SPU);
    spuThread.setName("SPU");
  }

  private static final Value _80010000 = MEMORY.ref(4, 0x80010000L);
  private static final Value bpe_80188a88 = MEMORY.ref(4, 0x80188a88L);

  private static final Object LOCK = new Object();

  private static Window.Events.Resize onResize;
  private static Window.Events.Key onKeyPress;
  private static Window.Events.Click onMouseRelease;
  private static Runnable onShutdown;

  private static Shader shader;
  private static Shader.UniformFloat shaderAlpha;
  private static Texture title1Texture;
  private static Texture title2Texture;
  private static Mesh fullScrenMesh;

  private static Shader eyeShader;
  private static Shader.UniformFloat eyeShaderAlpha;
  private static Shader.UniformFloat eyeShaderTicks;
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
  private static final Matrix4f identity = new Matrix4f();
  private static final Matrix4f eyeTransforms = new Matrix4f();
  private static final Matrix4f loadingTransforms = new Matrix4f();

  private static boolean loading;

  public static void start() throws IOException {
    final Thread thread = new Thread(() -> {
      try {
        LOGGER.info("--- Legend start ---");

        loading = true;
        GPU.mainRenderer = GameEngine::loadGfx;

        Files.createDirectories(Path.of("saves"));
        SAVES.registerDeserializer(SaveSerialization::fromRetailMatcher, SaveSerialization::fromRetail);
        SAVES.registerDeserializer(SaveSerialization::fromV1Matcher, SaveSerialization::fromV1);
        SAVES.registerDeserializer(SaveSerialization::fromV2Matcher, SaveSerialization::fromV2);

        synchronized(LOCK) {
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

          MEMORY.setBytes(_80010000.getAddress(), Unpacker.loadFile("lod_engine").getBytes());

          MEMORY.addFunctions(Scus94491BpeSegment.class);
          MEMORY.addFunctions(Scus94491BpeSegment_8002.class);
          MEMORY.addFunctions(Scus94491BpeSegment_8003.class);
          MEMORY.addFunctions(Scus94491BpeSegment_8004.class);
          MEMORY.addFunctions(Scus94491BpeSegment_800e.class);

          loadXpTables();

          // Find and load all mods so their global config can be shown in the title screen options menu
          MOD_ACCESS.findMods();
          bootMods(MODS.getAllModIds());

          ConfigStorage.loadConfig(CONFIG, ConfigStorageLocation.GLOBAL, Path.of("config.dcnf"));

          Scus94491BpeSegment_8002.start();
          loading = false;
        }
      } catch(final Exception e) {
        throw new RuntimeException(e);
      }
    });

    time = System.nanoTime();
    thread.start();
    GPU.run();
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
    LANG_ACCESS.initialize(Locale.getDefault());

    // Initialize event bus and find all event handlers
    EVENT_ACCESS.initialize();

    // Initialize config registry and fire off config registry events
    REGISTRY_ACCESS.initialize(REGISTRIES.config);

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
    transforms2.set(identity);

    shaderAlpha = null;

    if(shader != null) {
      shader.delete();
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

    eyeShaderAlpha = null;
    eyeShaderTicks = null;

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
      GPU.window().events.removeOnResize(onResize);
      onResize = null;
    }

    if(onKeyPress != null) {
      GPU.window().events.removeKeyPress(onKeyPress);
      onKeyPress = null;
    }

    if(onMouseRelease != null) {
      GPU.window().events.removeMouseRelease(onMouseRelease);
      onMouseRelease = null;
    }

    if(onShutdown != null) {
      GPU.window().events.removeShutdown(onShutdown);
      onShutdown = null;
    }

    spuThread.start();
    GPU.setStandardRenderer();

    synchronized(LOCK) {
      Fmv.playCurrentFmv();
      gameLoop();
    }
  }

  private static void loadGfx() {
    glEnable(GL_BLEND);
    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

    final Path vsh = Paths.get("gfx", "shaders", "vram.vsh");
    shader = loadShader(vsh, Paths.get("gfx", "shaders", "title.fsh"));
    shader.use();
    shaderAlpha = shader.new UniformFloat("alpha");

    title1Texture = Texture.png(Path.of(".", "gfx", "textures", "intro", "title1.png"));
    title2Texture = Texture.png(Path.of(".", "gfx", "textures", "intro", "title2.png"));
    loadingTexture = Texture.png(Path.of(".", "gfx", "textures", "intro", "loading.png"));
    eye = Texture.png(Path.of(".", "gfx", "textures", "loading.png"));

    eyeShader = loadShader(vsh, Paths.get("gfx", "shaders", "loading.fsh"));
    eyeShader.use();
    eyeShaderAlpha = eyeShader.new UniformFloat("alpha");
    eyeShaderTicks = eyeShader.new UniformFloat("ticks");

    eyeMesh = new Mesh(GL_TRIANGLE_STRIP, new float[] {
       0,  0, 0, 0,
       0, 32, 0, 1,
      32,  0, 1, 0,
      32, 32, 1, 1,
    }, 4);
    eyeMesh.attribute(0, 0L, 2, 4);
    eyeMesh.attribute(1, 2L, 2, 4);

    loadingMesh = new Mesh(GL_TRIANGLE_STRIP, new float[] {
        0,  0, 0, 0,
        0, 32, 0, 1,
      130,  0, 1, 0,
      130, 32, 1, 1,
    }, 4);
    loadingMesh.attribute(0, 0L, 2, 4);
    loadingMesh.attribute(1, 2L, 2, 4);

    transforms2 = ShaderManager.getUniformBuffer("transforms2");
    identity.identity();

    font = FontManager.get("default");

    onResize = GPU.window().events.onResize(GameEngine::windowResize);
    windowResize(GPU.window(), (int)(GPU.window().getWidth() * GPU.window().getScale()), (int)(GPU.window().getHeight() * GPU.window().getScale()));
    GPU.mainRenderer = GameEngine::renderIntro;

    onKeyPress = GPU.window().events.onKeyPress((window, key, scancode, mods) -> skip());
    onMouseRelease = GPU.window().events.onMouseRelease((window, x, y, button, mods) -> skip());
    onShutdown = GPU.window().events.onShutdown(Unpacker::stop);
  }

  private static void skip() {
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
    } else {
      if(!loading) {
        transitionToGame();
        return;
      }
    }

    shader.use();

    transforms2.set(identity);
    shaderAlpha.set(fade1 * fade1 * fade1);
    title1Texture.use();
    fullScrenMesh.draw();

    shaderAlpha.set(fade2 * fade2 * fade2);
    title2Texture.use();
    fullScrenMesh.draw();

    if(loading) {
      transforms2.set(eyeTransforms);
      eyeShader.use();
      eyeShaderAlpha.set(eyeFade);
      eyeShaderTicks.set(deltaMs / 10_000.0f);
      eye.use();
      eyeMesh.draw();

      transforms2.set(loadingTransforms);
      shader.use();
      shaderAlpha.set(loadingFade);
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
    final float windowScale = window.getScale();
    final float unscaledWidth = width / windowScale;
    final float unscaledHeight = height / windowScale;

    if(fullScrenMesh != null) {
      fullScrenMesh.delete();
    }

    final float aspect = (float)4 / 3;

    float w = unscaledWidth;
    float h = w / aspect;

    if(h > unscaledHeight) {
      h = unscaledHeight;
      w = h * aspect;
    }

    screenWidth = w;

    final float l = (unscaledWidth - w) / 2;
    final float t = (unscaledHeight - h) / 2;
    final float r = l + w;
    final float b = t + h;

    fullScrenMesh = new Mesh(GL_TRIANGLE_STRIP, new float[] {
      l, t, 0, 0,
      l, b, 0, 1,
      r, t, 1, 0,
      r, b, 1, 1,
    }, 4);
    fullScrenMesh.attribute(0, 0L, 2, 4);
    fullScrenMesh.attribute(1, 2L, 2, 4);

    eyeTransforms.translation(10.0f, unscaledHeight - 42.0f, 0.0f);
    loadingTransforms.translation(46.0f, unscaledHeight - 42.0f, 0.0f);
  }

  private static Shader loadShader(final Path vsh, final Path fsh) {
    final Shader shader;

    try {
      shader = new Shader(vsh, fsh);
    } catch(final IOException e) {
      throw new RuntimeException("Failed to load vram shader", e);
    }

    shader.bindUniformBlock("transforms", Shader.UniformBuffer.TRANSFORM);
    shader.bindUniformBlock("transforms2", Shader.UniformBuffer.TRANSFORM2);
    return shader;
  }
}
