package legend.core.opengl;

public class ShaderOptionsTmd extends ShaderOptionsBase<ShaderOptionsTmd> {
  private final Shader<ShaderOptionsTmd>.UniformInt tmdTranslucency;

  public ShaderOptionsTmd(final Shader<ShaderOptionsTmd>.UniformFloat modelIndex, final Shader<ShaderOptionsTmd>.UniformVec3 colourUniform, final Shader<ShaderOptionsTmd>.UniformVec2 uvOffsetUniform, final Shader<ShaderOptionsTmd>.UniformVec2 clutUniform, final Shader<ShaderOptionsTmd>.UniformVec2 tpageUniform, final Shader<ShaderOptionsTmd>.UniformFloat discardTranslucency, final Shader<ShaderOptionsTmd>.UniformInt tmdTranslucency) {
    super(modelIndex, colourUniform, uvOffsetUniform, clutUniform, tpageUniform, discardTranslucency);
    this.tmdTranslucency = tmdTranslucency;
  }

  public ShaderOptionsTmd tmdTranslucency(final int translucency) {
    this.tmdTranslucency.set(translucency);
    return this;
  }
}
