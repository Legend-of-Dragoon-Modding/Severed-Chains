package legend.core;

import legend.core.gte.MV;
import legend.core.opengl.Obj;
import legend.core.opengl.Shader;
import legend.core.opengl.SubmapWidescreenMode;
import legend.game.EngineState;
import legend.game.modding.coremod.CoreMod;
import legend.game.types.Translucency;
import org.joml.Matrix4f;

import java.nio.FloatBuffer;
import java.util.function.Supplier;

import static legend.core.GameEngine.CONFIG;
import static legend.core.MathHelper.PI;

public class RenderBatch {
  public final RenderEngine engine;

  /** The PS1 native width (usually 320, sometimes 368) */
  public int nativeWidth = 320;
  /** The PS1 native height */
  public int nativeHeight = 240;
  /** The PS1 projection depth */
  public float projectionDepth;
  /** The ratio of 320 / {@link #nativeHeight} */
  public float aspectRatio = (float)this.nativeWidth / this.nativeHeight;
  /** Field of view calculated from aspect ratio and projection depth */
  public float fieldOfView;

  /** The render mode for this batch */
  public EngineState.RenderMode renderMode = EngineState.RenderMode.PERSPECTIVE;
  /** Used to center ortho rendering when not in 4:3 aspect ratio */
  public float widescreenOrthoOffsetX;
  /** Expected PS1 render width, always 320 except 368 when using expanded submaps */
  public int expectedWidth = 320;

  public boolean needsSorting;

  public final Matrix4f perspectiveProjection = new Matrix4f();
  public final Matrix4f orthographicProjection = new Matrix4f();

  private final Supplier<Shader.UniformBuffer> vdfUniformSupplier;

  public final QueuePool<QueuedModel<?, ?>> modelPool;
  public final QueuePool<QueuedModel<?, ?>> orthoPool;

  public RenderBatch(final RenderEngine engine, final Supplier<Shader.UniformBuffer> vdfUniformSupplier, final FloatBuffer vdfBuffer, final FloatBuffer lightingBuffer) {
    this.engine = engine;
    this.vdfUniformSupplier = vdfUniformSupplier;
    this.modelPool = this.makePool(vdfBuffer, lightingBuffer);
    this.orthoPool = this.makePool(vdfBuffer, lightingBuffer);
  }

  public RenderBatch(final RenderEngine engine, final RenderBatch current, final Supplier<Shader.UniformBuffer> vdfUniformSupplier, final FloatBuffer vdfBuffer, final FloatBuffer lightingBuffer) {
    this(engine, vdfUniformSupplier, vdfBuffer, lightingBuffer);
    this.nativeWidth = current.nativeWidth;
    this.nativeHeight = current.nativeHeight;
    this.projectionDepth = current.projectionDepth;
    this.aspectRatio = current.aspectRatio;
    this.fieldOfView = current.fieldOfView;
    this.renderMode = current.renderMode;
    this.widescreenOrthoOffsetX = current.widescreenOrthoOffsetX;
    this.expectedWidth = current.expectedWidth;
    this.perspectiveProjection.set(current.perspectiveProjection);
    this.orthographicProjection.set(current.orthographicProjection);
  }

  private QueuePool<QueuedModel<?, ?>> makePool(final FloatBuffer vdfBuffer, final FloatBuffer lightingBuffer) {
    final QueuePool<QueuedModel<?, ?>> pool = new QueuePool<>();
    pool.addType(QueuedModelStandard.class, () -> new QueuedModelStandard(this, this.engine.standardShader, this.engine.standardShaderOptions));
    pool.addType(QueuedModelTmd.class, () -> new QueuedModelTmd(this, this.engine.tmdShader, this.engine.tmdShaderOptions, lightingBuffer));
    pool.addType(QueuedModelBattleTmd.class, () -> new QueuedModelBattleTmd(this, this.engine.battleTmdShader, this.engine.battleTmdShaderOptions, this.vdfUniformSupplier.get(), vdfBuffer, lightingBuffer));
    return pool;
  }

  void reset() {
    this.modelPool.reset();
    this.orthoPool.reset();
  }

  public void setRenderMode(final EngineState.RenderMode renderMode) {
    this.renderMode = renderMode;
    this.updateProjections();
  }

  public EngineState.RenderMode getRenderMode() {
    return this.renderMode;
  }

  public void setProjectionSize(final int width, final int height) {
    this.nativeWidth = width;
    this.nativeHeight = height;
    this.updateFieldOfView();
  }

  public int getNativeWidth() {
    return this.nativeWidth;
  }

  public int getNativeHeight() {
    return this.nativeHeight;
  }

  public void setProjectionDepth(final float depth) {
    this.projectionDepth = depth;
    this.updateFieldOfView();
  }

  public void updateFieldOfView() {
    this.aspectRatio = 320.0f / this.nativeHeight;
    final float halfWidth = this.nativeWidth / 2.0f;
    this.fieldOfView = (float)(Math.atan(halfWidth / this.projectionDepth) * 2.0f / this.aspectRatio);
    this.updateProjections();
  }

  public void updateProjections() {
    this.expectedWidth = 320;

    if(RenderEngine.legacyMode != 0) {
      this.perspectiveProjection.setPerspectiveLH(PI / 4.0f, (float)this.engine.getRenderWidth() / this.engine.getRenderHeight(), 0.1f, 500.0f);
      this.orthographicProjection.setOrtho2D(0.0f, this.engine.getRenderWidth(), this.engine.getRenderHeight(), 0.0f);
      return;
    }

    // LOD uses a left-handed projection with a negated Y axis because reasons.
    if(this.renderMode == EngineState.RenderMode.PERSPECTIVE) {
      final float ratio;
      if(CoreMod.ALLOW_WIDESCREEN_CONFIG.isValid() && CONFIG.getConfig(CoreMod.ALLOW_WIDESCREEN_CONFIG.get())) {
        ratio = (float)this.engine.getRenderWidth() / this.engine.getRenderHeight();
        final float w = this.nativeHeight * ratio;
        final float h = this.nativeHeight;
        this.orthographicProjection.setOrthoLH(0.0f, w * ((float)this.nativeWidth / this.expectedWidth), h, 0.0f, 0.0f, 1000000.0f);
        this.widescreenOrthoOffsetX = (w - this.expectedWidth) / 2.0f;
      } else {
        ratio = this.aspectRatio;
        this.orthographicProjection.setOrthoLH(0.0f, this.nativeWidth, this.nativeHeight, 0.0f, 0.0f, 1000000.0f);
        this.widescreenOrthoOffsetX = 0.0f;
      }

      this.perspectiveProjection.setPerspectiveLH(this.fieldOfView, ratio, 0.1f, 1000000.0f);
      this.perspectiveProjection.negateY();
    } else {
      // Our perspective projection is actually a centred orthographic projection. We are doing a
      // projection plane division in the vertex shader to emulate perspective division on the GTE.
      if(CONFIG.getConfig(CoreMod.LEGACY_WIDESCREEN_MODE_CONFIG.get()) == SubmapWidescreenMode.EXPANDED) {
        this.expectedWidth = 368;
        final float ratio = (float)this.engine.getRenderWidth() / this.engine.getRenderHeight();
        final int h = this.nativeHeight;
        final int w = Math.round(h * ratio) + 1 & ~1;
        this.perspectiveProjection.setOrthoLH(-w / 2.0f, w / 2.0f, h / 2.0f, -h / 2.0f, 0.0f, 1000000.0f);
        this.orthographicProjection.setOrthoLH(0.0f, w, h, 0.0f, 0.0f, 1000000.0f);
        this.widescreenOrthoOffsetX = (w - this.nativeWidth) / 2.0f;
      } else {
        this.perspectiveProjection.setOrthoLH(-this.nativeWidth / 2.0f, this.nativeWidth / 2.0f, this.nativeHeight / 2.0f, -this.nativeHeight / 2.0f, 0.0f, 1000000.0f);
        this.orthographicProjection.setOrthoLH(0.0f, this.nativeWidth, this.nativeHeight, 0.0f, 0.0f, 1000000.0f);
        this.widescreenOrthoOffsetX = 0.0f;
      }
    }
  }

  public <T extends QueuedModel<?, ?>> T queueModel(final Obj obj, final Class<T> type) {
    if(obj == null) {
      throw new IllegalArgumentException("obj is null");
    }

    this.temp.identity();

    final T entry = this.modelPool.acquire(type);
    entry.acquire(obj, this.temp);
    return entry;
  }

  public <T extends QueuedModel<?, ?>> T queueModel(final Obj obj, final MV mv, final Class<T> type) {
    if(obj == null) {
      throw new IllegalArgumentException("obj is null");
    }

    final T entry = this.modelPool.acquire(type);
    entry.acquire(obj, mv);
    return entry;
  }

  public <T extends QueuedModel<?, ?> & LitModel> T queueModel(final Obj obj, final MV mv, final MV lightMv, final Class<T> type) {
    if(obj == null) {
      throw new IllegalArgumentException("obj is null");
    }

    final T entry = this.modelPool.acquire(type);
    entry.acquire(obj, mv);
    entry.setLightTransforms(lightMv);
    return entry;
  }

  public <T extends QueuedModel<?, ?>> T queueModel(final Obj obj, final Matrix4f mv, final Class<T> type) {
    if(obj == null) {
      throw new IllegalArgumentException("obj is null");
    }

    final T entry = this.modelPool.acquire(type);
    entry.acquire(obj, mv);
    return entry;
  }

  public <T extends QueuedModel<?, ?> & LitModel> T queueModel(final Obj obj, final Matrix4f mv, final MV lightMv, final Class<T> type) {
    if(obj == null) {
      throw new IllegalArgumentException("obj is null");
    }

    final T entry = this.modelPool.acquire(type);
    entry.acquire(obj, mv);
    entry.setLightTransforms(lightMv);
    return entry;
  }

  private final Matrix4f temp = new Matrix4f();

  public <T extends QueuedModel<?, ?>> T queueOrthoModel(final Obj obj, final Class<T> type) {
    if(obj == null) {
      throw new IllegalArgumentException("obj is null");
    }

    if(obj.shouldRender(Translucency.HALF_B_PLUS_HALF_F)) {
      this.needsSorting = true;
    }

    this.temp.identity().setTranslation(this.widescreenOrthoOffsetX, 0.0f, 0.0f);

    final T entry = this.orthoPool.acquire(type);
    entry.acquire(obj, this.temp);
    return entry;
  }

  public <T extends QueuedModel<?, ?>> T queueOrthoModel(final Obj obj, final MV mv, final Class<T> type) {
    if(obj == null) {
      throw new IllegalArgumentException("obj is null");
    }

    if(obj.shouldRender(Translucency.HALF_B_PLUS_HALF_F)) {
      this.needsSorting = true;
    }

    this.temp.set(mv).setTranslation(mv.transfer.x + this.widescreenOrthoOffsetX, mv.transfer.y, mv.transfer.z);

    final T entry = this.orthoPool.acquire(type);
    entry.acquire(obj, this.temp);
    return entry;
  }

  /** NOTE: you have to add widescreenOrthoOffsetX yourself */
  public <T extends QueuedModel<?, ?>> T queueOrthoModel(final Obj obj, final Matrix4f transforms, final Class<T> type) {
    if(obj == null) {
      throw new IllegalArgumentException("obj is null");
    }

    if(obj.shouldRender(Translucency.HALF_B_PLUS_HALF_F)) {
      this.needsSorting = true;
    }

    final T entry = this.orthoPool.acquire(type);
    entry.acquire(obj, transforms);
    return entry;
  }
}
