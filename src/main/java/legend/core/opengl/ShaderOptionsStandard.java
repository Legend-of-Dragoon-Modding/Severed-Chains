package legend.core.opengl;

import legend.game.types.Translucency;

public class ShaderOptionsStandard extends ShaderOptionsBase<ShaderOptionsStandard> {
  private final Shader<ShaderOptionsStandard>.UniformFloat translucency;

  public ShaderOptionsStandard(final Shader<ShaderOptionsStandard>.UniformFloat modelIndex, final Shader<ShaderOptionsStandard>.UniformVec3 colourUniform, final Shader<ShaderOptionsStandard>.UniformVec2 uvOffsetUniform, final Shader<ShaderOptionsStandard>.UniformVec2 clutUniform, final Shader<ShaderOptionsStandard>.UniformVec2 tpageUniform, final Shader<ShaderOptionsStandard>.UniformFloat discardTranslucency, final Shader<ShaderOptionsStandard>.UniformFloat translucency) {
    super(modelIndex, colourUniform, uvOffsetUniform, clutUniform, tpageUniform, discardTranslucency);
    this.translucency = translucency;
  }

  public ShaderOptionsStandard opaque() {
    this.translucency.set(0);
    return this;
  }

  public ShaderOptionsStandard translucency(final Translucency translucency) {
    this.translucency.set(translucency.ordinal() + 1);
    return this;
  }
}
