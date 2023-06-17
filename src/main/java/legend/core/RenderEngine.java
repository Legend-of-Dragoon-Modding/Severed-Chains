package legend.core;

import legend.core.opengl.Camera;
import legend.core.opengl.Context;
import legend.core.opengl.Shader;
import legend.core.opengl.ShaderManager;
import legend.core.opengl.Window;
import legend.core.opengl.fonts.Font;
import legend.core.opengl.fonts.FontManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Paths;

import static legend.core.GameEngine.EVENTS;

public class RenderEngine {
  private static final Logger LOGGER = LogManager.getFormatterLogger();

  private Camera camera;
  private Window window;
  private Context ctx;
  private Shader.UniformBuffer transforms2;
  private final Matrix4f transforms = new Matrix4f();

  private long lastFrame;
  private double vsyncCount;
  private float fps;

  private Runnable renderCallback = () -> { };

  public Window.Events events() {
    return this.window.events;
  }

  public Window window() {
    return this.window;
  }

  public int getVsyncCount() {
    return (int)this.vsyncCount;
  }

  public float getFps() {
    return this.fps;
  }

  public Runnable setRenderCallback(final Runnable renderCallback) {
    final Runnable oldCallback = this.renderCallback;
    this.renderCallback = renderCallback;
    return oldCallback;
  }

  public void init() {
    this.camera = new Camera(0.0f, 0.0f);
    this.window = new Window("Legend of Dragoon", Config.windowWidth(), Config.windowHeight());
    this.window.setFpsLimit(60);
    this.ctx = new Context(this.window, this.camera);

    try {
      final Shader simpleShader = new Shader(Paths.get("gfx/shaders/simple.vsh"), Paths.get("gfx/shaders/simple.fsh"));
      simpleShader.bindUniformBlock("transforms", Shader.UniformBuffer.TRANSFORM);
      simpleShader.bindUniformBlock("transforms2", Shader.UniformBuffer.TRANSFORM2);
      ShaderManager.addShader("simple", simpleShader);
    } catch(final IOException e) {
      throw new RuntimeException("Failed to load simple shader", e);
    }

    try {
      final Shader fontShader = new Shader(Paths.get("gfx/shaders/font.vsh"), Paths.get("gfx/shaders/font.fsh"));
      fontShader.bindUniformBlock("transforms", Shader.UniformBuffer.TRANSFORM);
      fontShader.bindUniformBlock("transforms2", Shader.UniformBuffer.TRANSFORM2);
      fontShader.use();
      fontShader.bindUniform("colour");
      ShaderManager.addShader("font", fontShader);

      FontManager.add("default", new Font(Paths.get("gfx/fonts/consolas.ttf")));
    } catch(final IOException e) {
      throw new RuntimeException("Failed to load font", e);
    }

    final FloatBuffer transform2Buffer = BufferUtils.createFloatBuffer(4 * 4);
    this.transforms2 = ShaderManager.addUniformBuffer("transforms2", new Shader.UniformBuffer((long)transform2Buffer.capacity() * Float.BYTES, Shader.UniformBuffer.TRANSFORM2));

    this.ctx.onDraw(() -> {
      EVENTS.clearStaleRefs();

      // Restore model buffer to identity
      this.transforms.identity();
      this.transforms2.set(this.transforms);

      this.renderCallback.run();

      this.fps = 1.0f / ((System.nanoTime() - this.lastFrame) / (1_000_000_000 / 30.0f)) * 30.0f;
      this.lastFrame = System.nanoTime();
      this.vsyncCount += 60.0d * Config.getGameSpeedMultiplier() / this.window.getFpsLimit();
    });
  }

  public void run() {
    this.window.show();

    this.lastFrame = System.nanoTime();

    try {
      this.window.run();
    } catch(final Throwable t) {
      LOGGER.error("Shutting down due to exception:", t);
      this.window.close();
    } finally {
      FontManager.free();
      Window.free();
    }
  }
}
