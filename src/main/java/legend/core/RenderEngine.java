package legend.core;

import legend.core.opengl.Camera;
import legend.core.opengl.Context;
import legend.core.opengl.QuaternionCamera;
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
import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT_SHIFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_TAB;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;
import static org.lwjgl.opengl.GL11C.GL_FILL;
import static org.lwjgl.opengl.GL11C.GL_FRONT_AND_BACK;
import static org.lwjgl.opengl.GL11C.GL_LINE;
import static org.lwjgl.opengl.GL11C.glPolygonMode;

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

  private static final float MOVE_SPEED = 0.16f;
  private static final float MOUSE_SPEED = 0.00175f;

  private boolean firstMouse = true;
  private double lastX = Config.windowWidth() / 2.0f;
  private double lastY = Config.windowHeight() / 2.0f;
  private float yaw;
  private float pitch;

  private boolean movingLeft;
  private boolean movingRight;
  private boolean movingForward;
  private boolean movingBackward;
  private boolean movingUp;
  private boolean movingDown;

  private boolean wireframeMode;

  public Window.Events events() {
    return this.window.events;
  }

  public Window window() {
    return this.window;
  }

  public Camera camera() {
    return this.camera;
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
//    this.camera = new BasicCamera(0.0f, 0.0f);
    this.camera = new QuaternionCamera(0.0f, 0.0f, 0.0f);
    this.window = new Window("Legend of Dragoon", Config.windowWidth(), Config.windowHeight());
    this.window.setFpsLimit(60);

//    this.window.events.onMouseMove(this::onMouseMove);
//    this.window.events.onKeyPress(this::onKeyPress);
//    this.window.events.onKeyRelease(this::onKeyRelease);
//    this.window.hideCursor();

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

      if(this.movingLeft) {
        this.ctx.camera.strafe(-MOVE_SPEED);
      }

      if(this.movingRight) {
        this.ctx.camera.strafe(MOVE_SPEED);
      }

      if(this.movingForward) {
        this.ctx.camera.move(-MOVE_SPEED);
      }

      if(this.movingBackward) {
        this.ctx.camera.move(MOVE_SPEED);
      }

      if(this.movingUp) {
        this.ctx.camera.jump(MOVE_SPEED);
      }

      if(this.movingDown) {
        this.ctx.camera.jump(-MOVE_SPEED);
      }
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

  private void onMouseMove(final Window window, final double x, final double y) {
    if(this.firstMouse) {
      this.lastX = x;
      this.lastY = y;
      this.firstMouse = false;
    }

    this.yaw   += (x - this.lastX) * MOUSE_SPEED;
    this.pitch += (this.lastY - y) * MOUSE_SPEED;

    this.pitch = (float)Math.max(-Math.PI / 2, Math.min(this.pitch, Math.PI / 2));

    this.lastX = x;
    this.lastY = y;

    this.ctx.camera.look(-this.yaw, this.pitch);
  }

  private void onKeyPress(final Window window, final int key, final int scancode, final int mods) {
    switch(key) {
      case GLFW_KEY_W -> this.movingForward = true;
      case GLFW_KEY_S -> this.movingBackward = true;
      case GLFW_KEY_A -> this.movingLeft = true;
      case GLFW_KEY_D -> this.movingRight = true;
      case GLFW_KEY_SPACE -> this.movingUp = true;
      case GLFW_KEY_LEFT_SHIFT -> this.movingDown = true;
      case GLFW_KEY_ESCAPE -> this.window.close();
      case GLFW_KEY_TAB -> {
        this.wireframeMode = !this.wireframeMode;
        glPolygonMode(GL_FRONT_AND_BACK, this.wireframeMode ? GL_LINE : GL_FILL);
      }
    }
  }

  private void onKeyRelease(final Window window, final int key, final int scancode, final int mods) {
    switch(key) {
      case GLFW_KEY_W -> this.movingForward = false;
      case GLFW_KEY_S -> this.movingBackward = false;
      case GLFW_KEY_A -> this.movingLeft = false;
      case GLFW_KEY_D -> this.movingRight = false;
      case GLFW_KEY_SPACE -> this.movingUp = false;
      case GLFW_KEY_LEFT_SHIFT -> this.movingDown = false;
    }
  }
}
