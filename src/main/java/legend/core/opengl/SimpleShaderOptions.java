package legend.core.opengl;

import org.joml.Vector2f;
import org.joml.Vector4f;

public class SimpleShaderOptions implements ShaderOptions<SimpleShaderOptions> {
  private final Shader<SimpleShaderOptions>.UniformVec2 shiftUvUniform;
  private final Shader<SimpleShaderOptions>.UniformVec4 recolourUniform;

  private final Vector2f shiftUv = new Vector2f();
  private final Vector4f recolour = new Vector4f();

  public SimpleShaderOptions(final Shader<SimpleShaderOptions>.UniformVec2 shiftUvUniform, final Shader<SimpleShaderOptions>.UniformVec4 recolourUniform) {
    this.shiftUvUniform = shiftUvUniform;
    this.recolourUniform = recolourUniform;
  }

  public SimpleShaderOptions shiftUv(final Vector2f shiftUv) {
    this.shiftUv.set(shiftUv);
    return this;
  }

  public SimpleShaderOptions shiftUv(final float u, final float v) {
    this.shiftUv.set(u, v);
    return this;
  }

  public SimpleShaderOptions recolour(final Vector4f recolour) {
    this.recolour.set(this.recolour);
    return this;
  }

  public SimpleShaderOptions recolour(final float r, final float g, final float b, final float a) {
    this.recolour.set(r, g, b, a);
    return this;
  }

  public SimpleShaderOptions recolour(final float monochrome) {
    this.recolour.set(monochrome, monochrome, monochrome, 1.0f);
    return this;
  }

  @Override
  public void apply() {
    this.shiftUvUniform.set(this.shiftUv);
    this.recolourUniform.set(this.recolour);
  }
}
