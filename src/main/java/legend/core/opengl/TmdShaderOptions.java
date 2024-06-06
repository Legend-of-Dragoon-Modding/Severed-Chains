package legend.core.opengl;

import legend.game.types.Translucency;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class TmdShaderOptions implements ShaderOptions<TmdShaderOptions> {
  private final Shader<TmdShaderOptions>.UniformFloat modelIndex;
  private final Shader<TmdShaderOptions>.UniformVec3 colourUniform;
  private final Shader<TmdShaderOptions>.UniformVec2 uvOffsetUniform;
  private final Shader<TmdShaderOptions>.UniformVec2 clutUniform;
  private final Shader<TmdShaderOptions>.UniformVec2 tpageUniform;
  private final Shader<TmdShaderOptions>.UniformFloat translucency;
  private final Shader<TmdShaderOptions>.UniformFloat discardTranslucency;
  private final Shader<TmdShaderOptions>.UniformInt tmdTranslucency;
  private final Shader<TmdShaderOptions>.UniformInt ctmdFlags;
  private final Shader<TmdShaderOptions>.UniformVec3 battleColour;
  private final Shader<TmdShaderOptions>.UniformInt useVdf;

  public TmdShaderOptions(final Shader<TmdShaderOptions>.UniformFloat modelIndex, final Shader<TmdShaderOptions>.UniformVec3 colourUniform, final Shader<TmdShaderOptions>.UniformVec2 uvOffsetUniform, final Shader<TmdShaderOptions>.UniformVec2 clutUniform, final Shader<TmdShaderOptions>.UniformVec2 tpageUniform, final Shader<TmdShaderOptions>.UniformFloat translucency, final Shader<TmdShaderOptions>.UniformFloat discardTranslucency, final Shader<TmdShaderOptions>.UniformInt tmdTranslucency, final Shader<TmdShaderOptions>.UniformInt ctmdFlags, final Shader<TmdShaderOptions>.UniformVec3 battleColour, final Shader<TmdShaderOptions>.UniformInt useVdf) {
    this.modelIndex = modelIndex;
    this.colourUniform = colourUniform;
    this.uvOffsetUniform = uvOffsetUniform;
    this.clutUniform = clutUniform;
    this.tpageUniform = tpageUniform;
    this.translucency = translucency;
    this.discardTranslucency = discardTranslucency;
    this.tmdTranslucency = tmdTranslucency;
    this.ctmdFlags = ctmdFlags;
    this.battleColour = battleColour;
    this.useVdf = useVdf;
  }

  public TmdShaderOptions modelIndex(final float modelIndex) {
    this.modelIndex.set(modelIndex);
    return this;
  }

  public TmdShaderOptions colour(final Vector3f colour) {
    this.colourUniform.set(colour);
    return this;
  }

  public TmdShaderOptions uvOffset(final Vector2f colour) {
    this.uvOffsetUniform.set(colour);
    return this;
  }

  public TmdShaderOptions clut(final Vector2f clut) {
    this.clutUniform.set(clut);
    return this;
  }

  public TmdShaderOptions tpage(final Vector2f tpage) {
    this.tpageUniform.set(tpage);
    return this;
  }

  public TmdShaderOptions opaque() {
    this.translucency.set(0);
    return this;
  }

  public TmdShaderOptions translucency(final Translucency translucency) {
    this.translucency.set(translucency.ordinal() + 1);
    return this;
  }

  public TmdShaderOptions tmdTranslucency(final int translucency) {
    this.tmdTranslucency.set(translucency);
    return this;
  }

  public TmdShaderOptions realTranslucency() {
    this.translucency.set(0xff);
    return this;
  }

  public TmdShaderOptions ctmdFlags(final int flags) {
    this.ctmdFlags.set(flags);
    return this;
  }

  public TmdShaderOptions battleColour(final Vector3f colour) {
    this.battleColour.set(colour);
    return this;
  }

  /**
   * <ul>
   *   <li>0: regular rendering, anything rendered will pass through the shader</li>
   *   <li>1: discard translucent pixels, used for rendering translucent primitives that have translucency disabled in their textures</li>
   *   <li>2: discard non-translucent pixels, used to render B+F and B-F primitives since they don't need to go through the OIT shader</li>
   * </ul>
   */
  public TmdShaderOptions discardMode(final int discardMode) {
    this.discardTranslucency.set(discardMode);
    return this;
  }

  public TmdShaderOptions useVdf(final boolean useVdf) {
    this.useVdf.set(useVdf ? 1 : 0);
    return this;
  }

  @Override
  public void apply() {

  }
}
