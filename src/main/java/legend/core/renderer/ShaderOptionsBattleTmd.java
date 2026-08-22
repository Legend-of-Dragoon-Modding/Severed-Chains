package legend.core.renderer;

import org.joml.Vector3f;

public class ShaderOptionsBattleTmd extends ShaderOptionsBase {
  private final ShaderUniformInt tmdTranslucency;
  private final ShaderUniformInt usePs1Depth;
  private final ShaderUniformInt ctmdFlags;
  private final ShaderUniformVec3 battleColour;

  public ShaderOptionsBattleTmd(final ShaderUniformFloat modelIndex, final ShaderUniformVec3 colourUniform, final ShaderUniformVec2 uvOffsetUniform, final ShaderUniformVec2 clutUniform, final ShaderUniformVec2 tpageUniform, final ShaderUniformFloat discardTranslucency, final ShaderUniformInt tmdTranslucency, final ShaderUniformInt usePs1Depth, final ShaderUniformInt ctmdFlags, final ShaderUniformVec3 battleColour) {
    super(modelIndex, colourUniform, uvOffsetUniform, clutUniform, tpageUniform, discardTranslucency);
    this.tmdTranslucency = tmdTranslucency;
    this.usePs1Depth = usePs1Depth;
    this.ctmdFlags = ctmdFlags;
    this.battleColour = battleColour;
  }

  public ShaderOptionsBattleTmd tmdTranslucency(final int translucency) {
    this.tmdTranslucency.set(translucency);
    return this;
  }

  public ShaderOptionsBattleTmd usePs1Depth(final boolean use) {
    this.usePs1Depth.set(use ? 1 : 0);
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
}
