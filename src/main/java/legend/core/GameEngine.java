package legend.core;

import legend.core.gpu.Gpu;
import legend.core.gte.Gte;
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
import legend.core.ui.ScreenStack;
import legend.game.Scus94491BpeSegment_8002;
import legend.game.fmv.Fmv;
import legend.game.i18n.LangManager;
import legend.game.input.Input;
import legend.game.modding.ModManager;
import legend.game.modding.coremod.CoreMod;
import legend.game.modding.coremod.character.CharacterData;
import legend.game.modding.events.EventManager;
import legend.game.modding.registries.Registries;
import legend.game.saves.ConfigCollection;
import legend.game.saves.ConfigStorage;
import legend.game.saves.ConfigStorageLocation;
import legend.game.saves.SaveManager;
import legend.game.saves.serializers.RetailSerializer;
import legend.game.saves.serializers.V1Serializer;
import legend.game.saves.serializers.V2Serializer;
import legend.game.saves.serializers.V3Serializer;
import legend.game.scripting.ScriptManager;
import legend.game.sound.Sequencer;
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

import static legend.game.Scus94491BpeSegment.gameLoop;
import static legend.game.Scus94491BpeSegment.startSound;
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
  public static final SaveManager SAVES = new SaveManager(V3Serializer.MAGIC_V3, V3Serializer::toV3);

  public static final RenderEngine RENDERER = new RenderEngine();
  public static final ScreenStack SCREENS = new ScreenStack();

  public static final Gte GTE;
  public static final Gpu GPU;
  public static final Spu SPU;

  public static final Thread hardwareThread;
  public static final Thread spuThread;

  public static boolean legacyUi;

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

    GTE = new Gte();
    GPU = new Gpu();
    SPU = new Spu();

    hardwareThread = Thread.currentThread();
    hardwareThread.setName("Hardware");
    spuThread = new Thread(SPU);
    spuThread.setName("SPU");
  }

  private static final Value _80010000 = MEMORY.ref(4, 0x80010000L);

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
  private static final Matrix4f backgroundTransforms = new Matrix4f();
  private static final Matrix4f textTransforms = new Matrix4f();
  private static final Matrix4f eyeTransforms = new Matrix4f();
  private static final Matrix4f loadingTransforms = new Matrix4f();

  private static boolean loading;

  public static boolean isLoading() {
    return loading;
  }

  public static void start() throws IOException {
    final Thread thread = new Thread(() -> {
      try {
        LOGGER.info("--- Legend start ---");

        loading = true;
        RENDERER.setRenderCallback(GameEngine::loadGfx);

        Files.createDirectories(Path.of("saves"));
        SAVES.registerDeserializer(RetailSerializer::fromRetailMatcher, RetailSerializer::fromRetail);
        SAVES.registerDeserializer(V1Serializer::fromV1Matcher, V1Serializer::fromV1);
        SAVES.registerDeserializer(V2Serializer::fromV2Matcher, V2Serializer::fromV2);
        SAVES.registerDeserializer(V3Serializer::fromV3Matcher, V3Serializer::fromV3);

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

          loadCharacterData();

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
    RENDERER.init();
    GPU.init();
    RENDERER.run();
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

    MOD_ACCESS.loadingComplete();

    return missingMods;
  }

  public static void bootRegistries() {
    REGISTRY_ACCESS.initializeRemaining();
  }

  private static void loadCharacterData() throws IOException {
    String[] characters = {"dart", "lavitz", "shana", "rose", "haschel", "albert", "meru", "kongol", "miranda"};
    int[] additions = {7, 5, 1, 4, 6, 5, 5, 3, 1};

    for(int i = 0; i < 9; i++) {
      CoreMod.CHARACTER_DATA[i] = new CharacterData();
      CoreMod.loadCharacterXp(i, characters[i]);
      CoreMod.loadCharacterStats(i, characters[i]);
      CoreMod.loadCharacterDragoonXp(i, characters[i]);
      CoreMod.loadCharacterDragoonStats(i, characters[i]);
      CoreMod.loadCharacterAdditions(i, characters[i], additions[i]);
    }
    LOGGER.info("CoreMod Character Data Loaded");
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

    if(onShutdown != null) {
      RENDERER.events().removeShutdown(onShutdown);
      onShutdown = null;
    }

    spuThread.start();

    synchronized(LOCK) {
      Input.init();

      startSound();
      gameLoop();
      Fmv.playCurrentFmv();
    }
  }

  private static void loadGfx() {
    RENDERER.camera().moveTo(0.0f, 0.0f, -2.0f);

    glEnable(GL_BLEND);
    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

    final Path vsh = Paths.get("gfx", "shaders", "simple.vsh");
    shader = loadShader(vsh, Paths.get("gfx", "shaders", "title.fsh"));
    shader.use();
    shaderAlpha = shader.new UniformFloat("alpha");

    title1Texture = Texture.filteredPng(Path.of(".", "gfx", "textures", "intro", "title1.png"));
    title2Texture = Texture.filteredPng(Path.of(".", "gfx", "textures", "intro", "title2.png"));
    loadingTexture = Texture.png(Path.of(".", "gfx", "textures", "intro", "loading.png"));
    eye = Texture.png(Path.of(".", "gfx", "textures", "loading.png"));

    eyeShader = loadShader(vsh, Paths.get("gfx", "shaders", "loading.fsh"));
    eyeShader.use();
    eyeShaderAlpha = eyeShader.new UniformFloat("alpha");
    eyeShaderTicks = eyeShader.new UniformFloat("ticks");

    eyeMesh = new Mesh(GL_TRIANGLE_STRIP, new float[] {
      0.0f, 0.0f, 0, 0, 0,
      0.0f, 0.1f, 0, 0, 1,
      0.1f, 0.0f, 0, 1, 0,
      0.1f, 0.1f, 0, 1, 1,
    }, 4);
    eyeMesh.attribute(0, 0L, 3, 5);
    eyeMesh.attribute(1, 3L, 2, 5);

    loadingMesh = new Mesh(GL_TRIANGLE_STRIP, new float[] {
          0.0f, 0.0f, 0, 0, 0,
          0.0f, 0.1f, 0, 0, 1,
      0.40625f, 0.0f, 0, 1, 0,
      0.40625f, 0.1f, 0, 1, 1,
    }, 4);
    loadingMesh.attribute(0, 0L, 3, 5);
    loadingMesh.attribute(1, 3L, 2, 5);

    transforms2 = ShaderManager.getUniformBuffer("transforms2");
    identity.identity();

    font = FontManager.get("default");

    onResize = RENDERER.events().onResize(GameEngine::windowResize);
    windowResize(RENDERER.window(), (int)(RENDERER.window().getWidth() * RENDERER.window().getScale()), (int)(RENDERER.window().getHeight() * RENDERER.window().getScale()));
    RENDERER.setRenderCallback(GameEngine::renderIntro);

    onKeyPress = RENDERER.events().onKeyPress((window, key, scancode, mods) -> skip());
    onMouseRelease = RENDERER.events().onMouseRelease((window, x, y, button, mods) -> skip());
    onShutdown = RENDERER.events().onShutdown(Unpacker::stop);
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

    transforms2.set(backgroundTransforms);
    shaderAlpha.set(fade1 * fade1 * fade1);
    title1Texture.use();
    fullScrenMesh.draw();

    transforms2.set(textTransforms);
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
    if(fullScrenMesh != null) {
      fullScrenMesh.delete();
    }

    final float aspect = (float)width / height;

    float w = width;
    float h = w / aspect;

    if(h > height) {
      h = height;
      w = h * aspect;
    }

    screenWidth = w;

    final float textureWidth = (float)title1Texture.width / title1Texture.height;
    fullScrenMesh = new Mesh(GL_TRIANGLE_STRIP, new float[] {
      -textureWidth, -1.0f, 0.0f, 0, 0,
      -textureWidth,  1.0f, 0.0f, 0, 1,
       textureWidth, -1.0f, 0.0f, 1, 0,
       textureWidth,  1.0f, 0.0f, 1, 1,
    }, 4);
    fullScrenMesh.attribute(0, 0L, 3, 5);
    fullScrenMesh.attribute(1, 3L, 2, 5);

    backgroundTransforms.translation(0.0f, 0.0f, 0.4f);
    textTransforms.translation(0.0f, 0.0f, 0.3f);
    loadingTransforms.translation(-0.85f * aspect + 0.075f, -0.88f, 0.2f);
    eyeTransforms.translation(-0.85f * aspect, -0.85f, 0.1f);
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
