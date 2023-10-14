package legend.core;

import legend.core.gte.MV;
import legend.core.opengl.BasicCamera;
import legend.core.opengl.Camera;
import legend.core.opengl.FrameBuffer;
import legend.core.opengl.Mesh;
import legend.core.opengl.Obj;
import legend.core.opengl.QuaternionCamera;
import legend.core.opengl.Shader;
import legend.core.opengl.ShaderManager;
import legend.core.opengl.Texture;
import legend.core.opengl.Window;
import legend.core.opengl.fonts.Font;
import legend.core.opengl.fonts.FontManager;
import legend.game.types.Translucency;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static legend.core.GameEngine.EVENTS;
import static legend.core.GameEngine.GPU;
import static legend.core.GameEngine.RENDERER;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT_SHIFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_M;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_TAB;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;
import static org.lwjgl.opengl.GL11C.GL_ALWAYS;
import static org.lwjgl.opengl.GL11C.GL_BLEND;
import static org.lwjgl.opengl.GL11C.GL_COLOR;
import static org.lwjgl.opengl.GL11C.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11C.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11C.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11C.GL_DEPTH_COMPONENT;
import static org.lwjgl.opengl.GL11C.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11C.GL_FILL;
import static org.lwjgl.opengl.GL11C.GL_FLOAT;
import static org.lwjgl.opengl.GL11C.GL_FRONT_AND_BACK;
import static org.lwjgl.opengl.GL11C.GL_LESS;
import static org.lwjgl.opengl.GL11C.GL_LINE;
import static org.lwjgl.opengl.GL11C.GL_LINEAR;
import static org.lwjgl.opengl.GL11C.GL_ONE;
import static org.lwjgl.opengl.GL11C.GL_ONE_MINUS_SRC_COLOR;
import static org.lwjgl.opengl.GL11C.GL_RED;
import static org.lwjgl.opengl.GL11C.GL_RGBA;
import static org.lwjgl.opengl.GL11C.GL_STENCIL_BUFFER_BIT;
import static org.lwjgl.opengl.GL11C.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11C.GL_ZERO;
import static org.lwjgl.opengl.GL11C.glBlendFunc;
import static org.lwjgl.opengl.GL11C.glClear;
import static org.lwjgl.opengl.GL11C.glClearColor;
import static org.lwjgl.opengl.GL11C.glDepthFunc;
import static org.lwjgl.opengl.GL11C.glDepthMask;
import static org.lwjgl.opengl.GL11C.glDisable;
import static org.lwjgl.opengl.GL11C.glEnable;
import static org.lwjgl.opengl.GL11C.glPolygonMode;
import static org.lwjgl.opengl.GL11C.glViewport;
import static org.lwjgl.opengl.GL14C.GL_FUNC_ADD;
import static org.lwjgl.opengl.GL14C.glBlendEquation;
import static org.lwjgl.opengl.GL30C.GL_COLOR_ATTACHMENT0;
import static org.lwjgl.opengl.GL30C.GL_COLOR_ATTACHMENT1;
import static org.lwjgl.opengl.GL30C.GL_DEPTH_ATTACHMENT;
import static org.lwjgl.opengl.GL30C.GL_HALF_FLOAT;
import static org.lwjgl.opengl.GL30C.GL_R8;
import static org.lwjgl.opengl.GL30C.GL_RGBA16F;
import static org.lwjgl.opengl.GL30C.glClearBufferfv;
import static org.lwjgl.opengl.GL40C.glBlendFunci;

public class RenderEngine {
  private static final Logger LOGGER = LogManager.getFormatterLogger();

  private static final float FOV = (float)Math.toRadians(45.0f);

  private Camera camera2d;
  private Camera camera3d;
  private Window window;
  private Shader.UniformBuffer transformsUniform;
  private Shader.UniformBuffer transforms2Uniform;
  private Shader.UniformBuffer lightUniform;
  private final Matrix4f perspectiveProjection = new Matrix4f();
  private final Matrix4f orthographicProjection = new Matrix4f();
  private final Matrix4f transforms = new Matrix4f();
  private final FloatBuffer transformsBuffer = BufferUtils.createFloatBuffer(4 * 4 * 2);
  private final FloatBuffer transforms2Buffer = BufferUtils.createFloatBuffer(4 * 4);
  private final FloatBuffer lightBuffer = BufferUtils.createFloatBuffer(4 * 4 * 2 + 4);

  // Order-independent translucency
  private Shader tmdShader;
  private Shader tmdShaderTransparent;
  private Shader.UniformVec3 tmdShaderColour;
  private Shader.UniformVec3 tmdShaderTransparentColour;
  private FrameBuffer opaqueFrameBuffer;
  private FrameBuffer transparentFrameBuffer;
  private Texture opaqueTexture;
  private Texture depthTexture;
  private Texture accumTexture;
  private Texture revealTexture;
  private final float[] clear0 = {0.0f, 0.0f, 0.0f, 0.0f};
  private final float[] clear1 = {1.0f, 1.0f, 1.0f, 1.0f};

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

  private final QueuePool modelPool = new QueuePool();
  private final QueuePool orthoPool = new QueuePool();

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

  public void setClearColour(final float red, final float green, final float blue, final float alpha) {
    glClearColor(red, green, blue, alpha);
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

    final Shader simpleShader;
    try {
      simpleShader = new Shader(Paths.get("gfx/shaders/simple.vsh"), Paths.get("gfx/shaders/simple.fsh"));
      simpleShader.bindUniformBlock("transforms", Shader.UniformBuffer.TRANSFORM);
      simpleShader.bindUniformBlock("transforms2", Shader.UniformBuffer.TRANSFORM2);
      ShaderManager.addShader("simple", simpleShader);
    } catch(final IOException e) {
      throw new RuntimeException("Failed to load simple shader", e);
    }

    try {
      this.tmdShader = new Shader(Paths.get("gfx/shaders/tmd.vsh"), Paths.get("gfx/shaders/tmd.fsh"));
      this.tmdShader.bindUniformBlock("transforms", Shader.UniformBuffer.TRANSFORM);
      this.tmdShader.bindUniformBlock("transforms2", Shader.UniformBuffer.TRANSFORM2);
      this.tmdShader.bindUniformBlock("lighting", Shader.UniformBuffer.LIGHTING);

      this.tmdShader.use();
      this.tmdShader.new UniformInt("tex24").set(0);
      this.tmdShader.new UniformInt("tex15").set(1);
      this.tmdShaderColour = this.tmdShader.new UniformVec3("recolour");

      ShaderManager.addShader("tmd", this.tmdShader);
    } catch(final IOException e) {
      throw new RuntimeException("Failed to load TMD shader", e);
    }

    try {
      this.tmdShaderTransparent = new Shader(Paths.get("gfx/shaders/tmd.vsh"), Paths.get("gfx/shaders/tmd-transparent.fsh"));
      this.tmdShaderTransparent.bindUniformBlock("transforms", Shader.UniformBuffer.TRANSFORM);
      this.tmdShaderTransparent.bindUniformBlock("transforms2", Shader.UniformBuffer.TRANSFORM2);
      this.tmdShaderTransparent.bindUniformBlock("lighting", Shader.UniformBuffer.LIGHTING);

      this.tmdShaderTransparent.use();
      this.tmdShaderTransparent.new UniformInt("tex24").set(0);
      this.tmdShaderTransparent.new UniformInt("tex15").set(1);
      this.tmdShaderTransparentColour = this.tmdShaderTransparent.new UniformVec3("recolour");

      ShaderManager.addShader("tmd-transparent", this.tmdShaderTransparent);
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

    this.transformsUniform = new Shader.UniformBuffer((long)this.transformsBuffer.capacity() * Float.BYTES, Shader.UniformBuffer.TRANSFORM);

    this.transforms2Uniform = ShaderManager.addUniformBuffer("transforms2", new Shader.UniformBuffer((long)this.transforms2Buffer.capacity() * Float.BYTES, Shader.UniformBuffer.TRANSFORM2));

    this.lightUniform = ShaderManager.addUniformBuffer("lighting", new Shader.UniformBuffer((long)this.lightBuffer.capacity() * Float.BYTES, Shader.UniformBuffer.LIGHTING));

    final Shader compositeShader;
    try {
      compositeShader = new Shader(Paths.get("gfx/shaders/post.vsh"), Paths.get("gfx/shaders/composite.fsh"));
    } catch(final IOException e) {
      throw new RuntimeException("Failed to composite shader", e);
    }

    final Shader screenShader;
    try {
      screenShader = new Shader(Paths.get("gfx/shaders/post.vsh"), Paths.get("gfx/shaders/screen.fsh"));
    } catch(final IOException e) {
      throw new RuntimeException("Failed to composite shader", e);
    }

    final Mesh postQuad = new Mesh(GL_TRIANGLES, new float[] {
      -1.0f, -1.0f,  0.0f, 0.0f,
       1.0f, -1.0f,  1.0f, 0.0f,
      -1.0f,  1.0f,  0.0f, 1.0f,
       1.0f,  1.0f,  1.0f, 1.0f,
    }, new int[] {
      0, 3, 2,
      0, 1, 3,
    });

    postQuad.attribute(0, 0L, 2, 4);
    postQuad.attribute(1, 2L, 2, 4);

    this.window.events.onDraw(() -> {
      this.pre();

      EVENTS.clearStaleRefs();

      // Restore model buffer to identity
      this.transforms.identity();
      this.transforms2Uniform.set(this.transforms);

      this.renderCallback.run();

      this.opaqueFrameBuffer.bind();
      this.clear();

      this.transparentFrameBuffer.bind();
      glClearBufferfv(GL_COLOR, 0, this.clear0);
      glClearBufferfv(GL_COLOR, 1, this.clear1);

      RENDERER.setProjectionMode(ProjectionMode._3D);
      this.renderPool(this.modelPool);

      // set render states
      glDepthFunc(GL_ALWAYS);
      glEnable(GL_BLEND);
      glBlendFunc(GL_ONE, GL_ONE); //TODO this is only for B+F

      // bind opaque framebuffer
      this.opaqueFrameBuffer.bind();

      // use composite shader
      compositeShader.use();

      // draw screen quad
      this.accumTexture.use(0);
      this.revealTexture.use(1);
      postQuad.draw();

      // draw to backbuffer (final pass)
      // -----

      // set render states
      glDisable(GL_DEPTH_TEST);
      glDepthMask(true); // enable depth writes so glClear won't ignore clearing the depth buffer
      glDisable(GL_BLEND);

      // bind backbuffer
      FrameBuffer.unbind();
      this.setClearColour(0.0f, 0.0f, 0.0f);
      glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);

      // use screen shader
      screenShader.use();

      // draw final screen quad
      this.opaqueTexture.use();
      postQuad.draw();

      RENDERER.setProjectionMode(ProjectionMode._2D);
      this.tmdShader.use();
      GPU.useVramTexture();

      for(int i = 0; i < this.orthoPool.size(); i++) {
        final QueuedModel entry = this.orthoPool.get(i);
        entry.transforms.get(this.transforms2Buffer);
        this.transforms2Uniform.set(this.transforms2Buffer);
        this.tmdShaderColour.set(entry.colour);

        entry.lightDirection.get(this.lightBuffer);
        entry.lightColour.get(16, this.lightBuffer);
        entry.backgroundColour.get(32, this.lightBuffer);
        this.lightUniform.set(this.lightBuffer);

        entry.obj.render(null);

        for(int translucencyIndex = 0; translucencyIndex < Translucency.FOR_RENDERING.length; translucencyIndex++) {
          entry.obj.render(Translucency.FOR_RENDERING[translucencyIndex]);
        }
      }

      this.orthoPool.reset();

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
        this.camera3d.move(-MOVE_SPEED);
      }

      if(this.movingBackward) {
        this.camera3d.move(MOVE_SPEED);
      }

      if(this.movingUp) {
        this.camera3d.jump(-MOVE_SPEED);
      }

      if(this.movingDown) {
        this.camera3d.jump(MOVE_SPEED);
      }
    });
  }

  private void renderPool(final QueuePool pool) {
    glEnable(GL_DEPTH_TEST);
    glDepthFunc(GL_LESS);
    glDepthMask(true);
    glDisable(GL_BLEND);
    glEnable(GL_CULL_FACE);

    this.opaqueFrameBuffer.bind();
    this.tmdShader.use();
    GPU.useVramTexture();

    for(int i = 0; i < pool.size(); i++) {
      final QueuedModel entry = pool.get(i);
      entry.transforms.get(this.transforms2Buffer);
      this.transforms2Uniform.set(this.transforms2Buffer);
      this.tmdShaderColour.set(entry.colour);

      entry.lightDirection.get(this.lightBuffer);
      entry.lightColour.get(16, this.lightBuffer);
      entry.backgroundColour.get(32, this.lightBuffer);
      this.lightUniform.set(this.lightBuffer);

      entry.obj.render(null);
    }

    glDisable(GL_CULL_FACE);

    glDepthMask(false);
    glEnable(GL_BLEND);
    glBlendFunci(0, GL_ONE, GL_ONE);
    glBlendFunci(1, GL_ZERO, GL_ONE_MINUS_SRC_COLOR);
    glBlendEquation(GL_FUNC_ADD);

    this.transparentFrameBuffer.bind();
    this.tmdShaderTransparent.use();
    GPU.useVramTexture();

    for(int translucencyIndex = 0; translucencyIndex < Translucency.FOR_RENDERING.length; translucencyIndex++) {
      final Translucency translucency = Translucency.FOR_RENDERING[translucencyIndex];

//        switch(translucency) {
//          case HALF_B_PLUS_HALF_F ->
//            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
//
//          case B_PLUS_F ->
//            glBlendFunc(GL_ONE, GL_ONE);
//        }

      for(int i = 0; i < pool.size(); i++) {
        final QueuedModel entry = pool.get(i);
        entry.transforms.get(this.transforms2Buffer);
        this.transforms2Uniform.set(this.transforms2Buffer);
        this.tmdShaderTransparentColour.set(entry.colour);

        entry.lightDirection.get(this.lightBuffer);
        entry.lightColour.get(16, this.lightBuffer);
        entry.backgroundColour.get(32, this.lightBuffer);
        this.lightUniform.set(this.lightBuffer);

        entry.obj.render(translucency);
      }
    }

    pool.reset();
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

  public QueuedModel queueModel(final Obj obj) {
    final QueuedModel entry = this.modelPool.acquire();
    entry.obj = obj;
    entry.transforms.identity();
    entry.colour.set(1.0f, 1.0f, 1.0f);
    return entry;
  }

  public QueuedModel queueModel(final Obj obj, final MV mv) {
    final QueuedModel entry = this.modelPool.acquire();
    entry.obj = obj;
    entry.transforms.set(mv);
    entry.transforms.setTranslation(mv.transfer);
    entry.colour.set(1.0f, 1.0f, 1.0f);
    return entry;
  }

  public QueuedModel queueOrthoModel(final Obj obj) {
    final QueuedModel entry = this.orthoPool.acquire();
    entry.obj = obj;
    entry.transforms.identity();
    entry.colour.set(1.0f, 1.0f, 1.0f);
    return entry;
  }

  public QueuedModel queueOrthoModel(final Obj obj, final MV mv) {
    final QueuedModel entry = this.orthoPool.acquire();
    entry.obj = obj;
    entry.transforms.set(mv);
    entry.transforms.setTranslation(mv.transfer);
    entry.colour.set(1.0f, 1.0f, 1.0f);
    return entry;
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

    this.width = width;
    this.height = height;

    // Projections
    // LOD uses a left-handed projection with a negated Y axis because reasons
    this.perspectiveProjection.setPerspectiveLH(FOV, (float)width / height, 0.1f, 10000.0f); //TODO un-jank the world map so we can lower this ridiculousness
    this.perspectiveProjection.negateY();
    this.orthographicProjection.setOrtho2D(0.0f, width, height, 0.0f);

    // Order-independent translucency
    if(this.opaqueTexture != null) {
      this.opaqueTexture.delete();
    }

    if(this.depthTexture != null) {
      this.depthTexture.delete();
    }

    if(this.accumTexture != null) {
      this.accumTexture.delete();
    }

    if(this.revealTexture != null) {
      this.revealTexture.delete();
    }

    this.opaqueTexture = Texture.create(builder -> {
      builder.size(width, height);
      builder.internalFormat(GL_RGBA16F);
      builder.dataFormat(GL_RGBA);
      builder.dataType(GL_HALF_FLOAT);
      builder.magFilter(GL_LINEAR);
      builder.minFilter(GL_LINEAR);
    });

    this.depthTexture = Texture.create(builder -> {
      builder.size(width, height);
      builder.internalFormat(GL_DEPTH_COMPONENT);
      builder.dataFormat(GL_DEPTH_COMPONENT);
      builder.dataType(GL_FLOAT);
    });

    this.opaqueFrameBuffer = FrameBuffer.create(builder -> {
      builder.attachment(this.opaqueTexture, GL_COLOR_ATTACHMENT0);
      builder.attachment(this.depthTexture, GL_DEPTH_ATTACHMENT);
    });

    this.accumTexture = Texture.create(builder -> {
      builder.size(width, height);
      builder.internalFormat(GL_RGBA16F);
      builder.dataFormat(GL_RGBA);
      builder.dataType(GL_HALF_FLOAT);
      builder.magFilter(GL_LINEAR);
      builder.minFilter(GL_LINEAR);
    });

    this.revealTexture = Texture.create(builder -> {
      builder.size(width, height);
      builder.internalFormat(GL_R8);
      builder.dataFormat(GL_RED);
      builder.dataType(GL_FLOAT);
    });

    this.transparentFrameBuffer = FrameBuffer.create(builder -> {
      builder.attachment(this.accumTexture, GL_COLOR_ATTACHMENT0);
      builder.attachment(this.revealTexture, GL_COLOR_ATTACHMENT1);
      builder.attachment(this.depthTexture, GL_DEPTH_ATTACHMENT);
      builder.drawBuffer(GL_COLOR_ATTACHMENT0);
      builder.drawBuffer(GL_COLOR_ATTACHMENT1);
    });
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

      this.camera3d.look(-this.yaw, -this.pitch);
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

  public static class QueuedModel {
    private Obj obj;
    private final Matrix4f transforms = new Matrix4f();
    private final Vector3f colour = new Vector3f();

    private final Matrix4f lightDirection = new Matrix4f();
    private final Matrix4f lightColour = new Matrix4f();
    private final Vector4f backgroundColour = new Vector4f();

    public QueuedModel colour(final Vector3f colour) {
      this.colour.set(colour);
      return this;
    }

    public QueuedModel colour(final float r, final float g, final float b) {
      this.colour.set(r, g, b);
      return this;
    }

    public QueuedModel monochrome(final float shade) {
      this.colour.set(shade);
      return this;
    }

    public QueuedModel lightDirection(final Matrix3f lightDirection) {
      this.lightDirection.set(lightDirection);
      return this;
    }

    public QueuedModel lightColour(final Matrix3f lightColour) {
      this.lightColour.set(lightColour);
      return this;
    }

    public QueuedModel backgroundColour(final Vector3f backgroundColour) {
      this.backgroundColour.set(backgroundColour, 0.0f);
      return this;
    }
  }

  private static class QueuePool {
    private final List<QueuedModel> queue = new ArrayList<>();
    private int index;

    public QueuedModel get(final int index) {
      return this.queue.get(index);
    }

    public int size() {
      return this.queue.size();
    }

    public QueuedModel acquire() {
      if(this.index >= this.queue.size()) {
        final QueuedModel entry = new QueuedModel();
        this.queue.add(entry);
        this.index++;
        return entry;
      }

      return this.queue.get(this.index++);
    }

    public void reset() {
      this.index = 0;
    }
  }
}
