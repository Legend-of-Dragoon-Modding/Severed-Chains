package legend.core;

import legend.core.gpu.Rect4i;
import legend.core.gte.MV;
import legend.core.opengl.BasicCamera;
import legend.core.opengl.Camera;
import legend.core.opengl.FrameBuffer;
import legend.core.opengl.LegacyTextBuilder;
import legend.core.opengl.LineBuilder;
import legend.core.opengl.Mesh;
import legend.core.opengl.Obj;
import legend.core.opengl.QuadBuilder;
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
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

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
import static org.lwjgl.glfw.GLFW.GLFW_MOD_SHIFT;
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
import static org.lwjgl.opengl.GL11C.GL_LINE_SMOOTH;
import static org.lwjgl.opengl.GL11C.GL_ONE;
import static org.lwjgl.opengl.GL11C.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11C.GL_ONE_MINUS_SRC_COLOR;
import static org.lwjgl.opengl.GL11C.GL_RED;
import static org.lwjgl.opengl.GL11C.GL_RGBA;
import static org.lwjgl.opengl.GL11C.GL_SCISSOR_TEST;
import static org.lwjgl.opengl.GL11C.GL_SRC_ALPHA;
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
import static org.lwjgl.opengl.GL11C.glLineWidth;
import static org.lwjgl.opengl.GL11C.glPolygonMode;
import static org.lwjgl.opengl.GL11C.glScissor;
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

  public static int legacyMode;
  public boolean usePs1Gpu = true;

  private Camera camera2d;
  private Camera camera3d;
  private Window window;
  private Shader.UniformBuffer transformsUniform;
  private Shader.UniformBuffer transforms2Uniform;
  private Shader.UniformBuffer lightUniform;
  private Shader.UniformBuffer projectionUniform;
  private final Matrix4f perspectiveProjection = new Matrix4f();
  private final Matrix4f orthographicProjection = new Matrix4f();
  private final Matrix4f transforms = new Matrix4f();
  private final FloatBuffer transformsBuffer = BufferUtils.createFloatBuffer(4 * 4 * 2);
  private final FloatBuffer transforms2Buffer = BufferUtils.createFloatBuffer(4 * 4 + 2);
  private final FloatBuffer lightBuffer = BufferUtils.createFloatBuffer(4 * 4 * 2 + 4);
  private final FloatBuffer projectionBuffer = BufferUtils.createFloatBuffer(3);

  // Order-independent translucency
  private Shader tmdShader;
  private Shader tmdShaderTransparent;
  private Shader.UniformVec3 tmdShaderColour;
  private Shader.UniformVec2 tmdShaderUvOffset;
  private Shader.UniformVec2 tmdShaderClutOverride;
  private Shader.UniformVec2 tmdShaderTpageOverride;
  private Shader.UniformVec3 tmdShaderTransparentColour;
  private Shader.UniformVec2 tmdShaderTransparentUvOffset;
  private Shader.UniformVec2 tmdShaderTransparentClutOverride;
  private Shader.UniformVec2 tmdShaderTransparentTpageOverride;
  /**
   * <ul>
   *   <li>0: regular rendering, anything rendered will pass through the shader</li>
   *   <li>1: discard translucent pixels, used for rendering translucent primitives that have translucency disabled in their textures</li>
   *   <li>2: discard non-translucent pixels, used to render B+F and B-F primitives since they don't need to go through the OIT shader</li>
   * </ul>
   */
  private Shader.UniformFloat tmdShaderDiscardTranslucency;
  private FrameBuffer opaqueFrameBuffer;
  private FrameBuffer transparentFrameBuffer;
  private Texture opaqueTexture;
  private Texture depthTexture;
  private Texture accumTexture;
  private Texture revealTexture;
  private final float[] clear0 = {0.0f, 0.0f, 0.0f, 0.0f};
  private final float[] clear1 = {1.0f, 1.0f, 1.0f, 1.0f};

  // Text
  public Obj chars;
  // Plain quads
  public final Map<Translucency, Obj> plainQuads = new EnumMap<>(Translucency.class);
  // Simple quads
  public Obj centredQuadBPlusF;
  public Obj centredQuadBMinusF;
  // Line box (reticles)
  public Obj lineBox;
  public Obj lineBoxBPlusF;

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
  private final QueuePool orthoUnderlayPool = new QueuePool();
  private final QueuePool orthoOverlayPool = new QueuePool();
  private final Vector3f tempColour = new Vector3f();

  private float projectionWidth;
  private float projectionHeight;
  private float projectionDepth;
  private float aspectRatio;
  private float fieldOfView;
  private float halfWidthInv;
  private float halfHeightInv;

  public void setProjectionSize(final float width, final float height) {
    this.projectionWidth = width;
    this.projectionHeight = height;
    this.halfWidthInv = 1.0f / (width / 2.0f);
    this.halfHeightInv = 1.0f / (height / 2.0f);
    this.updateFieldOfView();
  }

  public void setProjectionDepth(final float depth) {
    this.projectionDepth = depth;
    this.updateFieldOfView();
  }

  private void updateFieldOfView() {
    this.aspectRatio = 320.0f / this.projectionHeight;
    final float halfWidth = this.projectionWidth / 2.0f;
    this.fieldOfView = (float)(Math.atan(halfWidth / this.projectionDepth) * 2.0f / this.aspectRatio);
    this.updateProjections();
  }

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

  public void clearDepth() {
    glClear(GL_DEPTH_BUFFER_BIT);
  }

  public Runnable setRenderCallback(final Runnable renderCallback) {
    final Runnable oldCallback = this.renderCallback;
    this.renderCallback = renderCallback;
    return oldCallback;
  }

  public void delete() {
    ShaderManager.delete();
    Obj.setShouldLog(false);
    Obj.clearObjList(true);
  }

  public void init() {
    this.camera2d = new BasicCamera(0.0f, 0.0f);
    this.camera3d = new QuaternionCamera(0.0f, 0.0f, 0.0f);
    this.window = new Window("Legend of Dragoon", Config.windowWidth(), Config.windowHeight());
    this.window.setFpsLimit(60);

    glEnable(GL_LINE_SMOOTH);

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
      this.tmdShader.bindUniformBlock("projectionInfo", Shader.UniformBuffer.PROJECTION_INFO);

      this.tmdShader.use();
      this.tmdShader.new UniformInt("tex24").set(0);
      this.tmdShader.new UniformInt("tex15").set(1);
      this.tmdShaderColour = this.tmdShader.new UniformVec3("recolour");
      this.tmdShaderClutOverride = this.tmdShader.new UniformVec2("clutOverride");
      this.tmdShaderTpageOverride = this.tmdShader.new UniformVec2("tpageOverride");
      this.tmdShaderUvOffset = this.tmdShader.new UniformVec2("uvOffset");
      this.tmdShaderDiscardTranslucency = this.tmdShader.new UniformFloat("discardTranslucency");
      this.tmdShaderDiscardTranslucency.set(1.0f);

      ShaderManager.addShader("tmd", this.tmdShader);
    } catch(final IOException e) {
      throw new RuntimeException("Failed to load TMD shader", e);
    }

    try {
      this.tmdShaderTransparent = new Shader(Paths.get("gfx/shaders/tmd.vsh"), Paths.get("gfx/shaders/tmd-transparent.fsh"));
      this.tmdShaderTransparent.bindUniformBlock("transforms", Shader.UniformBuffer.TRANSFORM);
      this.tmdShaderTransparent.bindUniformBlock("transforms2", Shader.UniformBuffer.TRANSFORM2);
      this.tmdShaderTransparent.bindUniformBlock("lighting", Shader.UniformBuffer.LIGHTING);
      this.tmdShaderTransparent.bindUniformBlock("projectionInfo", Shader.UniformBuffer.PROJECTION_INFO);

      this.tmdShaderTransparent.use();
      this.tmdShaderTransparent.new UniformInt("tex24").set(0);
      this.tmdShaderTransparent.new UniformInt("tex15").set(1);
      this.tmdShaderTransparentColour = this.tmdShaderTransparent.new UniformVec3("recolour");
      this.tmdShaderTransparentClutOverride = this.tmdShaderTransparent.new UniformVec2("clutOverride");
      this.tmdShaderTransparentTpageOverride = this.tmdShaderTransparent.new UniformVec2("tpageOverride");
      this.tmdShaderTransparentUvOffset = this.tmdShaderTransparent.new UniformVec2("uvOffset");

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
    this.projectionUniform = ShaderManager.addUniformBuffer("projectionInfo", new Shader.UniformBuffer((long)this.projectionBuffer.capacity() * Float.BYTES, Shader.UniformBuffer.PROJECTION_INFO));

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

    // Build text quads
    this.chars = new LegacyTextBuilder("Text Characters").build();
    this.chars.persistent = true;

    // Build fullscreen fade quads
    for(final Translucency translucency : Translucency.FOR_RENDERING) {
      final Obj obj = new QuadBuilder("Plain Quad " + translucency)
        .translucency(translucency)
        .size(1.0f, 1.0f)
        .build();
      obj.persistent = true;

      this.plainQuads.put(translucency, obj);
    }

    this.centredQuadBPlusF = new QuadBuilder("Centred Quad B+F")
      .translucency(Translucency.B_PLUS_F)
      .monochrome(1.0f)
      .pos(-1.0f, -1.0f, 0.0f)
      .size(1.0f, 1.0f)
      .build();
    this.centredQuadBPlusF.persistent = true;

    this.centredQuadBMinusF = new QuadBuilder("Centred Quad B-F")
      .translucency(Translucency.B_MINUS_F)
      .monochrome(1.0f)
      .pos(-1.0f, -1.0f, 0.0f)
      .size(1.0f, 1.0f)
      .build();
    this.centredQuadBMinusF.persistent = true;

    this.lineBox = new LineBuilder("Line Box")
      .pos(-1.0f, -1.0f, 0.0f)
      .pos( 1.0f, -1.0f, 0.0f)
      .pos( 1.0f,  1.0f, 0.0f)
      .pos(-1.0f,  1.0f, 0.0f)
      .closed()
      .build();
    this.lineBox.persistent = true;

    this.lineBoxBPlusF = new LineBuilder("Line Box (B+F)")
      .translucency(Translucency.B_PLUS_F)
      .pos(-1.0f, -1.0f, 0.0f)
      .pos( 1.0f, -1.0f, 0.0f)
      .pos( 1.0f,  1.0f, 0.0f)
      .pos(-1.0f,  1.0f, 0.0f)
      .closed()
      .build();
    this.lineBoxBPlusF.persistent = true;

    this.window.events.onDraw(() -> {
      this.pre();

      EVENTS.clearStaleRefs();

      // Restore model buffer to identity
      this.transforms.identity();
      this.transforms2Uniform.set(this.transforms);

      this.renderCallback.run();

      if(legacyMode == 0 && this.usePs1Gpu) {
        this.transparentFrameBuffer.bind();
        glClearBufferfv(GL_COLOR, 0, this.clear0);
        glClearBufferfv(GL_COLOR, 1, this.clear1);

        this.opaqueFrameBuffer.bind();
        this.clear();

        RENDERER.setProjectionMode(ProjectionMode._2D);
        this.render2dPool(this.orthoUnderlayPool);
        this.clearDepth();

        RENDERER.setProjectionMode(ProjectionMode._3D);
        this.renderPool(this.modelPool);

        RENDERER.setProjectionMode(ProjectionMode._2D);
        this.renderPool(this.orthoPool);

        // set render states
        RENDERER.setProjectionMode(ProjectionMode._3D);
        glDepthFunc(GL_ALWAYS);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

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
        this.render2dPool(this.orthoOverlayPool);
      } else {
        this.orthoUnderlayPool.reset();
        this.orthoOverlayPool.reset();
        this.orthoPool.reset();
        this.modelPool.reset();
      }

      this.fps = 1.0f / ((System.nanoTime() - this.lastFrame) / (1_000_000_000 / 30.0f)) * 30.0f;
      this.lastFrame = System.nanoTime();
      this.vsyncCount += 60.0d * Config.getGameSpeedMultiplier() / this.window.getFpsLimit();

      if(this.movingLeft) {
        this.camera3d.strafe(-MOVE_SPEED * 200);
      }

      if(this.movingRight) {
        this.camera3d.strafe(MOVE_SPEED * 200);
      }

      if(this.movingForward) {
        this.camera3d.move(-MOVE_SPEED * 200);
      }

      if(this.movingBackward) {
        this.camera3d.move(MOVE_SPEED * 200);
      }

      if(this.movingUp) {
        this.camera3d.jump(-MOVE_SPEED * 200);
      }

      if(this.movingDown) {
        this.camera3d.jump(MOVE_SPEED * 200);
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
    this.tmdShaderDiscardTranslucency.set(1.0f);
    GPU.useVramTexture();

    for(int i = 0; i < pool.size(); i++) {
      final QueuedModel entry = pool.get(i);
      this.tmdShaderColour.set(entry.colour);
      this.tmdShaderClutOverride.set(entry.clutOverride);
      this.tmdShaderTpageOverride.set(entry.tpageOverride);
      this.tmdShaderUvOffset.set(entry.uvOffset);
      boolean updated = false;

      if(entry.obj.shouldRender(null)) {
        updated = true;
        entry.updateTransforms();
        entry.render(null);
      }

      // First pass of translucency rendering - renders opaque pixels with translucency bit not set for translucent primitives
      for(int translucencyIndex = 0; translucencyIndex < Translucency.FOR_RENDERING.length; translucencyIndex++) {
        final Translucency translucency = Translucency.FOR_RENDERING[translucencyIndex];

        if(entry.obj.shouldRender(translucency)) {
          if(!updated) {
            updated = true;
            entry.updateTransforms();
          }

          entry.render(translucency);
        }
      }
    }

    glDepthMask(false);
    glDisable(GL_CULL_FACE);
    glEnable(GL_BLEND);

    // Render B+F (implicitly order-independent, because it's all addition)
    // Also renders B-F by negating the colour value and rendering as B+F
    this.tmdShaderDiscardTranslucency.set(2.0f);
    Translucency.B_PLUS_F.setGlState();

    for(int i = 0; i < pool.size(); i++) {
      final QueuedModel entry = pool.get(i);

      if(entry.obj.shouldRender(Translucency.B_PLUS_F)) {
        this.tmdShaderColour.set(entry.colour);
        this.tmdShaderClutOverride.set(entry.clutOverride);
        this.tmdShaderTpageOverride.set(entry.tpageOverride);
        this.tmdShaderUvOffset.set(entry.uvOffset);
        entry.updateTransforms();
        entry.render(Translucency.B_PLUS_F);
      }

      if(entry.obj.shouldRender(Translucency.B_MINUS_F)) {
        this.tmdShaderColour.set(entry.colour.mul(-1.0f, this.tempColour));
        this.tmdShaderClutOverride.set(entry.clutOverride);
        this.tmdShaderTpageOverride.set(entry.tpageOverride);
        this.tmdShaderUvOffset.set(entry.uvOffset);
        entry.updateTransforms();
        entry.render(Translucency.B_MINUS_F);
      }
    }

    // Order-independent translucency for (B+F)/2
    glBlendFunci(0, GL_ONE, GL_ONE);
    glBlendFunci(1, GL_ZERO, GL_ONE_MINUS_SRC_COLOR);
    glBlendEquation(GL_FUNC_ADD);

    this.transparentFrameBuffer.bind();
    this.tmdShaderTransparent.use();

    for(int i = 0; i < pool.size(); i++) {
      final QueuedModel entry = pool.get(i);

      if(entry.obj.shouldRender(Translucency.HALF_B_PLUS_HALF_F)) {
        entry.updateTransforms();
        this.tmdShaderTransparentColour.set(entry.colour);
        this.tmdShaderTransparentClutOverride.set(entry.clutOverride);
        this.tmdShaderTransparentTpageOverride.set(entry.tpageOverride);
        this.tmdShaderTransparentUvOffset.set(entry.uvOffset);
        entry.render(Translucency.HALF_B_PLUS_HALF_F);
      }
    }

    pool.reset();
  }

  private void render2dPool(final QueuePool pool) {
    this.tmdShader.use();
    GPU.useVramTexture();

    final float widthScale = this.window.getWidth() / this.projectionWidth;
    final float heightScale = this.window.getHeight() / this.projectionHeight;

    glDisable(GL_DEPTH_TEST);
    for(int i = 0; i < pool.size(); i++) {
      final QueuedModel entry = pool.get(i);
      entry.updateTransforms();
      this.tmdShaderColour.set(entry.colour);
      this.tmdShaderClutOverride.set(entry.clutOverride);
      this.tmdShaderTpageOverride.set(entry.tpageOverride);
      this.tmdShaderUvOffset.set(entry.uvOffset);

      if(entry.scissor.w != 0) {
        glEnable(GL_SCISSOR_TEST);
        glScissor((int)(entry.scissor.x * widthScale), this.window.getHeight() - (int)(entry.scissor.y * heightScale), (int)(entry.scissor.w * widthScale), (int)(entry.scissor.h * heightScale));
      }

      this.tmdShaderDiscardTranslucency.set(0.0f);
      glDisable(GL_BLEND);

      if(entry.obj.shouldRender(null)) {
        entry.render(null);
      }

      this.tmdShaderDiscardTranslucency.set(1.0f);

      for(int translucencyIndex = 0; translucencyIndex < Translucency.FOR_RENDERING.length; translucencyIndex++) {
        final Translucency translucency = Translucency.FOR_RENDERING[translucencyIndex];

        if(entry.obj.shouldRender(translucency)) {
          entry.render(translucency);
        }
      }

      this.tmdShaderDiscardTranslucency.set(2.0f);
      glEnable(GL_BLEND);

      for(int translucencyIndex = 0; translucencyIndex < Translucency.FOR_RENDERING.length; translucencyIndex++) {
        final Translucency translucency = Translucency.FOR_RENDERING[translucencyIndex];

        if(entry.obj.shouldRender(translucency)) {
          translucency.setGlState();
          entry.render(translucency);
        }
      }

      if(entry.scissor.w != 0) {
        glDisable(GL_SCISSOR_TEST);
      }
    }

    pool.reset();
  }

  public void setProjectionMode(final ProjectionMode projectionMode) {
    this.projectionBuffer.put(0, 0.0f);
    this.projectionBuffer.put(1, 1000000.0f);

    switch(projectionMode) {
      case _2D -> {
        glDisable(GL_CULL_FACE);
        this.setTransforms(this.camera2d, this.orthographicProjection);
        this.projectionBuffer.put(2, 1.0f);
      }

      case _3D -> {
        glEnable(GL_CULL_FACE);
        this.setTransforms(this.camera3d, this.perspectiveProjection);
        this.projectionBuffer.put(2, 0.0f);
      }
    }

    this.projectionUniform.set(this.projectionBuffer);
  }

  private void setTransforms(final Camera camera, final Matrix4f projection) {
    camera.get(this.transformsBuffer);
    projection.get(16, this.transformsBuffer);
    this.transformsUniform.set(this.transformsBuffer);
  }

  public QueuedModel queueModel(final Obj obj) {
    final QueuedModel entry = this.modelPool.acquire();
    entry.reset();
    entry.obj = obj;
    return entry;
  }

  public QueuedModel queueModel(final Obj obj, final MV mv) {
    final QueuedModel entry = this.modelPool.acquire();
    entry.reset();
    entry.obj = obj;
    entry.transforms.set(mv).setTranslation(mv.transfer);
    return entry;
  }

  public QueuedModel queueOrthoModel(final Obj obj) {
    if(obj == null) {
      throw new IllegalArgumentException("obj is null");
    }

    final QueuedModel entry = this.orthoPool.acquire();
    entry.reset();
    entry.obj = obj;
    return entry;
  }

  public QueuedModel queueOrthoModel(final Obj obj, final MV mv) {
    if(obj == null) {
      throw new IllegalArgumentException("obj is null");
    }

    final QueuedModel entry = this.orthoPool.acquire();
    entry.reset();
    entry.obj = obj;
    entry.transforms.set(mv).setTranslation(mv.transfer);
    return entry;
  }

  public QueuedModel queueOrthoOverlayModel(final Obj obj) {
    if(obj == null) {
      throw new IllegalArgumentException("obj is null");
    }

    final QueuedModel entry = this.orthoOverlayPool.acquire();
    entry.reset();
    entry.obj = obj;
    return entry;
  }

  public QueuedModel queueOrthoOverlayModel(final Obj obj, final MV mv) {
    if(obj == null) {
      throw new IllegalArgumentException("obj is null");
    }

    final QueuedModel entry = this.orthoOverlayPool.acquire();
    entry.reset();
    entry.obj = obj;
    entry.transforms.set(mv).setTranslation(mv.transfer);
    return entry;
  }

  public QueuedModel queueOrthoUnderlayModel(final Obj obj) {
    if(obj == null) {
      throw new IllegalArgumentException("obj is null");
    }

    final QueuedModel entry = this.orthoUnderlayPool.acquire();
    entry.reset();
    entry.obj = obj;
    return entry;
  }

  public QueuedModel queueOrthoUnderlayModel(final Obj obj, final MV mv) {
    if(obj == null) {
      throw new IllegalArgumentException("obj is null");
    }

    final QueuedModel entry = this.orthoUnderlayPool.acquire();
    entry.reset();
    entry.obj = obj;
    entry.transforms.set(mv).setTranslation(mv.transfer);
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

  private void updateProjections() {
    if(legacyMode != 0) {
      this.perspectiveProjection.setPerspectiveLH(MathHelper.PI / 4.0f, (float)this.width / this.height, 0.1f, 500.0f);
      this.orthographicProjection.setOrtho2D(0.0f, this.width, this.height, 0.0f);
      return;
    }

    // LOD uses a left-handed projection with a negated Y axis because reasons
    this.perspectiveProjection.setPerspectiveLH(this.fieldOfView, this.aspectRatio, 0.1f, 1000000.0f); //TODO un-jank the world map so we can lower this ridiculousness
    this.perspectiveProjection.negateY();
    this.orthographicProjection.setOrthoLH(0.0f, this.projectionWidth, this.projectionHeight, 0.0f, 0.1f, 1000000.0f);
  }

  private void onResize(final Window window, final int width, final int height) {
    if(width == 0 && height == 0) {
      return;
    }

    this.width = width;
    this.height = height;

    glLineWidth(Math.max(1, height / 480.0f));

    // Projections
    this.updateProjections();

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
    } else if(key == GLFW_KEY_TAB) {
      if((mods & GLFW_MOD_SHIFT) != 0) {
        legacyMode = Math.floorMod(legacyMode - 1, 3);
      } else {
        legacyMode = (legacyMode + 1) % 3;
      }

      this.updateProjections();

      switch(legacyMode) {
        case 0 -> System.out.println("Switched to OpenGL rendering");
        case 1 -> System.out.println("Switched to legacy rendering");
        case 2 -> System.out.println("Switched to VRAM rendering");
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

  public class QueuedModel {
    private Obj obj;
    private final Matrix4f transforms = new Matrix4f();
    private final Vector2f screenspaceOffset = new Vector2f();
    private final Vector3f colour = new Vector3f();
    private final Vector2f clutOverride = new Vector2f();
    private final Vector2f tpageOverride = new Vector2f();
    private final Vector2f uvOffset = new Vector2f();

    private final Matrix4f lightDirection = new Matrix4f();
    private final Matrix4f lightColour = new Matrix4f();
    private final Vector4f backgroundColour = new Vector4f();

    private final Rect4i scissor = new Rect4i();

    private int startVertex;
    private int vertexCount;

    public QueuedModel screenspaceOffset(final Vector2f offset) {
      this.screenspaceOffset.set(offset);
      return this;
    }

    public QueuedModel screenspaceOffset(final float x, final float y) {
      this.screenspaceOffset.set(x * RenderEngine.this.halfWidthInv, y * RenderEngine.this.halfHeightInv);
      return this;
    }

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

    public QueuedModel clutOverride(final float x, final float y) {
      this.clutOverride.set(x, y);
      return this;
    }

    public QueuedModel tpageOverride(final float x, final float y) {
      this.tpageOverride.set(x, y);
      return this;
    }

    public QueuedModel uvOffset(final float x, final float y) {
      this.uvOffset.set(x, y);
      return this;
    }

    public QueuedModel lightDirection(final Matrix3f lightDirection) {
      this.lightDirection.set(lightDirection).mul(this.transforms).setTranslation(0.0f, 0.0f, 0.0f);
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

    public QueuedModel scissor(final int x, final int y, final int w, final int h) {
      this.scissor.set(x, y, w, h);
      return this;
    }

    public QueuedModel vertices(final int startVertex, final int vertexCount) {
      this.startVertex = startVertex;
      this.vertexCount = vertexCount;
      return this;
    }

    private void reset() {
      this.transforms.identity();
      this.screenspaceOffset.zero();
      this.colour.set(1.0f, 1.0f, 1.0f);
      this.clutOverride.zero();
      this.tpageOverride.zero();
      this.uvOffset.zero();
      this.scissor.set(0, 0, 0, 0);
      this.vertexCount = 0;
    }

    private void updateTransforms() {
      this.transforms.get(RenderEngine.this.transforms2Buffer);
      this.screenspaceOffset.get(16, RenderEngine.this.transforms2Buffer);
      RenderEngine.this.transforms2Uniform.set(RenderEngine.this.transforms2Buffer);

      this.lightDirection.get(RenderEngine.this.lightBuffer);
      this.lightColour.get(16, RenderEngine.this.lightBuffer);
      this.backgroundColour.get(32, RenderEngine.this.lightBuffer);
      RenderEngine.this.lightUniform.set(RenderEngine.this.lightBuffer);
    }

    private void render(final Translucency translucency) {
      this.obj.render(translucency, this.startVertex, this.vertexCount);
    }
  }

  private class QueuePool {
    private final List<QueuedModel> queue = new ArrayList<>();
    private int index;

    public QueuedModel get(final int index) {
      return this.queue.get(index);
    }

    public int size() {
      return this.index;
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
