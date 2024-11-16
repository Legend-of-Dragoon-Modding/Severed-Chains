package legend.core.opengl;

import org.joml.Vector2f;
import org.joml.Vector3f;

public class ShaderOptionsBase<T extends ShaderOptionsBase<T>> implements ShaderOptions<T> {
  private final Shader<T>.UniformFloat modelIndex;
  private final Shader<T>.UniformVec3 colourUniform;
  private final Shader<T>.UniformVec2 uvOffsetUniform;
  private final Shader<T>.UniformVec2 clutUniform;
  private final Shader<T>.UniformVec2 tpageUniform;
  private final Shader<T>.UniformFloat discardTranslucency;

  public ShaderOptionsBase(final Shader<T>.UniformFloat modelIndex, final Shader<T>.UniformVec3 colourUniform, final Shader<T>.UniformVec2 uvOffsetUniform, final Shader<T>.UniformVec2 clutUniform, final Shader<T>.UniformVec2 tpageUniform, final Shader<T>.UniformFloat discardTranslucency) {
    this.modelIndex = modelIndex;
    this.colourUniform = colourUniform;
    this.uvOffsetUniform = uvOffsetUniform;
    this.clutUniform = clutUniform;
    this.tpageUniform = tpageUniform;
    this.discardTranslucency = discardTranslucency;
  }

  public ShaderOptionsBase<T> modelIndex(final float modelIndex) {
    this.modelIndex.set(modelIndex);
    return this;
  }

  public ShaderOptionsBase<T> colour(final Vector3f colour) {
    this.colourUniform.set(colour);
    return this;
  }

  public ShaderOptionsBase<T> uvOffset(final Vector2f colour) {
    this.uvOffsetUniform.set(colour);
    return this;
  }

  public ShaderOptionsBase<T> clut(final Vector2f clut) {
    this.clutUniform.set(clut);
    return this;
  }

  public ShaderOptionsBase<T> tpage(final Vector2f tpage) {
    this.tpageUniform.set(tpage);
    return this;
  }

  /**
   * <ul>
   *   <li>0: regular rendering, anything rendered will pass through the shader</li>
   *   <li>1: discard translucent pixels, used for rendering translucent primitives that have translucency disabled in their textures</li>
   *   <li>2: discard opaque pixels</li>
   * </ul>
   */
  public ShaderOptionsBase<T> discardMode(final int discardMode) {
    this.discardTranslucency.set(discardMode);
    return this;
  }

  @Override
  public void apply() {

  }
}
