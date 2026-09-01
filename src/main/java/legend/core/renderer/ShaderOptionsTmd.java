package legend.core.renderer;

public class ShaderOptionsTmd extends ShaderOptionsBase {
  private final ShaderUniformInt tmdTranslucency;
  private final ShaderUniformInt usePs1Depth;

  public ShaderOptionsTmd(final ShaderUniformFloat modelIndex, final ShaderUniformVec3 colourUniform, final ShaderUniformVec2 uvOffsetUniform, final ShaderUniformVec2 clutUniform, final ShaderUniformVec2 tpageUniform, final ShaderUniformFloat discardTranslucency, final ShaderUniformInt tmdTranslucency, final ShaderUniformInt usePs1Depth) {
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
