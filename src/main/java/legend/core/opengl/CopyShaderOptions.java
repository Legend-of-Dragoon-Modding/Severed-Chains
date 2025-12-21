package legend.core.opengl;

import org.joml.Matrix4f;

public class CopyShaderOptions implements ShaderOptions<CopyShaderOptions> {
  private final Shader<CopyShaderOptions>.UniformMat4 projectionUniform;

  public final Matrix4f projection = new Matrix4f();

  public CopyShaderOptions(final Shader<CopyShaderOptions>.UniformMat4 projectionUniform) {
    this.projectionUniform = projectionUniform;
  }

  @Override
  public void apply() {
    this.projectionUniform.set(this.projection);
  }
}
