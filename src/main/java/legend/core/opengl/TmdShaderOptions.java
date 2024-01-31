package legend.core.opengl;

import org.joml.Vector2f;
import org.joml.Vector3f;

public class TmdShaderOptions implements ShaderOptions<TmdShaderOptions> {
  private final Shader<TmdShaderOptions>.UniformFloat modelIndex;
  private final Shader<TmdShaderOptions>.UniformVec3 colourUniform;
  private final Shader<TmdShaderOptions>.UniformVec2 uvOffsetUniform;
  private final Shader<TmdShaderOptions>.UniformVec2 clutUniform;
  private final Shader<TmdShaderOptions>.UniformVec2 tpageUniform;
  private final Shader<TmdShaderOptions>.UniformFloat discardTranslucency;

  public TmdShaderOptions(final Shader<TmdShaderOptions>.UniformFloat modelIndex, final Shader<TmdShaderOptions>.UniformVec3 colourUniform, final Shader<TmdShaderOptions>.UniformVec2 uvOffsetUniform, final Shader<TmdShaderOptions>.UniformVec2 clutUniform, final Shader<TmdShaderOptions>.UniformVec2 tpageUniform, final Shader<TmdShaderOptions>.UniformFloat discardTranslucency) {
    this.modelIndex = modelIndex;
    this.colourUniform = colourUniform;
    this.uvOffsetUniform = uvOffsetUniform;
    this.clutUniform = clutUniform;
    this.tpageUniform = tpageUniform;
    this.discardTranslucency = discardTranslucency;
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

  @Override
  public void apply() {

  }
}
