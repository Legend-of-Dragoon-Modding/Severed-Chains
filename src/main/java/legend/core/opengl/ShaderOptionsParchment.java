package legend.core.opengl;

import org.joml.Vector4f;

public class ShaderOptionsParchment extends ShaderOptionsBase<ShaderOptionsParchment> {
  private final Shader<ShaderOptionsParchment>.UniformVec4 lightColourUniform;
  private final Shader<ShaderOptionsParchment>.UniformVec4 darkColourUniform;

  public ShaderOptionsParchment(final Shader<ShaderOptionsParchment>.UniformFloat modelIndex, final Shader<ShaderOptionsParchment>.UniformFloat discardTranslucency, final Shader<ShaderOptionsParchment>.UniformVec4 lightColourUniform, final Shader<ShaderOptionsParchment>.UniformVec4 darkColourUniform) {
    super(modelIndex, discardTranslucency);
    this.lightColourUniform = lightColourUniform;
    this.darkColourUniform = darkColourUniform;
  }

  public ShaderOptionsParchment lightColour(final Vector4f recolour) {
    this.lightColourUniform.set(recolour);
    return this;
  }

  public ShaderOptionsParchment lightColour(final float r, final float g, final float b, final float a) {
    this.lightColourUniform.set(r, g, b, a);
    return this;
  }

  public ShaderOptionsParchment lightColour(final float monochrome) {
    this.lightColourUniform.set(monochrome, monochrome, monochrome, 1.0f);
    return this;
  }

  public ShaderOptionsParchment darkColour(final Vector4f recolour) {
    this.darkColourUniform.set(recolour);
    return this;
  }

  public ShaderOptionsParchment darkColour(final float r, final float g, final float b, final float a) {
    this.darkColourUniform.set(r, g, b, a);
    return this;
  }

  public ShaderOptionsParchment darkColour(final float monochrome) {
    this.darkColourUniform.set(monochrome, monochrome, monochrome, 1.0f);
    return this;
  }
}
