package legend.core.opengl;

import org.joml.Vector3f;

public class FontShaderOptions implements ShaderOptions<FontShaderOptions> {
  private final Shader<FontShaderOptions>.UniformVec3 colourUniform;

  private final Vector3f colour = new Vector3f();

  public FontShaderOptions(final Shader<FontShaderOptions>.UniformVec3 colourUniform) {
    this.colourUniform = colourUniform;
  }

  public FontShaderOptions colour(final Vector3f colour) {
    this.colour.set(colour);
    return this;
  }

  public FontShaderOptions colour(final float r, final float g, final float b) {
    this.colour.set(r, g, b);
    return this;
  }

  @Override
  public void apply() {
    this.colourUniform.set(this.colour);
  }
}
