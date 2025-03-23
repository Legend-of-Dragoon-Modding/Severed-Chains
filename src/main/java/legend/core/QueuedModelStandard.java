package legend.core;

import legend.core.opengl.Obj;
import legend.core.opengl.Shader;
import legend.core.opengl.ShaderOptionsStandard;
import legend.game.types.Translucency;

import javax.annotation.Nullable;

public class QueuedModelStandard extends QueuedModel<ShaderOptionsStandard, QueuedModelStandard> {
  private Translucency translucency;
  private boolean hasTranslucencyOverride;
  private float alpha;
  private boolean useTextureAlpha;

  public QueuedModelStandard(final RenderBatch batch, final Shader<ShaderOptionsStandard> shader, final ShaderOptionsStandard shaderOptions) {
    super(batch, shader, shaderOptions);
  }

  public QueuedModelStandard translucency(@Nullable final Translucency translucency) {
    this.translucency = translucency;
    this.hasTranslucencyOverride = true;

    if(translucency == Translucency.HALF_B_PLUS_HALF_F) {
      this.batch.needsSorting = true;
    }

    return this;
  }

  /** Changes translucency mode to true alpha */
  public QueuedModelStandard alpha(final float alpha) {
    this.alpha = alpha;
    this.batch.needsSorting = true;
    return this;
  }

  /** Use texture's alpha channel */
  public QueuedModelStandard useTextureAlpha() {
    this.useTextureAlpha = true;
    return this;
  }

  @Override
  void acquire(final Obj obj) {
    super.acquire(obj);
    this.hasTranslucencyOverride = false;
    this.alpha = -1.0f;
    this.useTextureAlpha = false;
  }

  @Override
  public boolean hasTranslucency() {
    return super.hasTranslucency() || this.hasTranslucencyOverride;
  }

  @Override
  public boolean hasTranslucency(final int index) {
    return super.hasTranslucency(index) || this.hasTranslucencyOverride;
  }

  @Override
  public boolean shouldRender(@Nullable final Translucency translucency) {
    if(this.hasTranslucencyOverride) {
      return this.translucency == translucency;
    }

    return super.shouldRender(translucency);
  }

  @Override
  public boolean shouldRender(@Nullable final Translucency translucency, final int layer) {
    if(this.hasTranslucencyOverride) {
      return this.translucency == translucency;
    }

    return super.shouldRender(translucency, layer);
  }

  @Override
  void render(@Nullable final Translucency translucency, final int layer) {
    if(translucency != null) {
      this.shaderOptions.translucency(translucency);
    } else {
      this.shaderOptions.opaque();
    }

    this.shaderOptions.alpha(this.alpha);
    this.shaderOptions.useTextureAlpha(this.useTextureAlpha);

    if(this.hasTranslucencyOverride) {
      // Translucency override
      this.updateColours(translucency);
      this.obj.render(layer, this.startVertex, this.vertexCount);
      return;
    }

    super.render(translucency, layer);
  }
}
