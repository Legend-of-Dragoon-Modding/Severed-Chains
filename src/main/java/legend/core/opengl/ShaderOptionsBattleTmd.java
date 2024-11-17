package legend.core.opengl;

import org.joml.Vector3f;

public class ShaderOptionsBattleTmd extends ShaderOptionsBase<ShaderOptionsBattleTmd> {
  private final Shader<ShaderOptionsBattleTmd>.UniformInt tmdTranslucency;
  private final Shader<ShaderOptionsBattleTmd>.UniformInt ctmdFlags;
  private final Shader<ShaderOptionsBattleTmd>.UniformVec3 battleColour;
  private final Shader<ShaderOptionsBattleTmd>.UniformInt useVdf;

  public ShaderOptionsBattleTmd(final Shader<ShaderOptionsBattleTmd>.UniformFloat modelIndex, final Shader<ShaderOptionsBattleTmd>.UniformVec3 colourUniform, final Shader<ShaderOptionsBattleTmd>.UniformVec2 uvOffsetUniform, final Shader<ShaderOptionsBattleTmd>.UniformVec2 clutUniform, final Shader<ShaderOptionsBattleTmd>.UniformVec2 tpageUniform, final Shader<ShaderOptionsBattleTmd>.UniformFloat discardTranslucency, final Shader<ShaderOptionsBattleTmd>.UniformInt tmdTranslucency, final Shader<ShaderOptionsBattleTmd>.UniformInt ctmdFlags, final Shader<ShaderOptionsBattleTmd>.UniformVec3 battleColour, final Shader<ShaderOptionsBattleTmd>.UniformInt useVdf) {
    super(modelIndex, colourUniform, uvOffsetUniform, clutUniform, tpageUniform, discardTranslucency);
    this.tmdTranslucency = tmdTranslucency;
    this.ctmdFlags = ctmdFlags;
    this.battleColour = battleColour;
    this.useVdf = useVdf;
  }

  public ShaderOptionsBattleTmd tmdTranslucency(final int translucency) {
    this.tmdTranslucency.set(translucency);
    return this;
  }

  public ShaderOptionsBattleTmd ctmdFlags(final int flags) {
    this.ctmdFlags.set(flags);
    return this;
  }

  public ShaderOptionsBattleTmd battleColour(final Vector3f colour) {
    this.battleColour.set(colour);
    return this;
  }

  public ShaderOptionsBattleTmd useVdf(final boolean useVdf) {
    this.useVdf.set(useVdf ? 1 : 0);
    return this;
  }
}
