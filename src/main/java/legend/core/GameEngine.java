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
import legend.game.unpacker.Unpacker;
import legend.game.unpacker.UnpackerException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

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

  private static Shader shader;
  private static Shader.UniformFloat shaderAlpha;
  private static Texture title1;
  private static Texture title2;
  private static Mesh mesh;

  public static void start() {
    gpuThread.start();

    LOGGER.info("--- Legend start ---");

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

      Scus94491BpeSegment_8002.start();
    }
  }

  private static void transitionToGame() {
    glDisable(GL_BLEND);

    shaderAlpha = null;

    if(shader != null) {
      shader.delete();
      shader = null;
    }

    if(title1 != null) {
      title1.delete();
      title1 = null;
    }

    if(title2 != null) {
      title2.delete();
      title2 = null;
    }

    if(mesh != null) {
      mesh.delete();
      mesh = null;
    }

    if(onResize != null) {
      GPU.window().events.removeOnResize(onResize);
      onResize = null;
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

    shader = loadShader(Paths.get("gfx", "shaders", "vram.vsh"), Paths.get("gfx", "shaders", "title.fsh"));
    shader.use();
    shaderAlpha = shader.new UniformFloat("alpha");

    title1 = Texture.png(Path.of(".", "gfx", "textures", "intro", "title1.png"));
    title2 = Texture.png(Path.of(".", "gfx", "textures", "intro", "title2.png"));
    onResize = GPU.window().events.onResize(GameEngine::windowResize);
    windowResize(GPU.window(), (int)(GPU.window().getWidth() * GPU.window().getScale()), (int)(GPU.window().getHeight() * GPU.window().getScale()));
    GPU.mainRenderer = GameEngine::renderIntro;

    time = System.nanoTime();
  }

  private static long time;
  private static float fade1;
  private static float fade2;

  private static void renderIntro() {
    final int deltaMs = (int)((System.nanoTime() - time) / 1_000_000);

    if(deltaMs < 5000) {
      fade1 += 0.005f;
      if(fade1 > 1.0f) {
        fade1 = 1.0f;
      }
      fade2 = fade1;
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
    } else {
      transitionToGame();
      return;
    }

    shader.use();

    shaderAlpha.set(fade1 * fade1 * fade1);
    title1.use();
    mesh.draw();

    shaderAlpha.set(fade2 * fade2 * fade2);
    title2.use();
    mesh.draw();
  }

  private static void windowResize(final Window window, final int width, final int height) {
    final float windowScale = window.getScale();
    final float unscaledWidth = width / windowScale;
    final float unscaledHeight = height / windowScale;

    if(mesh != null) {
      mesh.delete();
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

    mesh = new Mesh(GL_TRIANGLE_STRIP, new float[] {
      l, t, 0, 0,
      l, b, 0, 1,
      r, t, 1, 0,
      r, b, 1, 1,
    }, 4);
    mesh.attribute(0, 0L, 2, 4);
    mesh.attribute(1, 2L, 2, 4);
  }

  public static boolean isAlive() {
    return gpuThread.isAlive() && spuThread.isAlive();
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
