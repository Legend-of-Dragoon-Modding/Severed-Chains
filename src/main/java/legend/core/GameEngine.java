package legend.core;

import legend.core.gpu.Gpu;
import legend.core.memory.Memory;
import legend.core.memory.Value;
import legend.core.memory.segments.RamSegment;
import legend.core.opengl.Mesh;
import legend.core.opengl.Shader;
import legend.core.opengl.Texture;
import legend.core.opengl.Window;
import legend.core.spu.Spu;
import legend.game.Scus94491BpeSegment;
import legend.game.Scus94491BpeSegment_8002;
import legend.game.Scus94491BpeSegment_8003;
import legend.game.Scus94491BpeSegment_8004;
import legend.game.Scus94491BpeSegment_800e;
import legend.game.fmv.Fmv;
import legend.game.modding.registries.Registries;
import legend.game.scripting.ScriptManager;
import legend.game.unpacker.Unpacker;
import legend.game.unpacker.UnpackerException;
import legend.integration.core.PlaySubscriber;
import legend.integration.youtube.YouTubePublisher;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Path;
import java.nio.file.Paths;

import static legend.game.Scus94491BpeSegment._80010004;
import static legend.game.Scus94491BpeSegment.gameLoop;
import static legend.game.Scus94491BpeSegment.loadFile;
import static legend.game.Scus94491BpeSegment_8004.overlays_8004db88;
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

  public static final Registries REGISTRIES = new Registries();
  private static final Registries.Access REGISTRY_ACCESS = REGISTRIES.new Access();

  public static final ScriptManager SCRIPTS = new ScriptManager();

  public static final Cpu CPU;
  public static final Gpu GPU;
  public static final Spu SPU;

  public static final Thread hardwareThread;
  public static final Thread gpuThread;
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

    MEMORY.addSegment(new RamSegment(0x0001_0000L, 0x4f_0000));
    MEMORY.addSegment(new RamSegment(0x1f80_0000L, 0x400));

    CPU = new Cpu();
    GPU = new Gpu();
    SPU = new Spu(MEMORY);

    hardwareThread = Thread.currentThread();
    hardwareThread.setName("Hardware");
    gpuThread = new Thread(GPU);
    gpuThread.setName("GPU");
    spuThread = new Thread(SPU);
    spuThread.setName("SPU");
  }

  private static final Value _80010000 = MEMORY.ref(4, 0x80010000L);
  private static final Value bpe_80188a88 = MEMORY.ref(4, 0x80188a88L);

  private static final Object LOCK = new Object();

  private static Window.Events.Resize onResize;
  private static Window.Events.Key onKeyPress;
  private static Window.Events.Click onMouseRelease;

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

  private static final FloatBuffer transform2Buffer = BufferUtils.createFloatBuffer(4 * 4);
  private static Shader.UniformBuffer transforms2;
  private static final Matrix4f identity = new Matrix4f();
  private static final Matrix4f eyeTransforms = new Matrix4f();
  private static final Matrix4f loadingTransforms = new Matrix4f();

  private static boolean loading;

  public static void start() {
    gpuThread.start();

    LOGGER.info("--- Legend start ---");

    loading = true;
    GPU.mainRenderer = GameEngine::loadGfx;

    synchronized(LOCK) {
      try {
        Unpacker.unpack();
      } catch(final UnpackerException e) {
        throw new RuntimeException("Failed to unpack files", e);
      }

      final byte[] fileData = Unpacker.loadFile("SCUS_944.91");
      MEMORY.setBytes(MathHelper.get(fileData, 0x18, 4), fileData, 0x800, (int)MathHelper.get(fileData, 0x1c, 4));

      final byte[] archive = MEMORY.getBytes(bpe_80188a88.getAddress(), 221736);
      final byte[] decompressed = Unpacker.decompress(archive);
      MEMORY.setBytes(_80010000.getAddress(), decompressed);

      MEMORY.addFunctions(Scus94491BpeSegment.class);
      MEMORY.addFunctions(Scus94491BpeSegment_8002.class);
      MEMORY.addFunctions(Scus94491BpeSegment_8003.class);
      MEMORY.addFunctions(Scus94491BpeSegment_8004.class);
      MEMORY.addFunctions(Scus94491BpeSegment_800e.class);

      // Load S_ITEM temporarily to get item names
      loadFile(overlays_8004db88.get(2), _80010004.get(), (address, size, integer) -> { }, 0, 0x10L);

      REGISTRY_ACCESS.initialize();

      Scus94491BpeSegment_8002.start();
      loading = false;

      final String liveChatId = Config.getYouTubeLiveChatId();
      if(!liveChatId.isBlank()) {
        final String apiKey = Config.getYouTubeApiKey();
        final PlaySubscriber sub = new PlaySubscriber();
        final YouTubePublisher pub = new YouTubePublisher(liveChatId, apiKey);
        pub.subscribe(sub);
        pub.play();
      }
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

    transforms2 = new Shader.UniformBuffer((long)transform2Buffer.capacity() * Float.BYTES, Shader.UniformBuffer.TRANSFORM2);
    identity.identity();

    onResize = GPU.window().events.onResize(GameEngine::windowResize);
    windowResize(GPU.window(), (int)(GPU.window().getWidth() * GPU.window().getScale()), (int)(GPU.window().getHeight() * GPU.window().getScale()));
    GPU.mainRenderer = GameEngine::renderIntro;

    onKeyPress = GPU.window().events.onKeyPress((window, key, scancode, mods) -> skip());
    onMouseRelease = GPU.window().events.onMouseRelease((window, x, y, button, mods) -> skip());

    time = System.nanoTime();
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
