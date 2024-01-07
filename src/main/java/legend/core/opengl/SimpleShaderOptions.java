package legend.core.opengl;

import org.joml.Vector2f;
import org.joml.Vector4f;

public class SimpleShaderOptions implements ShaderOptions<SimpleShaderOptions> {
  private final Shader<SimpleShaderOptions>.UniformVec2 shiftUvUniform;
  private final Shader<SimpleShaderOptions>.UniformVec4 recolourUniform;

  public SimpleShaderOptions(final Shader<SimpleShaderOptions>.UniformVec2 shiftUvUniform, final Shader<SimpleShaderOptions>.UniformVec4 recolourUniform) {
    this.shiftUvUniform = shiftUvUniform;
    this.recolourUniform = recolourUniform;
  }

  public SimpleShaderOptions shiftUv(final Vector2f shiftUv) {
    this.shiftUvUniform.set(shiftUv);
    return this;
  }

  public SimpleShaderOptions shiftUv(final float u, final float v) {
    this.shiftUvUniform.set(u, v);
    return this;
  }

  public SimpleShaderOptions recolour(final Vector4f recolour) {
    this.recolourUniform.set(recolour);
    return this;
  }

  public SimpleShaderOptions recolour(final float r, final float g, final float b, final float a) {
    this.recolourUniform.set(r, g, b, a);
    return this;
  }

  public SimpleShaderOptions recolour(final float monochrome) {
    this.recolourUniform.set(monochrome, monochrome, monochrome, 1.0f);
    return this;
  }
}
