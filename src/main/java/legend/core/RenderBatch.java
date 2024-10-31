package legend.core;

import legend.core.gte.MV;
import legend.core.opengl.Obj;
import legend.core.opengl.ShaderManager;
import legend.core.opengl.ShaderOptions;
import legend.core.opengl.ShaderType;
import legend.core.opengl.SubmapWidescreenMode;
import legend.core.opengl.VoidShaderOptions;
import legend.game.modding.coremod.CoreMod;
import legend.game.submap.SMap;
import legend.game.types.Translucency;
import org.joml.Matrix4f;

import static legend.core.GameEngine.CONFIG;
import static legend.core.MathHelper.PI;
import static legend.game.Scus94491BpeSegment.zOffset_1f8003e8;
import static legend.game.Scus94491BpeSegment.zShift_1f8003c4;
import static legend.game.Scus94491BpeSegment_8004.currentEngineState_8004dd04;

public class RenderBatch {
  private final RenderEngine engine;

  /** The PS1 native width (usually 320, sometimes 368) */
  public float projectionWidth;
  /** The PS1 native height */
  public float projectionHeight;
  /** The PS1 projection depth */
  public float projectionDepth;
  /** The ratio of 320 / {@link #projectionHeight} */
  public float aspectRatio;
  /** Field of view calculated from aspect ratio and projection depth */
  public float fieldOfView;

  /** Enables or disables widescreen */
  public boolean allowWidescreen;
  /** Use a modern perspective projection instead of the PS1 H division */
  public boolean allowHighQualityProjection;
  /** Used to center ortho rendering when not in 4:3 aspect ratio */
  public float widescreenOrthoOffsetX;
  /** Expected PS1 render width, always 320 except 368 when using expanded submaps */
  public float expectedWidth = 320.0f;
  /** Squishing factor used in conjunction with expanded submaps */
  public float widthSquisher = 1.0f;
  /** We're on a submap and expanded submaps is enabled */
  public boolean expandedSubmap;

  public boolean needsSorting;

  public final Matrix4f perspectiveProjection = new Matrix4f();
  public final Matrix4f orthographicProjection = new Matrix4f();

  public final QueuePool<QueuedModel<VoidShaderOptions>> modelPool;
  public final QueuePool<QueuedModel<VoidShaderOptions>> orthoPool;
  public final QueuePool<QueuedModel> shaderPool;
  public final QueuePool<QueuedModel> shaderOrthoPool;

  public RenderBatch(final RenderEngine engine) {
    this.engine = engine;
    this.modelPool = new QueuePool<>(() -> new QueuedModel<>(this));
    this.orthoPool = new QueuePool<>(() -> new QueuedModel<>(this));
    this.shaderPool = new QueuePool<>(() -> new QueuedModel<>(this));
    this.shaderOrthoPool = new QueuePool<>(() -> new QueuedModel<>(this));
  }

  public RenderBatch(final RenderEngine engine, final RenderBatch current) {
    this(engine);
    this.projectionWidth = current.projectionWidth;
    this.projectionHeight = current.projectionHeight;
    this.projectionDepth = current.projectionDepth;
    this.aspectRatio = current.aspectRatio;
    this.fieldOfView = current.fieldOfView;
    this.allowWidescreen = current.allowWidescreen;
    this.allowHighQualityProjection = current.allowHighQualityProjection;
    this.widescreenOrthoOffsetX = current.widescreenOrthoOffsetX;
    this.expectedWidth = current.expectedWidth;
    this.widthSquisher = current.widthSquisher;
    this.expandedSubmap = current.expandedSubmap;
    this.perspectiveProjection.set(current.perspectiveProjection);
    this.orthographicProjection.set(current.orthographicProjection);
  }

  void reset() {
    this.modelPool.reset();
    this.orthoPool.reset();
    this.shaderPool.reset();
    this.shaderOrthoPool.reset();
  }

  /** NOTE: you must call {@link #updateProjections} yourself */
  public void setAllowWidescreen(final boolean allowWidescreen) {
    this.allowWidescreen = allowWidescreen;
  }

  public boolean getAllowWidescreen() {
    return this.allowWidescreen;
  }

  /** NOTE: you must call {@link #updateProjections} yourself */
  public void setAllowHighQualityProjection(final boolean allowHighQualityProjection) {
    this.allowHighQualityProjection = allowHighQualityProjection;
  }

  public float getWidthSquisher() {
    return this.widthSquisher;
  }

  public void setProjectionSize(final float width, final float height) {
    this.projectionWidth = width;
    this.projectionHeight = height;
    this.updateFieldOfView();
  }

  public float getProjectionWidth() {
    return this.projectionWidth;
  }

  public float getProjectionHeight() {
    return this.projectionHeight;
  }

  public void setProjectionDepth(final float depth) {
    this.projectionDepth = depth;
    this.updateFieldOfView();
  }

  public void updateFieldOfView() {
    this.aspectRatio = 320.0f / this.projectionHeight;
    final float halfWidth = this.projectionWidth / 2.0f;
    this.fieldOfView = (float)(Math.atan(halfWidth / this.projectionDepth) * 2.0f / this.aspectRatio);
    this.updateProjections();
  }

  public void updateProjections() {
    this.widthSquisher = 1.0f;
    this.expectedWidth = 320.0f;
    this.expandedSubmap = false;

    if(RenderEngine.legacyMode != 0) {
      this.perspectiveProjection.setPerspectiveLH(PI / 4.0f, this.engine.getRenderWidth() / this.engine.getRenderHeight(), 0.1f, 500.0f);
      this.orthographicProjection.setOrtho2D(0.0f, this.engine.getRenderWidth(), this.engine.getRenderHeight(), 0.0f);
      return;
    }

    // LOD uses a left-handed projection with a negated Y axis because reasons.
    if(this.allowHighQualityProjection && (!CoreMod.HIGH_QUALITY_PROJECTION_CONFIG.isValid() || CONFIG.getConfig(CoreMod.HIGH_QUALITY_PROJECTION_CONFIG.get()))) {
      final float ratio;
      if(this.allowWidescreen && CONFIG.getConfig(CoreMod.ALLOW_WIDESCREEN_CONFIG.get())) {
        ratio = this.engine.getRenderWidth() / this.engine.getRenderHeight();
        final float w = this.projectionHeight * ratio;
        final float h = this.projectionHeight;
        this.orthographicProjection.setOrthoLH(0.0f, w * (this.projectionWidth / this.expectedWidth), h, 0.0f, 0.0f, 1000000.0f);
        this.widescreenOrthoOffsetX = (w - this.expectedWidth) / 2.0f;
      } else {
        ratio = this.aspectRatio;
        this.orthographicProjection.setOrthoLH(0.0f, this.projectionWidth, this.projectionHeight, 0.0f, 0.0f, 1000000.0f);
        this.widescreenOrthoOffsetX = 0.0f;
      }

      this.perspectiveProjection.setPerspectiveLH(this.fieldOfView, ratio, 0.1f, 1000000.0f);
      this.perspectiveProjection.negateY();
    } else {
      this.expandedSubmap = currentEngineState_8004dd04 instanceof SMap && CONFIG.getConfig(CoreMod.SUBMAP_WIDESCREEN_MODE_CONFIG.get()) == SubmapWidescreenMode.EXPANDED;

      // Our perspective projection is actually a centred orthographic projection. We are doing a
      // projection plane division in the vertex shader to emulate perspective division on the GTE.
      if(this.allowWidescreen && CONFIG.getConfig(CoreMod.ALLOW_WIDESCREEN_CONFIG.get()) || this.expandedSubmap) {
        if(this.expandedSubmap) {
          this.expectedWidth = 368.0f;
          this.widthSquisher = 368.0f / 320.0f;
        }

        final float ratio = this.engine.getRenderWidth() / this.engine.getRenderHeight();
        final float w = this.projectionHeight * ratio;
        final float h = this.projectionHeight;
        this.perspectiveProjection.setOrthoLH(-w / 2.0f * this.widthSquisher, w / 2.0f * this.widthSquisher, h / 2.0f, -h / 2.0f, 0.0f, 1000000.0f);
        this.orthographicProjection.setOrthoLH(0.0f, w * (this.projectionWidth / this.expectedWidth) * this.widthSquisher, h, 0.0f, 0.0f, 1000000.0f);
        this.widescreenOrthoOffsetX = (w * this.widthSquisher - this.expectedWidth) / 2.0f;
      } else {
        this.perspectiveProjection.setOrthoLH(-this.projectionWidth / 2.0f, this.projectionWidth / 2.0f, this.projectionHeight / 2.0f, -this.projectionHeight / 2.0f, 0.0f, 1000000.0f);
        this.orthographicProjection.setOrthoLH(0.0f, this.projectionWidth, this.projectionHeight, 0.0f, 0.0f, 1000000.0f);
        this.widescreenOrthoOffsetX = 0.0f;
      }
    }
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
    entry.depthOffset(zOffset_1f8003e8 * (1 << zShift_1f8003c4));
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
    entry.depthOffset(zOffset_1f8003e8 * (1 << zShift_1f8003c4));
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
    entry.depthOffset(zOffset_1f8003e8 * (1 << zShift_1f8003c4));
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
    entry.depthOffset(zOffset_1f8003e8 * (1 << zShift_1f8003c4));
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
    entry.depthOffset(zOffset_1f8003e8 * (1 << zShift_1f8003c4));
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
    entry.depthOffset(zOffset_1f8003e8 * (1 << zShift_1f8003c4));
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
    entry.depthOffset(zOffset_1f8003e8 * (1 << zShift_1f8003c4));
    return entry;
  }

  /** NOTE: you have to add widescreenOrthoOffsetX yourself */
  public QueuedModel<VoidShaderOptions> queueOrthoModel(final Obj obj, final Matrix4f transforms) {
    if(obj == null) {
      throw new IllegalArgumentException("obj is null");
    }

    if(obj.shouldRender(Translucency.HALF_B_PLUS_HALF_F)) {
      this.needsSorting = true;
    }

    final QueuedModel<VoidShaderOptions> entry = this.orthoPool.acquire();
    entry.reset();
    entry.obj = obj;
    entry.transforms.set(transforms);
    entry.lightTransforms.set(entry.transforms);
    entry.depthOffset(zOffset_1f8003e8 * (1 << zShift_1f8003c4));
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
    entry.depthOffset(zOffset_1f8003e8 * (1 << zShift_1f8003c4));
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
    entry.depthOffset(zOffset_1f8003e8 * (1 << zShift_1f8003c4));
    return entry;
  }

  public <Options extends ShaderOptions<Options>> QueuedModel<Options> queueOrthoModel(final Obj obj, final MV mv, final ShaderType<Options> shaderType) {
    if(obj == null) {
      throw new IllegalArgumentException("obj is null");
    }

    final QueuedModel<Options> entry = this.shaderOrthoPool.acquire();
    entry.reset();
    entry.obj = obj;
    entry.shader = ShaderManager.getShader(shaderType);
    entry.shaderOptions = entry.shader.makeOptions();
    entry.transforms.set(mv).setTranslation(mv.transfer);
    entry.lightTransforms.set(entry.transforms);
    entry.depthOffset(zOffset_1f8003e8 * (1 << zShift_1f8003c4));
    return entry;
  }
}
