package legend.core;

import legend.core.gte.MV;
import legend.core.opengl.Obj;
import legend.core.opengl.Shader;
import legend.core.opengl.ShaderOptionsBattleTmd;
import legend.game.types.Translucency;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import javax.annotation.Nullable;
import java.nio.FloatBuffer;

public class QueuedModelBattleTmd extends QueuedModel<ShaderOptionsBattleTmd, QueuedModelBattleTmd> implements LitModel {
  final Shader.UniformBuffer vdfUniform;
  final FloatBuffer vdfBuffer;
  final Matrix4f lightTransforms = new Matrix4f();
  final FloatBuffer lightingBuffer;

  final Matrix4f lightDirection = new Matrix4f();
  final Matrix3f lightColour = new Matrix3f();
  final Vector4f backgroundColour = new Vector4f();
  boolean lightUsed;

  /** The untextured translucency override from the TMD header */
  int tmdTranslucency;

  int ctmdFlags;
  final Vector3f battleColour = new Vector3f();
  Vector3f[] vdf;

  public QueuedModelBattleTmd(final RenderBatch batch, final Shader<ShaderOptionsBattleTmd> shader, final ShaderOptionsBattleTmd shaderOptions, final Shader.UniformBuffer vdfUniform, final FloatBuffer vdfBuffer, final FloatBuffer lightingBuffer) {
    super(batch, shader, shaderOptions);
    this.vdfUniform = vdfUniform;
    this.vdfBuffer = vdfBuffer;
    this.lightingBuffer = lightingBuffer;
  }

  public QueuedModelBattleTmd lightDirection(final Matrix3f lightDirection) {
    this.lightDirection.set(lightDirection).mul(this.lightTransforms).setTranslation(0.0f, 0.0f, 0.0f);
    this.lightUsed = true;
    return this;
  }

  public QueuedModelBattleTmd lightColour(final Matrix3f lightColour) {
    this.lightColour.set(lightColour);
    this.lightUsed = true;
    return this;
  }

  public QueuedModelBattleTmd backgroundColour(final Vector3f backgroundColour) {
    this.backgroundColour.set(backgroundColour, 0.0f);
    this.lightUsed = true;
    return this;
  }

  public QueuedModelBattleTmd tmdTranslucency(final int tmdTranslucency) {
    this.tmdTranslucency = tmdTranslucency;
    return this;
  }

  public QueuedModelBattleTmd ctmdFlags(final int ctmdFlags) {
    this.ctmdFlags = ctmdFlags;
    return this;
  }

  public QueuedModelBattleTmd battleColour(final Vector3f colour) {
    this.battleColour.set(colour);
    return this;
  }

  public QueuedModelBattleTmd vdf(final Vector3f[] vdf) {
    this.vdf = vdf;
    return this;
  }

  public boolean isTranslucent() {
    return (this.ctmdFlags & 0x2) != 0;
  }

  public boolean isUniformLit() {
    return (this.ctmdFlags & 0x10) != 0;
  }

  public boolean isCtmd() {
    return (this.ctmdFlags & 0x20) != 0;
  }

  @Override
  void acquire(final Obj obj) {
    super.acquire(obj);

    this.lightTransforms.set(this.transforms);
    this.lightUsed = false;
    this.tmdTranslucency = 0;
    this.ctmdFlags = 0;
    this.battleColour.zero();
    this.vdf = null;
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
  public boolean hasTranslucency() {
    return super.hasTranslucency() || (this.ctmdFlags & 0x2) != 0;
  }

  @Override
  public boolean hasTranslucency(final int index) {
    return super.hasTranslucency(index) || (this.ctmdFlags & 0x2) != 0;
  }

  @Override
  public void useShader(final int modelIndex, final int discardMode) {
    super.useShader(modelIndex, discardMode);
    this.shaderOptions.tmdTranslucency(this.tmdTranslucency);
    this.shaderOptions.ctmdFlags(this.ctmdFlags);
    this.shaderOptions.battleColour(this.battleColour);

    if(this.vdf != null) {
      this.shaderOptions.useVdf(true);
      this.setVdf(this.vdf);
    } else {
      this.shaderOptions.useVdf(false);
    }
  }

  private void setVdf(final Vector3f[] vertices) {
    for(int i = 0; i < vertices.length; i++) {
      vertices[i].get(i * 0x4, this.vdfBuffer);
    }

    this.vdfUniform.set(this.vdfBuffer);
  }

  @Override
  public boolean shouldRender(@Nullable final Translucency translucency) {
    if(this.hasTranslucency() && (!this.obj.hasTexture() || this.isUniformLit())) {
      return translucency != null && this.tmdTranslucency == translucency.ordinal();
    }

    return super.shouldRender(translucency) || (this.ctmdFlags & 0x2) != 0 && translucency != null && this.tmdTranslucency == translucency.ordinal();
  }

  @Override
  public boolean shouldRender(@Nullable final Translucency translucency, final int layer) {
    if(this.hasTranslucency(layer) && (!this.obj.hasTexture(layer) || this.isUniformLit())) {
      return translucency != null && this.tmdTranslucency == translucency.ordinal();
    }

    return super.shouldRender(translucency, layer) || (this.ctmdFlags & 0x2) != 0 && translucency != null && this.tmdTranslucency == translucency.ordinal();
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
    if(this.isTranslucent() || this.obj.hasTranslucency(layer) && (!this.obj.hasTexture(layer) || this.isUniformLit())) {
      // Translucency override
      this.updateColours(translucency);
      this.obj.render(layer, this.startVertex, this.vertexCount);
      return;
    }

    super.render(translucency, layer);
  }
}
