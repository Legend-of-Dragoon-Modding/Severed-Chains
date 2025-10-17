package legend.core.opengl;

public class ShaderOptionsTmd extends ShaderOptionsBase<ShaderOptionsTmd> {
  private final Shader<ShaderOptionsTmd>.UniformInt tmdTranslucency;
  private final Shader<ShaderOptionsTmd>.UniformInt usePs1Depth;

  public ShaderOptionsTmd(final Shader<ShaderOptionsTmd>.UniformFloat modelIndex, final Shader<ShaderOptionsTmd>.UniformVec3 colourUniform, final Shader<ShaderOptionsTmd>.UniformVec2 uvOffsetUniform, final Shader<ShaderOptionsTmd>.UniformVec2 clutUniform, final Shader<ShaderOptionsTmd>.UniformVec2 tpageUniform, final Shader<ShaderOptionsTmd>.UniformFloat discardTranslucency, final Shader<ShaderOptionsTmd>.UniformInt tmdTranslucency, final Shader<ShaderOptionsTmd>.UniformInt usePs1Depth) {
    super(modelIndex, colourUniform, uvOffsetUniform, clutUniform, tpageUniform, discardTranslucency);
    this.tmdTranslucency = tmdTranslucency;
    this.usePs1Depth = usePs1Depth;
  }

  public ShaderOptionsTmd tmdTranslucency(final int translucency) {
    this.tmdTranslucency.set(translucency);
    return this;
  }

  public ShaderOptionsTmd usePs1Depth(final boolean use) {
    this.usePs1Depth.set(use ? 1 : 0);
    return this;
  }
}
