package legend.core.opengl;

public class ShaderOptionsBase<T extends ShaderOptionsBase<T>> implements ShaderOptions<T> {
  private final Shader<T>.UniformFloat modelIndex;
  private final Shader<T>.UniformFloat discardTranslucency;

  public ShaderOptionsBase(final Shader<T>.UniformFloat modelIndex, final Shader<T>.UniformFloat discardTranslucency) {
    this.modelIndex = modelIndex;
    this.discardTranslucency = discardTranslucency;
  }

  public ShaderOptionsBase<T> modelIndex(final float modelIndex) {
    this.modelIndex.set(modelIndex);
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
