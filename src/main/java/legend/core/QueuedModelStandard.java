package legend.core;

import legend.core.opengl.Obj;
import legend.core.opengl.Shader;
import legend.core.opengl.ShaderOptionsStandard;
import legend.game.types.Translucency;

import javax.annotation.Nullable;

public class QueuedModelStandard extends QueuedModel<ShaderOptionsStandard, QueuedModelStandard> {
  Translucency translucency;
  boolean hasTranslucencyOverride;

  public QueuedModelStandard(final RenderBatch batch, final Shader<ShaderOptionsStandard> shader, final ShaderOptionsStandard shaderOptions) {
    super(batch, shader, shaderOptions);
  }

  public QueuedModelStandard translucency(@Nullable final Translucency translucency) {
    this.translucency = translucency;
    this.hasTranslucencyOverride = true;

    if(this.obj.shouldRender(Translucency.HALF_B_PLUS_HALF_F)) {
      this.batch.needsSorting = true;
    }

    return this;
  }

  @Override
  void acquire(final Obj obj) {
    super.acquire(obj);
    this.hasTranslucencyOverride = false;
  }

  @Override
  public boolean hasTranslucency() {
    return super.hasTranslucency() || this.hasTranslucencyOverride;
  }

  @Override
  public boolean shouldRender(@Nullable final Translucency translucency) {
    if(this.hasTranslucencyOverride) {
      return this.translucency == translucency;
    }

    return super.shouldRender(translucency);
  }

  @Override
  void render(@Nullable final Translucency translucency) {
    if(translucency != null) {
      this.shaderOptions.translucency(translucency);
    } else {
      this.shaderOptions.opaque();
    }

    if(this.hasTranslucencyOverride) {
      // Translucency override
      this.updateColours(translucency);
      this.obj.render(this.startVertex, this.vertexCount);
      return;
    }

    super.render(translucency);
  }
}
