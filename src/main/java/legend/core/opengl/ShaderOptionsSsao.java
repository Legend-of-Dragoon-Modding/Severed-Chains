package legend.core.opengl;

import org.joml.Vector3f;

public class ShaderOptionsSsao implements ShaderOptions<ShaderOptionsSsao> {
  private final Shader<ShaderOptionsSsao>.UniformVec2 screenSize;
  private final Shader<ShaderOptionsSsao>.UniformVec3[] samples;

  public ShaderOptionsSsao(final Shader<ShaderOptionsSsao>.UniformVec2 screenSize, final Shader<ShaderOptionsSsao>.UniformVec3[] samples) {
    this.screenSize = screenSize;
    this.samples = samples;
  }

  public ShaderOptionsSsao screenSize(final float width, final float height) {
    this.screenSize.set(width, height);
    return this;
  }

  public ShaderOptionsSsao samples(final int index, final Vector3f sample) {
    this.samples[index].set(sample);
    return this;
  }

  @Override
  public void apply() {

  }
}
