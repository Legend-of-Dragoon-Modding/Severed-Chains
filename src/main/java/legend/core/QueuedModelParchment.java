package legend.core;

import legend.core.opengl.Obj;
import legend.core.opengl.Shader;
import legend.core.opengl.ShaderOptionsParchment;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class QueuedModelParchment extends QueuedModel<ShaderOptionsParchment, QueuedModelParchment> {
  final Vector4f lightColour = new Vector4f(1.0f);
  final Vector4f darkColour = new Vector4f(1.0f);

  public QueuedModelParchment(final RenderBatch batch, final Shader<ShaderOptionsParchment> shader, final ShaderOptionsParchment shaderOptions) {
    super(batch, shader, shaderOptions);
  }

  public QueuedModelParchment lightColour(final Vector3f colour) {
    return this.lightColour(colour.x, colour.y, colour.z);
  }

  public QueuedModelParchment lightColour(final float r, final float g, final float b) {
    this.lightColour.set(r, g, b);
    return this;
  }

  public QueuedModelParchment lightColour(final Vector4f colour) {
    this.lightColour.set(colour);
    return this;
  }

  public QueuedModelParchment lightColour(final float r, final float g, final float b, final  float a) {
    this.lightColour.set(r, g, b, a);
    return this;
  }

  public QueuedModelParchment lightMonochrome(final float shade) {
    return this.lightColour(shade, shade, shade);
  }

  public QueuedModelParchment lightMonochrome(final float shade, final float alpha) {
    return this.lightColour(shade, shade, shade, alpha);
  }

  public QueuedModelParchment darkColour(final Vector3f colour) {
    return this.darkColour(colour.x, colour.y, colour.z);
  }

  public QueuedModelParchment darkColour(final float r, final float g, final float b) {
    this.darkColour.set(r, g, b);
    return this;
  }

  public QueuedModelParchment darkColour(final Vector4f colour) {
    this.darkColour.set(colour);
    return this;
  }

  public QueuedModelParchment darkColour(final float r, final float g, final float b, final  float a) {
    this.darkColour.set(r, g, b, a);
    return this;
  }

  public QueuedModelParchment darkMonochrome(final float shade) {
    return this.darkColour(shade, shade, shade);
  }

  public QueuedModelParchment darkMonochrome(final float shade, final float alpha) {
    return this.darkColour(shade, shade, shade, alpha);
  }

  @Override
  void acquire(final Obj obj, final int sequence) {
    super.acquire(obj, sequence);
    this.lightColour.set(1.0f, 1.0f, 1.0f);
    this.darkColour.set(1.0f, 1.0f, 1.0f);
  }

  @Override
  public void useShader(final int modelIndex, final int discardMode) {
    super.useShader(modelIndex, discardMode);
    this.shaderOptions.lightColour(this.lightColour);
    this.shaderOptions.darkColour(this.darkColour);
  }
}
