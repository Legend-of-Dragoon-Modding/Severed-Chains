package legend.core.opengl;

public class ShaderOptionsSsaoDepthPrepass implements ShaderOptions<ShaderOptionsSsaoDepthPrepass> {
  private final Shader<ShaderOptionsSsaoDepthPrepass>.UniformFloat modelIndex;
  private final Shader<ShaderOptionsSsaoDepthPrepass>.UniformFloat discardTranslucency;

  public ShaderOptionsSsaoDepthPrepass(final Shader<ShaderOptionsSsaoDepthPrepass>.UniformFloat modelIndex, final Shader<ShaderOptionsSsaoDepthPrepass>.UniformFloat discardTranslucency) {
    this.modelIndex = modelIndex;
    this.discardTranslucency = discardTranslucency;
  }

  public ShaderOptionsSsaoDepthPrepass modelIndex(final float modelIndex) {
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
  public ShaderOptionsSsaoDepthPrepass discardMode(final int discardMode) {
    this.discardTranslucency.set(discardMode);
    return this;
  }

  @Override
  public void apply() {

  }
}
