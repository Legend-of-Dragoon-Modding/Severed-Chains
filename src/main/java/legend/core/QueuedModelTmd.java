package legend.core;

import legend.core.gte.MV;
import legend.core.opengl.Obj;
import legend.core.opengl.Shader;
import legend.core.opengl.ShaderOptionsTmd;
import legend.game.types.Translucency;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import javax.annotation.Nullable;
import java.nio.FloatBuffer;

public class QueuedModelTmd extends QueuedModel<ShaderOptionsTmd, QueuedModelTmd> implements LitModel {
  final Matrix4f lightTransforms = new Matrix4f();
  final FloatBuffer lightingBuffer;

  final Matrix4f lightDirection = new Matrix4f();
  final Matrix3f lightColour = new Matrix3f();
  final Vector4f backgroundColour = new Vector4f();
  boolean lightUsed;

  /** The untextured translucency override from the TMD header */
  int tmdTranslucency;

  public QueuedModelTmd(final RenderBatch batch, final Shader<ShaderOptionsTmd> shader, final ShaderOptionsTmd shaderOptions, final FloatBuffer lightingBuffer) {
    super(batch, shader, shaderOptions);
    this.lightingBuffer = lightingBuffer;
  }

  public QueuedModelTmd lightDirection(final Matrix3f lightDirection) {
    this.lightDirection.set(lightDirection).mul(this.lightTransforms).setTranslation(0.0f, 0.0f, 0.0f);
    this.lightUsed = true;
    return this;
  }

  public QueuedModelTmd lightColour(final Matrix3f lightColour) {
    this.lightColour.set(lightColour);
    this.lightUsed = true;
    return this;
  }

  public QueuedModelTmd backgroundColour(final Vector3f backgroundColour) {
    this.backgroundColour.set(backgroundColour, 0.0f);
    this.lightUsed = true;
    return this;
  }

  public QueuedModelTmd tmdTranslucency(final int tmdTranslucency) {
    this.tmdTranslucency = tmdTranslucency;
    return this;
  }

  @Override
  void acquire(final Obj obj) {
    super.acquire(obj);

    this.lightTransforms.set(this.transforms);
    this.lightUsed = false;
    this.tmdTranslucency = 0;
  }

  @Override
  void setTransforms(final MV transforms) {
    super.setTransforms(transforms);
    this.lightTransforms.set(this.transforms);
  }

  @Override
  void setTransforms(final Matrix4f transforms) {
    super.setTransforms(transforms);
    this.lightTransforms.set(this.transforms);
  }

  @Override
  public void setLightTransforms(final MV light) {
    this.lightTransforms.set(light).setTranslation(light.transfer);
  }

  @Override
  public void useShader(final int modelIndex, final int discardMode) {
    super.useShader(modelIndex, discardMode);
    this.shaderOptions.tmdTranslucency(this.tmdTranslucency);
  }

  @Override
  public boolean shouldRender(@Nullable final Translucency translucency) {
    if(this.hasTranslucency() && !this.obj.hasTexture()) {
      return translucency != null && this.tmdTranslucency == translucency.ordinal();
    }

    return super.shouldRender(translucency);
  }

  @Override
  public boolean shouldRender(@Nullable final Translucency translucency, final int layer) {
    if(this.hasTranslucency() && !this.obj.hasTexture()) {
      return translucency != null && this.tmdTranslucency == translucency.ordinal();
    }

    return super.shouldRender(translucency, layer);
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
    if(this.obj.hasTranslucency() && !this.obj.hasTexture()) {
      // Translucency override
      this.updateColours(translucency);
      this.obj.render(layer, this.startVertex, this.vertexCount);
      return;
    }

    super.render(translucency, layer);
  }
}
