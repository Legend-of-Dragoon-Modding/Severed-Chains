package legend.core;

import legend.core.gte.MV;
import legend.core.opengl.Obj;
import legend.core.opengl.Shader;
import legend.core.opengl.ShaderOptionsStandard;
import legend.game.types.Translucency;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import javax.annotation.Nullable;
import java.nio.FloatBuffer;

public class QueuedModelStandard extends QueuedModel<ShaderOptionsStandard, QueuedModelStandard> implements LitModel {
  private Translucency translucency;
  private boolean hasTranslucencyOverride;
  private float alpha;
  private boolean useTextureAlpha;

  final Matrix4f lightTransforms = new Matrix4f();
  final FloatBuffer lightingBuffer;

  final Matrix4f lightDirection = new Matrix4f();
  final Matrix3f lightColour = new Matrix3f();
  final Vector4f backgroundColour = new Vector4f();
  private boolean lightUsed;

  public QueuedModelStandard(final RenderBatch batch, final Shader<ShaderOptionsStandard> shader, final ShaderOptionsStandard shaderOptions, final FloatBuffer lightingBuffer) {
    super(batch, shader, shaderOptions);
    this.lightingBuffer = lightingBuffer;
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
  void acquire(final Obj obj, final int sequence) {
    super.acquire(obj, sequence);
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


  public QueuedModelStandard lightDirection(final Matrix3f lightDirection) {
    this.lightDirection.set(lightDirection).mul(this.lightTransforms).setTranslation(0.0f, 0.0f, 0.0f);
    this.lightUsed = true;
    return this;
  }

  public QueuedModelStandard lightColour(final Matrix3f lightColour) {
    this.lightColour.set(lightColour);
    this.lightUsed = true;
    return this;
  }

  public QueuedModelStandard backgroundColour(final Vector3f backgroundColour) {
    this.backgroundColour.set(backgroundColour, 0.0f);
    this.lightUsed = true;
    return this;
  }

  @Override
  public void setLightTransforms(MV light) {
    this.lightTransforms.set(light).setTranslation(light.transfer);
  }

  @Override
  void storeTransforms(final int modelIndex, final FloatBuffer transforms2Buffer) {
    super.storeTransforms(modelIndex, transforms2Buffer);

    if(this.lightUsed) {
      this.lightDirection.get(modelIndex * 32, this.lightingBuffer);
      this.lightColour.get3x4(modelIndex * 32 + 16, this.lightingBuffer);
      this.backgroundColour.get(modelIndex * 32 + 28, this.lightingBuffer);
    }
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
