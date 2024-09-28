package legend.core;

import legend.core.opengl.VoidShaderOptions;
import org.joml.Matrix4f;

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

  public final Matrix4f perspectiveProjection = new Matrix4f();
  public final Matrix4f orthographicProjection = new Matrix4f();

  public final QueuePool<RenderEngine.QueuedModel<VoidShaderOptions>> modelPool;
  public final QueuePool<RenderEngine.QueuedModel<VoidShaderOptions>> orthoPool;
  public final QueuePool<RenderEngine.QueuedModel> shaderPool;
  public final QueuePool<RenderEngine.QueuedModel> shaderOrthoPool;

  public RenderBatch(final RenderEngine engine) {
    this.engine = engine;
    this.modelPool = new QueuePool<>(() -> new RenderEngine.QueuedModel<>(this.engine));
    this.orthoPool = new QueuePool<>(() -> new RenderEngine.QueuedModel<>(this.engine));
    this.shaderPool = new QueuePool<>(() -> new RenderEngine.QueuedModel<>(this.engine));
    this.shaderOrthoPool = new QueuePool<>(() -> new RenderEngine.QueuedModel<>(this.engine));
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
}
