package legend.core;

import legend.core.gpu.Rect4i;
import legend.core.gte.MV;
import legend.core.opengl.BasicCamera;
import legend.core.opengl.Camera;
import legend.core.opengl.FontShaderOptions;
import legend.core.opengl.FrameBuffer;
import legend.core.opengl.LegacyTextBuilder;
import legend.core.opengl.LineBuilder;
import legend.core.opengl.Mesh;
import legend.core.opengl.Obj;
import legend.core.opengl.QuadBuilder;
import legend.core.opengl.QuaternionCamera;
import legend.core.opengl.Shader;
import legend.core.opengl.ShaderManager;
import legend.core.opengl.ShaderOptions;
import legend.core.opengl.ShaderType;
import legend.core.opengl.SimpleShaderOptions;
import legend.core.opengl.Texture;
import legend.core.opengl.TmdShaderOptions;
import legend.core.opengl.VoidShaderOptions;
import legend.core.opengl.Window;
import legend.core.opengl.fonts.Font;
import legend.core.opengl.fonts.FontManager;
import legend.game.combat.Battle;
import legend.game.modding.coremod.CoreMod;
import legend.game.types.Translucency;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;

import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import static legend.core.GameEngine.CONFIG;
import static legend.core.GameEngine.EVENTS;
import static legend.core.GameEngine.GPU;
import static legend.core.GameEngine.GTE;
import static legend.core.GameEngine.RENDERER;
import static legend.game.Scus94491BpeSegment_8004.currentEngineState_8004dd04;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_F10;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_F11;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_F9;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT_SHIFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_M;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_TAB;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;
import static org.lwjgl.glfw.GLFW.GLFW_MOD_SHIFT;
import static org.lwjgl.opengl.GL11C.GL_BLEND;
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
import static org.lwjgl.opengl.GL11C.GL_RGBA;
import static org.lwjgl.opengl.GL11C.GL_SCISSOR_TEST;
import static org.lwjgl.opengl.GL11C.GL_STENCIL_BUFFER_BIT;
import static org.lwjgl.opengl.GL11C.GL_TRIANGLES;
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
import static org.lwjgl.opengl.GL30C.GL_COLOR_ATTACHMENT0;
import static org.lwjgl.opengl.GL30C.GL_DEPTH_ATTACHMENT;
import static org.lwjgl.opengl.GL30C.GL_HALF_FLOAT;
import static org.lwjgl.opengl.GL30C.GL_RGBA16F;

public class RenderEngine {
  private static final Logger LOGGER = LogManager.getFormatterLogger(RenderEngine.class);

  public static int legacyMode;
  public boolean usePs1Gpu = true;

  public boolean allowWidescreen;
  public boolean allowHighQualityProjection;
  private float widescreenOrthoOffsetX;

  private Camera camera2d;
  private Camera camera3d;
  private Window window;
  private Shader.UniformBuffer transformsUniform;
  private Shader.UniformBuffer transforms2Uniform;
  private Shader.UniformBuffer lightUniform;
  private Shader.UniformBuffer projectionUniform;
  private final Matrix4f perspectiveProjection = new Matrix4f();
  private final Matrix4f orthographicProjection = new Matrix4f();
  private final FloatBuffer transformsBuffer = BufferUtils.createFloatBuffer(4 * 4 * 2);
  private final FloatBuffer transforms2Buffer = BufferUtils.createFloatBuffer((4 * 4 + 4) * 128);
  private final FloatBuffer lightBuffer = BufferUtils.createFloatBuffer((4 * 4 + 3 * 4 + 4) * 128); // 3*4 since glsl std140 means mat3's are basically 3 vec4s
  private final FloatBuffer projectionBuffer = BufferUtils.createFloatBuffer(4);

  public static final ShaderType<SimpleShaderOptions> SIMPLE_SHADER = new ShaderType<>(
    options -> loadShader("simple", "simple", options),
    shader -> {
      shader.bindUniformBlock("transforms", Shader.UniformBuffer.TRANSFORM);
      shader.bindUniformBlock("transforms2", Shader.UniformBuffer.TRANSFORM2);
      final Shader<SimpleShaderOptions>.UniformVec2 shiftUv = shader.new UniformVec2("shiftUv");
      final Shader<SimpleShaderOptions>.UniformVec4 recolour = shader.new UniformVec4("recolour");
      return () -> new SimpleShaderOptions(shiftUv, recolour);
    }
  );

  public static final ShaderType<FontShaderOptions> FONT_SHADER = new ShaderType<>(
    options -> loadShader("simple", "font", options),
    shader -> {
      shader.bindUniformBlock("transforms", Shader.UniformBuffer.TRANSFORM);
      shader.bindUniformBlock("transforms2", Shader.UniformBuffer.TRANSFORM2);
      final Shader<FontShaderOptions>.UniformVec3 colour = shader.new UniformVec3("colour");
      return () -> new FontShaderOptions(colour);
    }
  );

  public static final ShaderType<TmdShaderOptions> TMD_SHADER = new ShaderType<>(
    options -> loadShader("tmd", "tmd", options),
    shader -> {
      shader.use();
      shader.new UniformInt("tex24").set(0);
      shader.new UniformInt("tex15").set(1);
      shader.bindUniformBlock("transforms", Shader.UniformBuffer.TRANSFORM);
      shader.bindUniformBlock("transforms2", Shader.UniformBuffer.TRANSFORM2);
      shader.bindUniformBlock("lighting", Shader.UniformBuffer.LIGHTING);
      shader.bindUniformBlock("projectionInfo", Shader.UniformBuffer.PROJECTION_INFO);
      final Shader<TmdShaderOptions>.UniformFloat modelIndex = shader.new UniformFloat("modelIndex");
      final Shader<TmdShaderOptions>.UniformVec3 recolour = shader.new UniformVec3("recolour");
      final Shader<TmdShaderOptions>.UniformVec2 uvOffset = shader.new UniformVec2("uvOffset");
      final Shader<TmdShaderOptions>.UniformVec2 clutOverride = shader.new UniformVec2("clutOverride");
      final Shader<TmdShaderOptions>.UniformVec2 tpageOverride = shader.new UniformVec2("tpageOverride");
      final Shader<TmdShaderOptions>.UniformFloat translucency = shader.new UniformFloat("translucency");
      final Shader<TmdShaderOptions>.UniformFloat discardTranslucency = shader.new UniformFloat("discardTranslucency");
      final Shader<TmdShaderOptions>.UniformInt tmdTranslucency = shader.new UniformInt("tmdTranslucency");
      final Shader<TmdShaderOptions>.UniformInt ctmdFlags = shader.new UniformInt("ctmdFlags");
      final Shader<TmdShaderOptions>.UniformVec3 battleColour = shader.new UniformVec3("battleColour");
      return () -> new TmdShaderOptions(modelIndex, recolour, uvOffset, clutOverride, tpageOverride, translucency, discardTranslucency, tmdTranslucency, ctmdFlags, battleColour);
    }
  );

  public static final ShaderType<VoidShaderOptions> SCREEN_SHADER = new ShaderType<>(options -> loadShader("post", "screen", options), shader -> () -> VoidShaderOptions.INSTANCE);

  private Shader<TmdShaderOptions> tmdShader;
  private TmdShaderOptions tmdShaderOptions;
  private FrameBuffer opaqueFrameBuffer;
  private Texture opaqueTexture;
  private Texture depthTexture;

  // Text
  public Obj chars;
  // Plain quads
  public final Map<Translucency, Obj> plainQuads = new EnumMap<>(Translucency.class);
  public Obj opaqueQuad;
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

  private final QueuePool<QueuedModel<VoidShaderOptions>> modelPool = new QueuePool<>(QueuedModel::new);
  private final QueuePool<QueuedModel<VoidShaderOptions>> orthoPool = new QueuePool<>(QueuedModel::new);
  private final QueuePool<QueuedModel> shaderPool = new QueuePool<>(QueuedModel::new);
  private final Vector3f tempColour = new Vector3f();
  private boolean needsSorting;

  private float projectionWidth;
  private float projectionHeight;
  private float projectionDepth;
  private float aspectRatio;
  private float fieldOfView;

  private boolean togglePause;
  private boolean paused;
  private boolean frameAdvanceSingle;
  private boolean frameAdvance;

  public void setProjectionSize(final float width, final float height) {
    this.projectionWidth = width;
    this.projectionHeight = height;
    this.updateFieldOfView();
  }

  public Vector2f getProjectionSize() {
    return new Vector2f(this.projectionWidth, this.projectionHeight);
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

  public static <Options extends ShaderOptions<Options>> Shader<Options> loadShader(final String vsh, final String fsh, final Function<Shader<Options>, Supplier<Options>> options) {
    try {
      return new Shader<>(Paths.get("gfx/shaders/" + vsh + ".vsh"), Paths.get("gfx/shaders/" + fsh + ".fsh"), options);
    } catch(final IOException e) {
      throw new RuntimeException(e);
    }
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

    ShaderManager.addShader(SIMPLE_SHADER);
    ShaderManager.addShader(FONT_SHADER);
    final Shader<VoidShaderOptions> screenShader = ShaderManager.addShader(SCREEN_SHADER);
    this.tmdShader = ShaderManager.addShader(TMD_SHADER);
    this.tmdShaderOptions = this.tmdShader.makeOptions();

    try {
      FontManager.add("default", new Font(Paths.get("gfx/fonts/consolas.ttf")));
    } catch(final IOException e) {
      throw new RuntimeException("Failed to load font", e);
    }

    this.transformsUniform = new Shader.UniformBuffer((long)this.transformsBuffer.capacity() * Float.BYTES, Shader.UniformBuffer.TRANSFORM);
    this.transforms2Uniform = ShaderManager.addUniformBuffer("transforms2", new Shader.UniformBuffer((long)this.transforms2Buffer.capacity() * Float.BYTES, Shader.UniformBuffer.TRANSFORM2));
    this.lightUniform = ShaderManager.addUniformBuffer("lighting", new Shader.UniformBuffer((long)this.lightBuffer.capacity() * Float.BYTES, Shader.UniformBuffer.LIGHTING));
    this.projectionUniform = ShaderManager.addUniformBuffer("projectionInfo", new Shader.UniformBuffer((long)this.projectionBuffer.capacity() * Float.BYTES, Shader.UniformBuffer.PROJECTION_INFO));

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

    this.opaqueQuad = new QuadBuilder("Plain Quad Opaque")
      .monochrome(1.0f)
      .size(1.0f, 1.0f)
      .build();
    this.opaqueQuad.persistent = true;

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

      if(this.togglePause) {
        this.togglePause = false;
        this.paused = !this.paused;

        if(!this.paused) {
          this.frameAdvanceSingle = false;
          this.frameAdvance = false;
          this.modelPool.reset();
          this.orthoPool.reset();
        } else {
          this.renderCallback.run();
        }
      }

      if(this.frameAdvanceSingle || this.frameAdvance) {
        this.modelPool.reset();
        this.orthoPool.reset();
        this.shaderPool.reset();
        this.renderCallback.run();
        if(this.frameAdvanceSingle) {
          this.frameAdvanceSingle = false;
        }
      }

      if(!this.paused) {
        this.renderCallback.run();
      }

      if(legacyMode == 0 && this.usePs1Gpu) {
        if(this.needsSorting) {
          this.sortOrthoPool();
          this.needsSorting = false;
        }

        this.opaqueFrameBuffer.bind();
        this.clear();

        // Gross hack bro
        if(currentEngineState_8004dd04 instanceof final Battle battle && battle._800c6930 != null) {
          this.tmdShader.use();
          this.tmdShaderOptions.battleColour(battle._800c6930.colour_00);
        }

        RENDERER.setProjectionMode(ProjectionMode._3D);
        this.renderPool(this.modelPool, true);
        this.renderShaderPool();

        RENDERER.setProjectionMode(ProjectionMode._2D);
        this.renderPool(this.orthoPool, false);

        RENDERER.setProjectionMode(ProjectionMode._3D);
        this.renderPoolTranslucent(this.modelPool);

        RENDERER.setProjectionMode(ProjectionMode._2D);
        this.renderPoolTranslucent(this.orthoPool);

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

        // If we don't unbind the framebuffer textures, window resizing will crash since it has to resize the framebuffer
        Texture.unbind();

        // If we're paused, don't reset the pool so that we keep rendering the same scene over and over again
        if(!this.paused) {
          this.modelPool.reset();
          this.orthoPool.reset();
          this.shaderPool.reset();
        }
      } else if(!this.paused) {
        this.orthoPool.reset();
        this.modelPool.reset();
        this.shaderPool.reset();
      }

      this.fps = 1_000_000_000.0f / (System.nanoTime() - this.lastFrame);
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

  private void renderShaderPool() {
    glDisable(GL_CULL_FACE);
    glEnable(GL_BLEND);

    for(int i = 0; i < this.shaderPool.size(); i++) {
      final QueuedModel<?> entry = this.shaderPool.get(i);

      entry.useTexture();

      entry.shader.use();
      entry.shaderOptions.apply();
      entry.render(null);
    }
  }

  private void renderPool(final QueuePool<QueuedModel<VoidShaderOptions>> pool, final boolean backFaceCulling) {
    if(pool.isEmpty()) {
      return;
    }

    // Render if the depth is less than what is currently in the depth buffer
    glEnable(GL_DEPTH_TEST);
    glDepthFunc(GL_LESS);

    // Update the depth mask so nothing further away than this will render
    glDepthMask(true);

    glDisable(GL_BLEND);

    if(backFaceCulling) {
      glEnable(GL_CULL_FACE);
    } else {
      glDisable(GL_CULL_FACE);
    }

    boolean modelBackFaceCulling = true;

    this.tmdShader.use();
    this.tmdShaderOptions.discardMode(1);

    final boolean widescreen = this.allowWidescreen && CONFIG.getConfig(CoreMod.ALLOW_WIDESCREEN_CONFIG.get());
    final float w = this.window.getWidth() / this.projectionWidth;
    final float h = this.window.getHeight() / this.projectionHeight;

    for(int i = 0; i < pool.size(); i++) {
      final int modelIndex = i & 0x7f;

      // Load the next 128 model transforms into the buffers
      if(modelIndex == 0) {
        for(int storeIndex = 0; storeIndex < Math.min(128, pool.size() - i); storeIndex++) {
          pool.get(i + storeIndex).storeTransforms(storeIndex, this.transforms2Buffer, this.lightBuffer);
        }

        this.transforms2Uniform.set(this.transforms2Buffer);
        this.lightUniform.set(this.lightBuffer);
      }

      final QueuedModel<VoidShaderOptions> entry = pool.get(i);
      this.tmdShaderOptions.modelIndex(modelIndex);
      this.tmdShaderOptions.colour(entry.colour);
      this.tmdShaderOptions.clut(entry.clutOverride);
      this.tmdShaderOptions.tpage(entry.tpageOverride);
      this.tmdShaderOptions.uvOffset(entry.uvOffset);
      this.tmdShaderOptions.opaque();
      this.tmdShaderOptions.ctmdFlags(entry.ctmdFlags);
      this.tmdShaderOptions.tmdTranslucency(entry.tmdTranslucency);
      this.tmdShaderOptions.battleColour(entry.battleColour);
      boolean updated = false;

      if(entry.scissor.w != 0) {
        glEnable(GL_SCISSOR_TEST);

        if(widescreen) {
          glScissor((int)((entry.scissor.x + this.widescreenOrthoOffsetX) * h * (320.0f / this.projectionWidth)), this.window.getHeight() - (int)(entry.scissor.y * h), (int)(entry.scissor.w * h * (320.0f / this.projectionWidth)), (int)(entry.scissor.h * h));
        } else {
          glScissor((int)((entry.scissor.x + this.widescreenOrthoOffsetX) * w), this.window.getHeight() - (int)(entry.scissor.y * h), (int)(entry.scissor.w * w), (int)(entry.scissor.h * h));
        }
      }

      if(entry.shouldRender(null)) {
        if(backFaceCulling && modelBackFaceCulling != entry.obj.useBackfaceCulling()) {
          modelBackFaceCulling = entry.obj.useBackfaceCulling();

          if(modelBackFaceCulling) {
            glEnable(GL_CULL_FACE);
          } else {
            glDisable(GL_CULL_FACE);
          }
        }

        updated = true;
        entry.useTexture();
        entry.render(null);
      }

      // First pass of translucency rendering - renders opaque pixels with translucency bit not set for translucent primitives
      if(entry.hasTranslucency()) {
        for(int translucencyIndex = 0; translucencyIndex < Translucency.FOR_RENDERING.length; translucencyIndex++) {
          final Translucency translucency = Translucency.FOR_RENDERING[translucencyIndex];

          if(entry.shouldRender(translucency)) {
            this.tmdShaderOptions.translucency(translucency);

            if(backFaceCulling && modelBackFaceCulling) {
              modelBackFaceCulling = false;
              glDisable(GL_CULL_FACE);
            }

            if(!updated) {
              updated = true;
              entry.useTexture();
            }

            entry.render(translucency);
          }
        }
      }

      if(entry.scissor.w != 0) {
        glDisable(GL_SCISSOR_TEST);
      }
    }
  }

  private void renderPoolTranslucent(final QueuePool<QueuedModel<VoidShaderOptions>> pool) {
    if(pool.isEmpty()) {
      return;
    }

    // Do not update the depth mask so that we don't prevent things further away than this from rendering
    glDepthMask(false);

    glDisable(GL_CULL_FACE);
    glEnable(GL_BLEND);

    this.tmdShader.use();
    this.tmdShaderOptions.discardMode(2);
    this.tmdShaderOptions.translucency(Translucency.B_PLUS_F);
    Translucency.B_PLUS_F.setGlState();

    for(int i = 0; i < pool.size(); i++) {
      final int modelIndex = i & 0x7f;

      // Load the next 128 model transforms into the buffers
      if(modelIndex == 0) {
        for(int storeIndex = 0; storeIndex < Math.min(128, pool.size() - i); storeIndex++) {
          pool.get(i + storeIndex).storeTransforms(storeIndex, this.transforms2Buffer, this.lightBuffer);
        }

        this.transforms2Uniform.set(this.transforms2Buffer);
        this.lightUniform.set(this.lightBuffer);
      }

      final QueuedModel<VoidShaderOptions> entry = pool.get(i);

      if(entry.hasTranslucency()) {
        this.tmdShaderOptions.modelIndex(modelIndex);
        this.tmdShaderOptions.clut(entry.clutOverride);
        this.tmdShaderOptions.tpage(entry.tpageOverride);
        this.tmdShaderOptions.uvOffset(entry.uvOffset);
        this.tmdShaderOptions.ctmdFlags(entry.ctmdFlags);
        this.tmdShaderOptions.tmdTranslucency(entry.tmdTranslucency);
        this.tmdShaderOptions.battleColour(entry.battleColour);
        entry.useTexture();

        if(entry.shouldRender(Translucency.HALF_B_PLUS_HALF_F)) {
          Translucency.HALF_B_PLUS_HALF_F.setGlState();
          this.tmdShaderOptions.translucency(Translucency.HALF_B_PLUS_HALF_F);
          this.tmdShaderOptions.colour(entry.colour);
          entry.render(Translucency.HALF_B_PLUS_HALF_F);
          this.tmdShaderOptions.translucency(Translucency.B_PLUS_F);
          Translucency.B_PLUS_F.setGlState();
        }

        if(entry.shouldRender(Translucency.B_PLUS_F)) {
          this.tmdShaderOptions.colour(entry.colour);
          entry.render(Translucency.B_PLUS_F);
        }

        if(entry.shouldRender(Translucency.B_MINUS_F)) {
          this.tmdShaderOptions.colour(entry.colour.mul(-1.0f, this.tempColour));
          entry.render(Translucency.B_MINUS_F);
        }

        if(entry.shouldRender(Translucency.B_PLUS_QUARTER_F)) {
          this.tmdShaderOptions.colour(entry.colour.mul(0.25f, this.tempColour));
          entry.render(Translucency.B_PLUS_QUARTER_F);
        }
      }
    }
  }

  public void setProjectionMode(final ProjectionMode projectionMode) {
    final boolean highQualityProjection = this.allowHighQualityProjection && CONFIG.getConfig(CoreMod.HIGH_QUALITY_PROJECTION_CONFIG.get());

    // znear
    this.projectionBuffer.put(0, 0.0f);

    // zfar
    if(highQualityProjection) {
      this.projectionBuffer.put(1, 1000000.0f);
      this.projectionBuffer.put(2, 1.0f / 1000000.0f);
    } else {
      this.projectionBuffer.put(1, GTE.getProjectionPlaneDistance());
      this.projectionBuffer.put(2, 1.0f / GTE.getProjectionPlaneDistance());
    }

    switch(projectionMode) {
      case _2D -> {
        glDisable(GL_CULL_FACE);
        this.setTransforms(this.camera2d, this.orthographicProjection);
        this.projectionBuffer.put(3, 0.0f); // Projection mode: ortho
      }

      case _3D -> {
        glEnable(GL_CULL_FACE);
        this.setTransforms(this.camera3d, this.perspectiveProjection);

        if(highQualityProjection) {
          this.projectionBuffer.put(3, 2.0f); // projection mode: high quality perspective
        } else {
          this.projectionBuffer.put(3, 1.0f); // projection mode: PS1 perspective
        }
      }
    }

    this.projectionUniform.set(this.projectionBuffer);
  }

  private void setTransforms(final Camera camera, final Matrix4f projection) {
    camera.get(this.transformsBuffer);
    projection.get(16, this.transformsBuffer);
    this.transformsUniform.set(this.transformsBuffer);
  }

  private final Comparator<QueuedModel<?>> translucencySorter = Comparator.comparingDouble((QueuedModel<?> model) -> model.transforms.m32()).reversed();

  private void sortOrthoPool() {
    this.orthoPool.sort(this.translucencySorter);
  }

  public QueuedModel<VoidShaderOptions> queueModel(final Obj obj) {
    if(obj == null) {
      throw new IllegalArgumentException("obj is null");
    }

    if(obj.shouldRender(Translucency.HALF_B_PLUS_HALF_F)) {
      throw new IllegalArgumentException("3D models can only use order-independent translucency modes");
    }

    final QueuedModel<VoidShaderOptions> entry = this.modelPool.acquire();
    entry.reset();
    entry.obj = obj;
    return entry;
  }

  public QueuedModel<VoidShaderOptions> queueModel(final Obj obj, final MV mv) {
    if(obj == null) {
      throw new IllegalArgumentException("obj is null");
    }

    final QueuedModel<VoidShaderOptions> entry = this.modelPool.acquire();
    entry.reset();
    entry.obj = obj;
    entry.transforms.set(mv).setTranslation(mv.transfer);
    entry.lightTransforms.set(entry.transforms);
    return entry;
  }

  public QueuedModel<VoidShaderOptions> queueModel(final Obj obj, final MV mv, final MV lightMv) {
    if(obj == null) {
      throw new IllegalArgumentException("obj is null");
    }

    final QueuedModel<VoidShaderOptions> entry = this.modelPool.acquire();
    entry.reset();
    entry.obj = obj;
    entry.transforms.set(mv).setTranslation(mv.transfer);
    entry.lightTransforms.set(lightMv).setTranslation(lightMv.transfer);
    return entry;
  }

  public QueuedModel<VoidShaderOptions> queueModel(final Obj obj, final Matrix4f mv) {
    if(obj == null) {
      throw new IllegalArgumentException("obj is null");
    }

    final QueuedModel<VoidShaderOptions> entry = this.modelPool.acquire();
    entry.reset();
    entry.obj = obj;
    entry.transforms.set(mv);
    entry.lightTransforms.set(entry.transforms);
    return entry;
  }

  public QueuedModel<VoidShaderOptions> queueModel(final Obj obj, final Matrix4f mv, final MV lightMv) {
    if(obj == null) {
      throw new IllegalArgumentException("obj is null");
    }

    final QueuedModel<VoidShaderOptions> entry = this.modelPool.acquire();
    entry.reset();
    entry.obj = obj;
    entry.transforms.set(mv);
    entry.lightTransforms.set(lightMv).setTranslation(lightMv.transfer);
    return entry;
  }

  public QueuedModel<VoidShaderOptions> queueOrthoModel(final Obj obj) {
    if(obj == null) {
      throw new IllegalArgumentException("obj is null");
    }

    if(obj.shouldRender(Translucency.HALF_B_PLUS_HALF_F)) {
      this.needsSorting = true;
    }

    final QueuedModel<VoidShaderOptions> entry = this.orthoPool.acquire();
    entry.reset();
    entry.transforms.setTranslation(this.widescreenOrthoOffsetX, 0.0f, 0.0f);
    entry.obj = obj;
    return entry;
  }

  public QueuedModel<VoidShaderOptions> queueOrthoModel(final Obj obj, final MV mv) {
    if(obj == null) {
      throw new IllegalArgumentException("obj is null");
    }

    if(obj.shouldRender(Translucency.HALF_B_PLUS_HALF_F)) {
      this.needsSorting = true;
    }

    final QueuedModel<VoidShaderOptions> entry = this.orthoPool.acquire();
    entry.reset();
    entry.obj = obj;
    entry.transforms.set(mv).setTranslation(mv.transfer.x + this.widescreenOrthoOffsetX, mv.transfer.y, mv.transfer.z);
    entry.lightTransforms.set(entry.transforms);
    return entry;
  }

  public <Options extends ShaderOptions<Options>> QueuedModel<Options> queueModel(final Obj obj, final ShaderType<Options> shaderType) {
    if(obj == null) {
      throw new IllegalArgumentException("obj is null");
    }

    final QueuedModel<Options> entry = this.shaderPool.acquire();
    entry.reset();
    entry.obj = obj;
    entry.shader = ShaderManager.getShader(shaderType);
    entry.shaderOptions = entry.shader.makeOptions();
    return entry;
  }

  public <Options extends ShaderOptions<Options>> QueuedModel<Options> queueModel(final Obj obj, final MV mv, final ShaderType<Options> shaderType) {
    if(obj == null) {
      throw new IllegalArgumentException("obj is null");
    }

    final QueuedModel<Options> entry = this.shaderPool.acquire();
    entry.reset();
    entry.obj = obj;
    entry.shader = ShaderManager.getShader(shaderType);
    entry.shaderOptions = entry.shader.makeOptions();
    entry.transforms.set(mv).setTranslation(mv.transfer);
    entry.lightTransforms.set(entry.transforms);
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

  public void updateProjections() {
    if(legacyMode != 0) {
      this.perspectiveProjection.setPerspectiveLH(MathHelper.PI / 4.0f, (float)this.width / this.height, 0.1f, 500.0f);
      this.orthographicProjection.setOrtho2D(0.0f, this.width, this.height, 0.0f);
      return;
    }

    // LOD uses a left-handed projection with a negated Y axis because reasons.
    if(this.allowHighQualityProjection && (!CoreMod.HIGH_QUALITY_PROJECTION_CONFIG.isValid() || CONFIG.getConfig(CoreMod.HIGH_QUALITY_PROJECTION_CONFIG.get()))) {
      final float ratio;
      if(this.allowWidescreen && CONFIG.getConfig(CoreMod.ALLOW_WIDESCREEN_CONFIG.get())) {
        ratio = this.width / (float)this.height;
        final float w = this.projectionHeight * ratio;
        final float h = this.projectionHeight;
        this.orthographicProjection.setOrthoLH(0.0f, w * (this.projectionWidth / 320.0f), h, 0.0f, 0.0f, 1000000.0f);
        this.widescreenOrthoOffsetX = (w - 320.0f) / 2.0f;
      } else {
        ratio = this.aspectRatio;
        this.orthographicProjection.setOrthoLH(0.0f, this.projectionWidth, this.projectionHeight, 0.0f, 0.0f, 1000000.0f);
        this.widescreenOrthoOffsetX = 0.0f;
      }

      this.perspectiveProjection.setPerspectiveLH(this.fieldOfView, ratio, 0.1f, 1000000.0f);
      this.perspectiveProjection.negateY();
    } else {
      // Our perspective projection is actually a centred orthographic projection. We are doing a
      // projection plane division in the vertex shader to emulate perspective division on the GTE.
      if(this.allowWidescreen && CONFIG.getConfig(CoreMod.ALLOW_WIDESCREEN_CONFIG.get())) {
        final float ratio = this.width / (float)this.height;
        final float w = this.projectionHeight * ratio;
        final float h = this.projectionHeight;
        this.perspectiveProjection.setOrthoLH(-w / 2.0f, w / 2.0f, h / 2.0f, -h / 2.0f, 0.0f, 1000000.0f);
        this.orthographicProjection.setOrthoLH(0.0f, w * (this.projectionWidth / 320.0f), h, 0.0f, 0.0f, 1000000.0f);
        this.widescreenOrthoOffsetX = (w - 320.0f) / 2.0f;
      } else {
        this.perspectiveProjection.setOrthoLH(-this.projectionWidth / 2.0f, this.projectionWidth / 2.0f, this.projectionHeight / 2.0f, -this.projectionHeight / 2.0f, 0.0f, 1000000.0f);
        this.orthographicProjection.setOrthoLH(0.0f, this.projectionWidth, this.projectionHeight, 0.0f, 0.0f, 1000000.0f);
        this.widescreenOrthoOffsetX = 0.0f;
      }
    }
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
    } else if(key == GLFW_KEY_F11) {
      this.togglePause = !this.togglePause;
    } else if(key == GLFW_KEY_F9) {
      if(this.paused) {
        this.frameAdvanceSingle = true;
      }
    } else if(key == GLFW_KEY_F10) {
      if(this.paused) {
        this.frameAdvance = true;
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
    } else if (key == GLFW_KEY_F10) {
      this.frameAdvance = false;
    }
  }

  public class QueuedModel<Options extends ShaderOptions<Options>> {
    private Obj obj;
    private final Matrix4f transforms = new Matrix4f();
    private final Matrix4f lightTransforms = new Matrix4f();
    private final Vector3f screenspaceOffset = new Vector3f();
    private final Vector3f colour = new Vector3f();
    private final Vector2f clutOverride = new Vector2f();
    private final Vector2f tpageOverride = new Vector2f();
    private final Vector2f uvOffset = new Vector2f();

    private final Matrix4f lightDirection = new Matrix4f();
    private final Matrix3f lightColour = new Matrix3f();
    private final Vector4f backgroundColour = new Vector4f();
    private boolean lightUsed;

    private final Rect4i scissor = new Rect4i();

    private Shader<Options> shader;
    private Options shaderOptions;

    private int startVertex;
    private int vertexCount;

    private final Texture[] textures = new Texture[32];
    private boolean texturesUsed;

    private Translucency translucency;
    private boolean hasTranslucency;

    private int tmdTranslucency;
    private int ctmdFlags;
    private final Vector3f battleColour = new Vector3f();

    public Options options() {
      return this.shaderOptions;
    }

    public QueuedModel<Options> screenspaceOffset(final Vector2f offset) {
      this.screenspaceOffset.x = offset.x;
      this.screenspaceOffset.y = offset.y;
      return this;
    }

    public QueuedModel<Options> screenspaceOffset(final float x, final float y) {
      this.screenspaceOffset.x = x;
      this.screenspaceOffset.y = y;
      return this;
    }

    public QueuedModel<Options> depthOffset(final float z) {
      this.screenspaceOffset.z = z;
      return this;
    }

    public QueuedModel<Options> colour(final Vector3f colour) {
      this.colour.set(colour);
      return this;
    }

    public QueuedModel<Options> colour(final float r, final float g, final float b) {
      this.colour.set(r, g, b);
      return this;
    }

    public QueuedModel<Options> monochrome(final float shade) {
      this.colour.set(shade);
      return this;
    }

    public QueuedModel<Options> clutOverride(final float x, final float y) {
      this.clutOverride.set(x, y);
      return this;
    }

    public QueuedModel<Options> tpageOverride(final float x, final float y) {
      this.tpageOverride.set(x, y);
      return this;
    }

    public QueuedModel<Options> uvOffset(final float x, final float y) {
      this.uvOffset.set(x, y);
      return this;
    }

    public QueuedModel<Options> lightDirection(final Matrix3f lightDirection) {
      this.lightDirection.set(lightDirection).mul(this.lightTransforms).setTranslation(0.0f, 0.0f, 0.0f);
      this.lightUsed = true;
      return this;
    }

    public QueuedModel<Options> lightColour(final Matrix3f lightColour) {
      this.lightColour.set(lightColour);
      this.lightUsed = true;
      return this;
    }

    public QueuedModel<Options> backgroundColour(final Vector3f backgroundColour) {
      this.backgroundColour.set(backgroundColour, 0.0f);
      this.lightUsed = true;
      return this;
    }

    public QueuedModel<Options> scissor(final int x, final int y, final int w, final int h) {
      this.scissor.set(x, y, w, h);
      return this;
    }

    public QueuedModel<Options> vertices(final int startVertex, final int vertexCount) {
      this.startVertex = startVertex;
      this.vertexCount = vertexCount;
      return this;
    }

    public QueuedModel<Options> texture(final Texture texture, final int textureUnit) {
      this.textures[textureUnit] = texture;
      this.texturesUsed = true;
      return this;
    }

    public QueuedModel<Options> texture(final Texture texture) {
      return this.texture(texture, 0);
    }

    public QueuedModel<Options> translucency(final Translucency translucency) {
      this.translucency = translucency;
      this.hasTranslucency = true;
      return this;
    }

    public QueuedModel<Options> ctmdFlags(final int ctmdFlags) {
      this.ctmdFlags = ctmdFlags;
      return this;
    }

    public QueuedModel<Options> tmdTranslucency(final int tmdTranslucency) {
      this.tmdTranslucency = tmdTranslucency;
      return this;
    }

    public QueuedModel<Options> battleColour(final Vector3f colour) {
      this.battleColour.set(colour);
      return this;
    }

    private void reset() {
      this.shader = null;
      this.shaderOptions = null;
      this.transforms.identity();
      this.lightTransforms.identity();
      this.screenspaceOffset.zero();
      this.colour.set(1.0f, 1.0f, 1.0f);
      this.clutOverride.zero();
      this.tpageOverride.zero();
      this.uvOffset.zero();
      this.scissor.set(0, 0, 0, 0);
      this.vertexCount = 0;
      Arrays.fill(this.textures, null);
      this.hasTranslucency = false;
      this.texturesUsed = false;
      this.lightUsed = false;
      this.tmdTranslucency = 0;
      this.ctmdFlags = 0;
      this.battleColour.zero();
    }

    private void useTexture() {
      if(this.texturesUsed) {
        for(int i = 0; i < this.textures.length; i++) {
          if(this.textures[i] != null) {
            this.textures[i].use(i);
          }
        }
      } else {
        GPU.useVramTexture();
      }
    }

    public boolean hasTranslucency() {
      return this.hasTranslucency || (this.ctmdFlags & 0x2) != 0 || this.obj.hasTranslucency();
    }

    public boolean shouldRender(@Nullable final Translucency translucency) {
      return this.hasTranslucency && this.translucency == translucency || (this.ctmdFlags & 0x2) != 0 && translucency != null && this.tmdTranslucency == translucency.ordinal() || this.obj.shouldRender(translucency);
    }

    private void storeTransforms(final int modelIndex, final FloatBuffer transforms2Buffer, final FloatBuffer lightingBuffer) {
      this.transforms.get(modelIndex * 20, transforms2Buffer);
      this.screenspaceOffset.get(modelIndex * 20 + 16, transforms2Buffer);

      if(this.lightUsed) {
        this.lightDirection.get(modelIndex * 32, lightingBuffer);
        this.lightColour.get(modelIndex * 32 + 16, lightingBuffer);
        this.backgroundColour.get(modelIndex * 32 + 28, lightingBuffer);
      }
    }

    private void render(final Translucency translucency) {
      if(this.hasTranslucency || (this.ctmdFlags & 0x2) != 0) {
        // Translucency override
        this.obj.render(this.startVertex, this.vertexCount);
      } else {
        this.obj.render(translucency, this.startVertex, this.vertexCount);
      }
    }

    @Override
    public String toString() {
      return this.obj.toString();
    }
  }

  private static class QueuePool<T> {
    private final List<T> queue = new ArrayList<>();
    private final Supplier<T> constructor;
    private int index;

    private QueuePool(final Supplier<T> constructor) {
      this.constructor = constructor;
    }

    public T get(final int index) {
      return this.queue.get(index);
    }

    public int size() {
      return this.index;
    }

    public boolean isEmpty() {
      return this.size() == 0;
    }

    public T acquire() {
      if(this.index >= this.queue.size()) {
        final T entry = this.constructor.get();
        this.queue.add(entry);
        this.index++;
        return entry;
      }

      return this.queue.get(this.index++);
    }

    public void reset() {
      this.index = 0;
    }

    public void sort(final Comparator<? super T> comparator) {
      this.queue.subList(0, this.size()).sort(comparator);
    }
  }
}
