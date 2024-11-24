package legend.core;

import legend.core.gpu.Bpp;
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
import legend.core.opengl.RenderState;
import legend.core.opengl.Resolution;
import legend.core.opengl.ScissorStack;
import legend.core.opengl.Shader;
import legend.core.opengl.ShaderManager;
import legend.core.opengl.ShaderOptions;
import legend.core.opengl.ShaderOptionsBattleTmd;
import legend.core.opengl.ShaderOptionsStandard;
import legend.core.opengl.ShaderOptionsTmd;
import legend.core.opengl.ShaderType;
import legend.core.opengl.SimpleShaderOptions;
import legend.core.opengl.Texture;
import legend.core.opengl.VoidShaderOptions;
import legend.core.opengl.Window;
import legend.core.opengl.fonts.Font;
import legend.core.opengl.fonts.FontManager;
import legend.game.combat.Battle;
import legend.game.modding.coremod.CoreMod;
import legend.game.types.Translucency;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import static legend.core.GameEngine.CONFIG;
import static legend.core.GameEngine.EVENTS;
import static legend.core.GameEngine.GTE;
import static legend.core.MathHelper.PI;
import static legend.core.MathHelper.clamp;
import static legend.game.Scus94491BpeSegment_8004.currentEngineState_8004dd04;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_EQUAL;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_F10;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_F11;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_F5;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_F9;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT_SHIFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_M;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_MINUS;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_TAB;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;
import static org.lwjgl.glfw.GLFW.GLFW_MOD_CONTROL;
import static org.lwjgl.glfw.GLFW.GLFW_MOD_SHIFT;
import static org.lwjgl.opengl.GL11C.GL_BLEND;
import static org.lwjgl.opengl.GL11C.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11C.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11C.GL_DEPTH_COMPONENT;
import static org.lwjgl.opengl.GL11C.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11C.GL_FILL;
import static org.lwjgl.opengl.GL11C.GL_FLOAT;
import static org.lwjgl.opengl.GL11C.GL_FRONT_AND_BACK;
import static org.lwjgl.opengl.GL11C.GL_LEQUAL;
import static org.lwjgl.opengl.GL11C.GL_LESS;
import static org.lwjgl.opengl.GL11C.GL_LINE;
import static org.lwjgl.opengl.GL11C.GL_LINEAR;
import static org.lwjgl.opengl.GL11C.GL_LINE_SMOOTH;
import static org.lwjgl.opengl.GL11C.GL_NEAREST;
import static org.lwjgl.opengl.GL11C.GL_RGBA;
import static org.lwjgl.opengl.GL11C.GL_STENCIL_BUFFER_BIT;
import static org.lwjgl.opengl.GL11C.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11C.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11C.glClear;
import static org.lwjgl.opengl.GL11C.glClearColor;
import static org.lwjgl.opengl.GL11C.glDepthFunc;
import static org.lwjgl.opengl.GL11C.glDepthMask;
import static org.lwjgl.opengl.GL11C.glDisable;
import static org.lwjgl.opengl.GL11C.glEnable;
import static org.lwjgl.opengl.GL11C.glLineWidth;
import static org.lwjgl.opengl.GL11C.glPolygonMode;
import static org.lwjgl.opengl.GL11C.glViewport;
import static org.lwjgl.opengl.GL30C.GL_COLOR_ATTACHMENT0;
import static org.lwjgl.opengl.GL30C.GL_DEPTH_ATTACHMENT;
import static org.lwjgl.opengl.GL31C.GL_RGBA16_SNORM;

public class RenderEngine {
  private static final Logger LOGGER = LogManager.getFormatterLogger(RenderEngine.class);

  public static int legacyMode;
  public boolean usePs1Gpu = true;

  private final List<RenderBatch> batches = new ArrayList<>();
  private final RenderBatch mainBatch;
  public final ScissorStack scissorStack;
  private final RenderState state;

  private Camera camera2d;
  private Camera camera3d;
  private Window window;
  private Shader.UniformBuffer transformsUniform;
  private Shader.UniformBuffer transforms2Uniform;
  private Shader.UniformBuffer lightUniform;
  private Shader.UniformBuffer projectionUniform;
  private Shader.UniformBuffer vdfUniform;
  private final FloatBuffer transformsBuffer = BufferUtils.createFloatBuffer(4 * 4 * 2);
  private final FloatBuffer transforms2Buffer = BufferUtils.createFloatBuffer((4 * 4 + 4) * 128);
  private final FloatBuffer lightBuffer = BufferUtils.createFloatBuffer((4 * 4 + 3 * 4 + 4) * 128); // 3*4 since glsl std140 means mat3's are basically 3 vec4s
  private final FloatBuffer projectionBuffer = BufferUtils.createFloatBuffer(4);
  private final FloatBuffer vdfBuffer = BufferUtils.createFloatBuffer(4 * 1024);

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

  public static final ShaderType<ShaderOptionsStandard> STANDARD_SHADER = new ShaderType<>(
    options -> loadShader("standard", "standard", options),
    shader -> {
      shader.use();
      shader.new UniformInt("tex24").set(0);
      shader.new UniformInt("tex15").set(1);
      shader.bindUniformBlock("transforms", Shader.UniformBuffer.TRANSFORM);
      shader.bindUniformBlock("transforms2", Shader.UniformBuffer.TRANSFORM2);
      shader.bindUniformBlock("projectionInfo", Shader.UniformBuffer.PROJECTION_INFO);
      final Shader<ShaderOptionsStandard>.UniformFloat modelIndex = shader.new UniformFloat("modelIndex");
      final Shader<ShaderOptionsStandard>.UniformVec3 recolour = shader.new UniformVec3("recolour");
      final Shader<ShaderOptionsStandard>.UniformVec2 uvOffset = shader.new UniformVec2("uvOffset");
      final Shader<ShaderOptionsStandard>.UniformVec2 clutOverride = shader.new UniformVec2("clutOverride");
      final Shader<ShaderOptionsStandard>.UniformVec2 tpageOverride = shader.new UniformVec2("tpageOverride");
      final Shader<ShaderOptionsStandard>.UniformFloat translucency = shader.new UniformFloat("translucency");
      final Shader<ShaderOptionsStandard>.UniformFloat discardTranslucency = shader.new UniformFloat("discardTranslucency");
      return () -> new ShaderOptionsStandard(modelIndex, recolour, uvOffset, clutOverride, tpageOverride, discardTranslucency, translucency);
    }
  );

  public static final ShaderType<ShaderOptionsTmd> TMD_SHADER = new ShaderType<>(
    options -> loadShader("tmd", "tmd", options),
    shader -> {
      shader.use();
      shader.new UniformInt("tex24").set(0);
      shader.new UniformInt("tex15").set(1);
      shader.bindUniformBlock("transforms", Shader.UniformBuffer.TRANSFORM);
      shader.bindUniformBlock("transforms2", Shader.UniformBuffer.TRANSFORM2);
      shader.bindUniformBlock("lighting", Shader.UniformBuffer.LIGHTING);
      shader.bindUniformBlock("projectionInfo", Shader.UniformBuffer.PROJECTION_INFO);
      final Shader<ShaderOptionsTmd>.UniformFloat modelIndex = shader.new UniformFloat("modelIndex");
      final Shader<ShaderOptionsTmd>.UniformVec3 recolour = shader.new UniformVec3("recolour");
      final Shader<ShaderOptionsTmd>.UniformVec2 uvOffset = shader.new UniformVec2("uvOffset");
      final Shader<ShaderOptionsTmd>.UniformVec2 clutOverride = shader.new UniformVec2("clutOverride");
      final Shader<ShaderOptionsTmd>.UniformVec2 tpageOverride = shader.new UniformVec2("tpageOverride");
      final Shader<ShaderOptionsTmd>.UniformFloat discardTranslucency = shader.new UniformFloat("discardTranslucency");
      final Shader<ShaderOptionsTmd>.UniformInt tmdTranslucency = shader.new UniformInt("tmdTranslucency");
      return () -> new ShaderOptionsTmd(modelIndex, recolour, uvOffset, clutOverride, tpageOverride, discardTranslucency, tmdTranslucency);
    }
  );

  public static final ShaderType<ShaderOptionsBattleTmd> BATTLE_TMD_SHADER = new ShaderType<>(
    options -> loadShader("battle_tmd", "battle_tmd", options),
    shader -> {
      shader.use();
      shader.new UniformInt("tex24").set(0);
      shader.new UniformInt("tex15").set(1);
      shader.bindUniformBlock("transforms", Shader.UniformBuffer.TRANSFORM);
      shader.bindUniformBlock("transforms2", Shader.UniformBuffer.TRANSFORM2);
      shader.bindUniformBlock("lighting", Shader.UniformBuffer.LIGHTING);
      shader.bindUniformBlock("projectionInfo", Shader.UniformBuffer.PROJECTION_INFO);
      shader.bindUniformBlock("vdf", Shader.UniformBuffer.VDF);
      final Shader<ShaderOptionsBattleTmd>.UniformFloat modelIndex = shader.new UniformFloat("modelIndex");
      final Shader<ShaderOptionsBattleTmd>.UniformVec3 recolour = shader.new UniformVec3("recolour");
      final Shader<ShaderOptionsBattleTmd>.UniformVec2 uvOffset = shader.new UniformVec2("uvOffset");
      final Shader<ShaderOptionsBattleTmd>.UniformVec2 clutOverride = shader.new UniformVec2("clutOverride");
      final Shader<ShaderOptionsBattleTmd>.UniformVec2 tpageOverride = shader.new UniformVec2("tpageOverride");
      final Shader<ShaderOptionsBattleTmd>.UniformFloat discardTranslucency = shader.new UniformFloat("discardTranslucency");
      final Shader<ShaderOptionsBattleTmd>.UniformInt tmdTranslucency = shader.new UniformInt("tmdTranslucency");
      final Shader<ShaderOptionsBattleTmd>.UniformInt ctmdFlags = shader.new UniformInt("ctmdFlags");
      final Shader<ShaderOptionsBattleTmd>.UniformVec3 battleColour = shader.new UniformVec3("battleColour");
      final Shader<ShaderOptionsBattleTmd>.UniformInt useVdf = shader.new UniformInt("useVdf");
      return () -> new ShaderOptionsBattleTmd(modelIndex, recolour, uvOffset, clutOverride, tpageOverride, discardTranslucency, tmdTranslucency, ctmdFlags, battleColour, useVdf);
    }
  );

  public static final ShaderType<VoidShaderOptions> SCREEN_SHADER = new ShaderType<>(options -> loadShader("post", "screen", options), shader -> () -> VoidShaderOptions.INSTANCE);

  private static final int RENDER_BUFFER_COUNT = 2;
  Shader<ShaderOptionsStandard> standardShader;
  ShaderOptionsStandard standardShaderOptions;
  Shader<ShaderOptionsTmd> tmdShader;
  ShaderOptionsTmd tmdShaderOptions;
  Shader<ShaderOptionsBattleTmd> battleTmdShader;
  ShaderOptionsBattleTmd battleTmdShaderOptions;
  private final FrameBuffer[] renderBuffers = new FrameBuffer[RENDER_BUFFER_COUNT];
  private final Texture[] renderTextures = new Texture[RENDER_BUFFER_COUNT];
  private Texture depthTexture;
  private int renderBufferIndex;

  // Text
  public Texture textTexture;
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
  // Render buffer
  public Obj renderBufferQuad;

  /** The actual width for rendering (taking into account resolution config) */
  private int renderWidth;
  /** The actual height for rendering (taking into account resolution config) */
  private int renderHeight;
  private float renderAspectRatio;

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

  private boolean togglePause;
  private boolean paused;
  private boolean frameAdvanceSingle;
  private boolean frameAdvance;
  private boolean reloadShaders;

  private int frameSkipIndex;

  public RenderEngine() {
    this.mainBatch = new RenderBatch(this, () -> this.vdfUniform, this.vdfBuffer, this.lightBuffer);
    this.scissorStack = new ScissorStack(this, this.mainBatch);
    this.state = new RenderState(this, this.scissorStack);
  }

  /**
   * Adds a new render batch. Batches will be rendered in the order that they are
   * added. Depth buffer is cleared between batches, so each batch will be rendered
   * on top of the previous one. Use the returned {@link RenderBatch} to queue
   * models into this batch. Render batches have their own projections, widescreen
   * config, etc. and will need to be configured separately if desired. They start
   * out with a copy of the main render batch's config when they are created.
   */
  public RenderBatch addBatch() {
    final RenderBatch batch = new RenderBatch(this, this.mainBatch, () -> this.vdfUniform, this.vdfBuffer, this.lightBuffer);
    this.batches.add(batch);
    return batch;
  }

  private void resetBatches() {
    this.mainBatch.reset();

    for(int i = 0; i < this.batches.size(); i++) {
      this.batches.get(i).reset();
    }
  }

  public float getNativeAspectRatio() {
    return this.mainBatch.aspectRatio;
  }

  /** NOTE: you must call {@link #updateProjections} yourself */
  public void setAllowWidescreen(final boolean allowWidescreen) {
    this.mainBatch.setAllowWidescreen(allowWidescreen);
  }

  public boolean getAllowWidescreen() {
    return this.mainBatch.allowWidescreen;
  }

  /** NOTE: you must call {@link #updateProjections} yourself */
  public void setAllowHighQualityProjection(final boolean allowHighQualityProjection) {
    this.mainBatch.setAllowHighQualityProjection(allowHighQualityProjection);
  }

  public float getWidthSquisher() {
    return this.mainBatch.widthSquisher;
  }

  public void setProjectionSize(final float width, final float height) {
    this.mainBatch.setProjectionSize(width, height);
  }

  public float getProjectionWidth() {
    return this.mainBatch.getProjectionWidth();
  }

  public float getProjectionHeight() {
    return this.mainBatch.getProjectionHeight();
  }

  public int getRenderWidth() {
    return this.renderWidth;
  }

  public int getRenderHeight() {
    return this.renderHeight;
  }

  public float getRenderAspectRatio() {
    return this.renderAspectRatio;
  }

  public void setProjectionDepth(final float depth) {
    this.mainBatch.setProjectionDepth(depth);
  }

  public void updateProjections() {
    this.mainBatch.updateProjections();
  }

  public boolean expandedSubmap() {
    return this.mainBatch.expandedSubmap;
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

  public Texture getLastFrame() {
    return this.renderTextures[Math.floorMod(this.renderBufferIndex - 1, RENDER_BUFFER_COUNT)];
  }

  public void init() {
    this.camera2d = new BasicCamera(0.0f, 0.0f);
    this.camera3d = new QuaternionCamera(0.0f, 0.0f, 0.0f);
    this.window = new Window("Severed Chains " + Version.VERSION, Config.windowWidth(), Config.windowHeight());
    this.window.setFpsLimit(60);

    glEnable(GL_LINE_SMOOTH);

    this.window.events.onResize(this::onResize);

    this.window.events.onMouseMove(this::onMouseMove);
    this.window.events.onKeyPress(this::onKeyPress);
    this.window.events.onKeyRelease(this::onKeyRelease);

    ShaderManager.addShader(SIMPLE_SHADER);
    ShaderManager.addShader(FONT_SHADER);
    final Shader<VoidShaderOptions> screenShader = ShaderManager.addShader(SCREEN_SHADER);
    this.standardShader = ShaderManager.addShader(STANDARD_SHADER);
    this.standardShaderOptions = this.standardShader.makeOptions();
    this.tmdShader = ShaderManager.addShader(TMD_SHADER);
    this.tmdShaderOptions = this.tmdShader.makeOptions();
    this.battleTmdShader = ShaderManager.addShader(BATTLE_TMD_SHADER);
    this.battleTmdShaderOptions = this.battleTmdShader.makeOptions();

    try {
      FontManager.add("default", new Font(Paths.get("gfx/fonts/consolas.ttf")));
    } catch(final IOException e) {
      throw new RuntimeException("Failed to load font", e);
    }

    this.transformsUniform = new Shader.UniformBuffer((long)this.transformsBuffer.capacity() * Float.BYTES, Shader.UniformBuffer.TRANSFORM);
    this.transforms2Uniform = ShaderManager.addUniformBuffer("transforms2", new Shader.UniformBuffer((long)this.transforms2Buffer.capacity() * Float.BYTES, Shader.UniformBuffer.TRANSFORM2));
    this.lightUniform = ShaderManager.addUniformBuffer("lighting", new Shader.UniformBuffer((long)this.lightBuffer.capacity() * Float.BYTES, Shader.UniformBuffer.LIGHTING));
    this.projectionUniform = ShaderManager.addUniformBuffer("projectionInfo", new Shader.UniformBuffer((long)this.projectionBuffer.capacity() * Float.BYTES, Shader.UniformBuffer.PROJECTION_INFO));
    this.vdfUniform = ShaderManager.addUniformBuffer("vdf", new Shader.UniformBuffer((long)this.vdfBuffer.capacity() * Float.BYTES, Shader.UniformBuffer.VDF));

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
    this.textTexture = Texture.png(Path.of("./gfx/fonts/ingame.png"));
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

    this.renderBufferQuad = new QuadBuilder("Render buffer")
      .bpp(Bpp.BITS_24)
      .size(1.0f, 1.0f)
      .uv(0.0f, 1.0f)
      .uvSize(1.0f, -1.0f)
      .build();
    this.renderBufferQuad.persistent = true;

    this.window.events.onDraw(() -> {
      this.pre();

      EVENTS.clearStaleRefs();

      if(this.togglePause) {
        this.togglePause = false;
        this.paused = !this.paused;

        if(!this.paused) {
          this.frameAdvanceSingle = false;
          this.frameAdvance = false;
          this.resetBatches();
        } else {
          this.renderCallback.run();
        }
      }

      if(this.frameAdvanceSingle || this.frameAdvance) {
        this.resetBatches();
        this.renderCallback.run();
        if(this.frameAdvanceSingle) {
          this.frameAdvanceSingle = false;
        }
      }

      if(!this.paused) {
        this.renderCallback.run();

        if(Config.getGameSpeedMultiplier() > 1) {
          for(int i = 0; i < this.batches.size(); i++) {
            this.batches.get(i).modelPool.ignoreQueues = true;
            this.batches.get(i).orthoPool.ignoreQueues = true;
          }

          this.mainBatch.modelPool.ignoreQueues = true;
          this.mainBatch.orthoPool.ignoreQueues = true;

          for(int i = 1; i < Config.getGameSpeedMultiplier(); i++) {
            this.renderCallback.run();
          }

          for(int i = 0; i < this.batches.size(); i++) {
            this.batches.get(i).modelPool.ignoreQueues = false;
            this.batches.get(i).orthoPool.ignoreQueues = false;
          }

          this.mainBatch.modelPool.ignoreQueues = false;
          this.mainBatch.orthoPool.ignoreQueues = false;
        }
      }

      if(legacyMode == 0 && this.usePs1Gpu) {
        // Gross hack bro
        if(currentEngineState_8004dd04 instanceof final Battle battle && battle._800c6930 != null) {
          this.battleTmdShader.use();
          this.battleTmdShaderOptions.battleColour(battle._800c6930.colour_00);
        }

        this.renderBuffers[this.renderBufferIndex].bind();
        this.clearColour();

        // Render batches
        for(int i = 0; i < this.batches.size(); i++) {
          final RenderBatch batch = this.batches.get(i);
          this.renderBatch(batch);
        }

        this.renderBatch(this.mainBatch);

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
        glViewport(0, 0, this.window.getWidth(), this.window.getHeight());
        this.renderTextures[this.renderBufferIndex].use();
        postQuad.draw();

        // If we don't unbind the framebuffer textures, window resizing will crash since it has to resize the framebuffer
        Texture.unbind();
      }

      // If we're paused, don't reset the pool so that we keep rendering the same scene over and over again
      if(!this.paused) {
        this.resetBatches();

        this.renderBufferIndex = (this.renderBufferIndex + 1) % RENDER_BUFFER_COUNT;

        // Delete stuff marked for deletion
        Obj.deleteObjects();
        Texture.deleteTextures();
      }

      this.scissorStack.reset();

      this.fps = 1_000_000_000.0f / (System.nanoTime() - this.lastFrame);
      this.lastFrame = System.nanoTime();
      this.vsyncCount += 60.0d * Config.getGameSpeedMultiplier() / this.window.getFpsLimit();

      if(this.reloadShaders) {
        this.reloadShaders = false;
        LOGGER.info("Reloading shaders");

        try {
          ShaderManager.reload();
        } catch(final IOException e) {
          LOGGER.error("Failed to reload shaders", e);
        }
      }

      this.handleMovement();
    });
  }

  private void renderBatch(final RenderBatch batch) {
    if(batch.needsSorting) {
      this.sortOrthoPool(batch.orthoPool);
      batch.needsSorting = false;
    }

    this.state.initBatch(batch);
    this.state.enableScissor();

    this.clearDepth();

    this.setProjectionMode(batch, ProjectionMode._3D);
    this.renderPool(batch.modelPool, true);

    this.setProjectionMode(batch, ProjectionMode._2D);
    this.renderPool(batch.orthoPool, false);

    this.setProjectionMode(batch, ProjectionMode._3D);
    this.renderPoolTranslucent(batch, batch.modelPool);

    this.setProjectionMode(batch, ProjectionMode._2D);
    this.renderPoolTranslucent(batch, batch.orthoPool);

    this.state.disableScissor();
  }

  private void renderPool(final QueuePool<QueuedModel<?, ?>> pool, final boolean backFaceCulling) {
    if(pool.isEmpty()) {
      return;
    }

    // Render if the depth is less than what is currently in the depth buffer
    glEnable(GL_DEPTH_TEST);
    glDepthFunc(GL_LESS);

    // Update the depth mask so nothing further away than this will render
    glDepthMask(true);

    glDisable(GL_BLEND);

    this.state.backfaceCulling(backFaceCulling);

    for(int i = 0; i < pool.size(); i++) {
      final int modelIndex = i & 0x7f;

      // Load the next 128 model transforms into the buffers
      if(modelIndex == 0) {
        for(int storeIndex = 0; storeIndex < Math.min(128, pool.size() - i); storeIndex++) {
          pool.get(i + storeIndex).storeTransforms(storeIndex, this.transforms2Buffer);
        }

        this.transforms2Uniform.set(this.transforms2Buffer);
        this.lightUniform.set(this.lightBuffer);
      }

      final QueuedModel<?, ?> entry = pool.get(i);
      entry.useShader(modelIndex, 1);

      this.state.scissor(entry);

      if(entry.shouldRender(null)) {
        if(backFaceCulling) {
          this.state.backfaceCulling(entry.obj.useBackfaceCulling());
        }

        entry.useTexture();
        entry.render(null);
      }

      // First pass of translucency rendering - renders opaque pixels with translucency bit not set for translucent primitives
      if(entry.hasTranslucency()) {
        for(int translucencyIndex = 0; translucencyIndex < Translucency.FOR_RENDERING.length; translucencyIndex++) {
          final Translucency translucency = Translucency.FOR_RENDERING[translucencyIndex];

          if(entry.shouldRender(translucency)) {
            this.state.backfaceCulling(false);
            entry.useTexture();
            entry.render(translucency);
          }
        }
      }
    }
  }

  private void renderPoolTranslucent(final RenderBatch batch, final QueuePool<QueuedModel<?, ?>> pool) {
    if(pool.isEmpty()) {
      return;
    }

    this.state.backfaceCulling(false);

    // Do not update the depth mask so that we don't prevent things further away than this from rendering
    glDepthMask(false);
    glEnable(GL_DEPTH_TEST);
    glDepthFunc(GL_LEQUAL);

    glEnable(GL_BLEND);

    for(int i = 0; i < pool.size(); i++) {
      final int modelIndex = i & 0x7f;

      // Load the next 128 model transforms into the buffers
      if(modelIndex == 0) {
        for(int storeIndex = 0; storeIndex < Math.min(128, pool.size() - i); storeIndex++) {
          pool.get(i + storeIndex).storeTransforms(storeIndex, this.transforms2Buffer);
        }

        this.transforms2Uniform.set(this.transforms2Buffer);
        this.lightUniform.set(this.lightBuffer);
      }

      final QueuedModel<?, ?> entry = pool.get(i);

      if(entry.hasTranslucency()) {
        entry.useShader(modelIndex, 2);

        this.state.scissor(entry);

        entry.useTexture();

        if(entry.shouldRender(Translucency.HALF_B_PLUS_HALF_F)) {
          Translucency.HALF_B_PLUS_HALF_F.setGlState();
          entry.render(Translucency.HALF_B_PLUS_HALF_F);
        }

        if(entry.shouldRender(Translucency.B_PLUS_F)) {
          Translucency.B_PLUS_F.setGlState();
          entry.render(Translucency.B_PLUS_F);
        }

        if(entry.shouldRender(Translucency.B_MINUS_F)) {
          Translucency.B_PLUS_F.setGlState(); // yes B-F uses negated additive blending
          entry.render(Translucency.B_MINUS_F);
        }

        if(entry.shouldRender(Translucency.B_PLUS_QUARTER_F)) {
          Translucency.B_PLUS_F.setGlState();
          entry.render(Translucency.B_PLUS_QUARTER_F);
        }
      }
    }
  }

  private void handleMovement() {
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
  }

  /**
   * @param transforms Matrix used for transforms, contents will be overwritten
   */
  public QueuedModelStandard queueLine(final Matrix4f transforms, final float z, final Vector2f p0, final Vector2f p1) {
    return this.queueLine(this.opaqueQuad, transforms, z, p0, p1);
  }

  /**
   * @param transforms Matrix used for transforms, contents will be overwritten
   */
  public QueuedModelStandard queueLine(final Obj obj, final Matrix4f transforms, final float z, final Vector2f p0, final Vector2f p1) {
    final float dx = p0.x - p1.x;
    final float dy = p0.y - p1.y;
    final float angle = MathHelper.HALF_PI + MathHelper.atan2(dy, dx);
    final float length = (float)Math.sqrt(dx * dx + dy * dy);

    transforms.translation(p0.x + this.mainBatch.widescreenOrthoOffsetX, p0.y, z);
    transforms.rotateZ(angle);
    transforms.scale(1.0f, length, 1.0f);
    return this.queueOrthoModel(obj, transforms, QueuedModelStandard.class);
  }

  public void setProjectionMode(final ProjectionMode projectionMode) {
    this.setProjectionMode(this.mainBatch, projectionMode);
  }

  public void setProjectionMode(final RenderBatch batch, final ProjectionMode projectionMode) {
    final boolean highQualityProjection = batch.allowHighQualityProjection && CONFIG.getConfig(CoreMod.HIGH_QUALITY_PROJECTION_CONFIG.get());

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
        this.state.backfaceCulling(false);
        this.setTransforms(this.camera2d, batch.orthographicProjection);
        this.projectionBuffer.put(3, 0.0f); // Projection mode: ortho
      }

      case _3D -> {
        this.state.backfaceCulling(true);
        this.setTransforms(this.camera3d, batch.perspectiveProjection);

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

  private final Comparator<QueuedModel<?, ?>> translucencySorter = Comparator.comparingDouble((QueuedModel<?, ?> model) -> model.transforms.m32()).reversed();

  private void sortOrthoPool(final QueuePool<QueuedModel<?, ?>> pool) {
    pool.sort(this.translucencySorter);
  }

  public <T extends QueuedModel<?, ?>> T queueModel(final Obj obj, final Class<T> type) {
    return this.mainBatch.queueModel(obj, type);
  }

  public <T extends QueuedModel<?, ?>> T queueModel(final Obj obj, final MV mv, final Class<T> type) {
    return this.mainBatch.queueModel(obj, mv, type);
  }

  public <T extends QueuedModel<?, ?> & LitModel> T queueModel(final Obj obj, final MV mv, final MV lightMv, final Class<T> type) {
    return this.mainBatch.queueModel(obj, mv, lightMv, type);
  }

  public <T extends QueuedModel<?, ?>> T queueModel(final Obj obj, final Matrix4f mv, final Class<T> type) {
    return this.mainBatch.queueModel(obj, mv, type);
  }

  public <T extends QueuedModel<?, ?> & LitModel> T queueModel(final Obj obj, final Matrix4f mv, final MV lightMv, final Class<T> type) {
    return this.mainBatch.queueModel(obj, mv, lightMv, type);
  }

  public <T extends QueuedModel<?, ?>> T queueOrthoModel(final Obj obj, final Class<T> type) {
    return this.mainBatch.queueOrthoModel(obj, type);
  }

  public <T extends QueuedModel<?, ?>> T queueOrthoModel(final Obj obj, final MV mv, final Class<T> type) {
    return this.mainBatch.queueOrthoModel(obj, mv, type);
  }

  /** NOTE: you have to add widescreenOrthoOffsetX yourself */
  public <T extends QueuedModel<?, ?>> T queueOrthoModel(final Obj obj, final Matrix4f transforms, final Class<T> type) {
    return this.mainBatch.queueOrthoModel(obj, transforms, type);
  }

  private void pre() {
    glViewport(0, 0, (int)(this.renderWidth * this.window.getScale()), (int)(this.renderHeight * this.window.getScale()));

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

  public void updateResolution() {
    this.onResize(this.window, this.window.getWidth(), this.window.getHeight());
  }

  private void onResize(final Window window, final int width, final int height) {
    if(width == 0 && height == 0) {
      return;
    }

    LOGGER.info("Resizing window to %dx%d", width, height);

    final Resolution res = CONFIG.getConfig(CoreMod.RESOLUTION_CONFIG.get());
    if(res == Resolution.NATIVE) {
      this.renderWidth = width;
      this.renderHeight = height;
    } else {
      this.renderWidth = (int)((float)res.verticalResolution / height * width);
      this.renderHeight = res.verticalResolution;
    }

    this.renderAspectRatio = (float)this.renderWidth / (float)this.renderHeight;

    // glLineWidth has been removed on M3 macs
    if(!this.isMac()) {
      glLineWidth(Math.max(1, this.renderHeight / 480.0f));
    }

    // Projections
    this.updateProjections();

    for(int i = 0; i < this.batches.size(); i++) {
      this.batches.get(i).updateProjections();
    }

    // Textures
    for(int i = 0; i < this.renderTextures.length; i++) {
      if(this.renderTextures[i] != null) {
        this.renderTextures[i].delete();
      }

      this.renderTextures[i] = Texture.create(builder -> {
        builder.size(this.renderWidth, this.renderHeight);
        builder.internalFormat(GL_RGBA16_SNORM);
        builder.dataFormat(GL_RGBA);
        builder.dataType(GL_UNSIGNED_BYTE);
        builder.magFilter(GL_NEAREST);
        builder.minFilter(GL_LINEAR);
      });
    }

    if(this.depthTexture != null) {
      this.depthTexture.delete();
    }

    this.depthTexture = Texture.create(builder -> {
      builder.size(this.renderWidth, this.renderHeight);
      builder.internalFormat(GL_DEPTH_COMPONENT);
      builder.dataFormat(GL_DEPTH_COMPONENT);
      builder.dataType(GL_FLOAT);
    });


    // Render buffers
    for(int i = 0; i < this.renderBuffers.length; i++) {
      if(this.renderBuffers[i] != null) {
        this.renderBuffers[i].delete();
      }

      final int finalI = i;
      this.renderBuffers[i] = FrameBuffer.create(builder -> {
        builder.attachment(this.renderTextures[finalI], GL_COLOR_ATTACHMENT0);
        builder.attachment(this.depthTexture, GL_DEPTH_ATTACHMENT);
      });
    }
  }

  private boolean isMac() {
    final String os = System.getProperty("os.name").toLowerCase(Locale.US);
    return os.contains("mac os x") || os.contains("darwin") || os.contains("osx");
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

      this.pitch = clamp(this.pitch, -PI / 2, PI / 2);

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
    } else if(key == GLFW_KEY_F5) {
      this.reloadShaders = true;
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
    } else if(key == GLFW_KEY_EQUAL) {
      Config.setGameSpeedMultiplier(Math.min(Config.getGameSpeedMultiplier() + 1, 16));
    } else if(key == GLFW_KEY_MINUS) {
      Config.setGameSpeedMultiplier(Math.max(Config.getGameSpeedMultiplier() - 1, 1));
    }

    if(key == GLFW_KEY_M && (mods & GLFW_MOD_CONTROL) != 0) {
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
}
