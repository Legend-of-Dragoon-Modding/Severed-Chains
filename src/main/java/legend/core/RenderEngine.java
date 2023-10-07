package legend.core;

import legend.core.opengl.BasicCamera;
import legend.core.opengl.Camera;
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
import static org.lwjgl.glfw.GLFW.GLFW_KEY_M;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_TAB;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;
import static org.lwjgl.opengl.GL11C.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11C.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11C.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11C.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11C.GL_FILL;
import static org.lwjgl.opengl.GL11C.GL_FRONT_AND_BACK;
import static org.lwjgl.opengl.GL11C.GL_LINE;
import static org.lwjgl.opengl.GL11C.glClear;
import static org.lwjgl.opengl.GL11C.glClearColor;
import static org.lwjgl.opengl.GL11C.glDisable;
import static org.lwjgl.opengl.GL11C.glEnable;
import static org.lwjgl.opengl.GL11C.glPolygonMode;
import static org.lwjgl.opengl.GL11C.glViewport;

public class RenderEngine {
  private static final Logger LOGGER = LogManager.getFormatterLogger();

  private static final float FOV = (float)Math.toRadians(45.0f);

  private Camera camera2d;
  private Camera camera3d;
  private Window window;
  private Shader.UniformBuffer transformsUniform;
  private Shader.UniformBuffer transforms2Uniform;
  private final Matrix4f perspectiveProjection = new Matrix4f();
  private final Matrix4f orthographicProjection = new Matrix4f();
  private final Matrix4f transforms = new Matrix4f();
  private final FloatBuffer transformsBuffer = BufferUtils.createFloatBuffer(4 * 4 * 2);

  private int width;
  private int height;

  private long lastFrame;
  private double vsyncCount;
  private float fps;

  private Runnable renderCallback = () -> { };

  private static final float MOVE_SPEED = 0.96f;
  private static final float MOUSE_SPEED = 0.00175f;

  private boolean firstMouse = true;
  private double lastX = Config.windowWidth() / 2.0f;
  private double lastY = Config.windowHeight() / 2.0f;
  private float yaw;
  private float pitch;

  private boolean allowMovement;

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
    return this.camera3d;
  }

  public int getVsyncCount() {
    return (int)this.vsyncCount;
  }

  public float getFps() {
    return this.fps;
  }

  public void setClearColour(final float red, final float green, final float blue) {
    glClearColor(red, green, blue, 1.0f);
  }

  public void clear() {
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
  }

  public void clearColour() {
    glClear(GL_COLOR_BUFFER_BIT);
  }

  public Runnable setRenderCallback(final Runnable renderCallback) {
    final Runnable oldCallback = this.renderCallback;
    this.renderCallback = renderCallback;
    return oldCallback;
  }

  public void delete() {
    ShaderManager.delete();
  }

  public void init() {
    this.camera2d = new BasicCamera(0.0f, 0.0f);
    this.camera3d = new QuaternionCamera(0.0f, 0.0f, 0.0f);
    this.window = new Window("Legend of Dragoon", Config.windowWidth(), Config.windowHeight());
    this.window.setFpsLimit(60);

    this.window.events.onResize(this::onResize);

    this.window.events.onMouseMove(this::onMouseMove);
    this.window.events.onKeyPress(this::onKeyPress);
    this.window.events.onKeyRelease(this::onKeyRelease);

    try {
      final Shader simpleShader = new Shader(Paths.get("gfx/shaders/simple.vsh"), Paths.get("gfx/shaders/simple.fsh"));
      simpleShader.bindUniformBlock("transforms", Shader.UniformBuffer.TRANSFORM);
      simpleShader.bindUniformBlock("transforms2", Shader.UniformBuffer.TRANSFORM2);
      ShaderManager.addShader("simple", simpleShader);
    } catch(final IOException e) {
      throw new RuntimeException("Failed to load simple shader", e);
    }

    try {
      final Shader simpleShader = new Shader(Paths.get("gfx/shaders/tmd.vsh"), Paths.get("gfx/shaders/tmd.fsh"));
      simpleShader.bindUniformBlock("transforms", Shader.UniformBuffer.TRANSFORM);
      simpleShader.bindUniformBlock("transforms2", Shader.UniformBuffer.TRANSFORM2);
      ShaderManager.addShader("tmd", simpleShader);
    } catch(final IOException e) {
      throw new RuntimeException("Failed to load TMD shader", e);
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
    this.transformsUniform = new Shader.UniformBuffer((long)this.transformsBuffer.capacity() * Float.BYTES, Shader.UniformBuffer.TRANSFORM);
    this.transforms2Uniform = ShaderManager.addUniformBuffer("transforms2", new Shader.UniformBuffer((long)transform2Buffer.capacity() * Float.BYTES, Shader.UniformBuffer.TRANSFORM2));

    this.window.events.onDraw(() -> {
      this.pre();

      EVENTS.clearStaleRefs();

      // Restore model buffer to identity
      this.transforms.identity();
      this.transforms2Uniform.set(this.transforms);

      this.renderCallback.run();

      this.fps = 1.0f / ((System.nanoTime() - this.lastFrame) / (1_000_000_000 / 30.0f)) * 30.0f;
      this.lastFrame = System.nanoTime();
      this.vsyncCount += 60.0d * Config.getGameSpeedMultiplier() / this.window.getFpsLimit();

      if(this.movingLeft) {
        this.camera3d.strafe(-MOVE_SPEED);
      }

      if(this.movingRight) {
        this.camera3d.strafe(MOVE_SPEED);
      }

      if(this.movingForward) {
        this.camera3d.move(MOVE_SPEED);
      }

      if(this.movingBackward) {
        this.camera3d.move(-MOVE_SPEED);
      }

      if(this.movingUp) {
        this.camera3d.jump(MOVE_SPEED);
      }

      if(this.movingDown) {
        this.camera3d.jump(-MOVE_SPEED);
      }
    });
  }

  public void setProjectionMode(final ProjectionMode projectionMode) {
    switch(projectionMode) {
      case _2D -> {
        glDisable(GL_CULL_FACE);
        glDisable(GL_DEPTH_TEST);
        this.setTransforms(this.camera2d, this.orthographicProjection);
      }

      case _3D -> {
        glEnable(GL_CULL_FACE);
        glEnable(GL_DEPTH_TEST);
        this.setTransforms(this.camera3d, this.perspectiveProjection);
      }
    }
  }

  private void setTransforms(final Camera camera, final Matrix4f projection) {
    camera.get(this.transformsBuffer);
    projection.get(16, this.transformsBuffer);
    this.transformsUniform.set(this.transformsBuffer);
  }

  private void pre() {
    glViewport(0, 0, (int)(this.width * this.window.getScale()), (int)(this.height * this.window.getScale()));

    // Update global transforms (default to 3D)
    this.setProjectionMode(ProjectionMode._3D);

    // Render scene
    this.clear();
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

  private void onResize(final Window window, final int width, final int height) {
    if(width == 0 && height == 0) {
      return;
    }

    this.perspectiveProjection.setPerspective(FOV, (float)width / height, 0.1f, 1500.0f);
    this.orthographicProjection.setOrtho2D(0.0f, width, height, 0.0f);

    this.width = width;
    this.height = height;
  }

  private void onMouseMove(final Window window, final double x, final double y) {
    if(this.firstMouse) {
      this.lastX = x;
      this.lastY = y;
      this.firstMouse = false;
    }

    if(this.allowMovement) {
      this.yaw += (x - this.lastX) * MOUSE_SPEED;
      this.pitch += (this.lastY - y) * MOUSE_SPEED;

      this.pitch = MathHelper.clamp(this.pitch, -MathHelper.PI / 2, MathHelper.PI / 2);

      this.lastX = x;
      this.lastY = y;

      this.camera3d.look(this.yaw, -this.pitch);
    }
  }

  private void onKeyPress(final Window window, final int key, final int scancode, final int mods) {
    if(this.allowMovement) {
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

    if(key == GLFW_KEY_M) {
      this.allowMovement = !this.allowMovement;
      LOGGER.info("Allow movement: %b", this.allowMovement);

      if(this.allowMovement) {
        this.window.hideCursor();
      } else {
        this.window.showCursor();
      }
    }
  }

  private void onKeyRelease(final Window window, final int key, final int scancode, final int mods) {
    if(this.allowMovement) {
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
}
