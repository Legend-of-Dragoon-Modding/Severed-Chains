package legend.core.renderer;

import org.joml.Matrix4f;

public class CopyShaderOptions implements ShaderOptions {
  private final ShaderUniformMat4 projectionUniform;

  public final Matrix4f projection = new Matrix4f();

  public CopyShaderOptions(final ShaderUniformMat4 projectionUniform) {
    this.projectionUniform = projectionUniform;
  }

  @Override
  public void apply() {
    this.projectionUniform.set(this.projection);
  }
}
