package legend.core.opengl;

import legend.game.types.Translucency;

public class ShaderOptionsStandard extends ShaderOptionsBase<ShaderOptionsStandard> {
  private final Shader<ShaderOptionsStandard>.UniformFloat translucency;
  private final Shader<ShaderOptionsStandard>.UniformFloat alpha;

  public ShaderOptionsStandard(final Shader<ShaderOptionsStandard>.UniformFloat modelIndex, final Shader<ShaderOptionsStandard>.UniformVec3 colourUniform, final Shader<ShaderOptionsStandard>.UniformVec2 uvOffsetUniform, final Shader<ShaderOptionsStandard>.UniformVec2 clutUniform, final Shader<ShaderOptionsStandard>.UniformVec2 tpageUniform, final Shader<ShaderOptionsStandard>.UniformFloat discardTranslucency, final Shader<ShaderOptionsStandard>.UniformFloat translucency, final Shader<ShaderOptionsStandard>.UniformFloat alpha) {
    super(modelIndex, colourUniform, uvOffsetUniform, clutUniform, tpageUniform, discardTranslucency);
    this.translucency = translucency;
    this.alpha = alpha;
  }

  public ShaderOptionsStandard opaque() {
    this.translucency.set(0);
    return this;
  }

  public ShaderOptionsStandard translucency(final Translucency translucency) {
    this.translucency.set(translucency.ordinal() + 1);
    return this;
  }

  public ShaderOptionsStandard alpha(final float alpha) {
    this.alpha.set(alpha);
    return this;
  }
}
