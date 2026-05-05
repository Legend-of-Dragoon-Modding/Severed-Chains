package legend.core.opengl;

import org.joml.Vector2f;
import org.joml.Vector3f;

public class ShaderOptionsPsx<T extends ShaderOptionsPsx<T>> extends ShaderOptionsBase<T> {
  private final Shader<T>.UniformVec3 colourUniform;
  private final Shader<T>.UniformVec2 uvOffsetUniform;
  private final Shader<T>.UniformVec2 clutUniform;
  private final Shader<T>.UniformVec2 tpageUniform;

  public ShaderOptionsPsx(final Shader<T>.UniformFloat modelIndex, final Shader<T>.UniformVec3 colourUniform, final Shader<T>.UniformVec2 uvOffsetUniform, final Shader<T>.UniformVec2 clutUniform, final Shader<T>.UniformVec2 tpageUniform, final Shader<T>.UniformFloat discardTranslucency) {
    super(modelIndex, discardTranslucency);
    this.colourUniform = colourUniform;
    this.uvOffsetUniform = uvOffsetUniform;
    this.clutUniform = clutUniform;
    this.tpageUniform = tpageUniform;
  }

  public ShaderOptionsPsx<T> colour(final Vector3f colour) {
    this.colourUniform.set(colour);
    return this;
  }

  public ShaderOptionsPsx<T> uvOffset(final Vector2f colour) {
    this.uvOffsetUniform.set(colour);
    return this;
  }

  public ShaderOptionsPsx<T> clut(final Vector2f clut) {
    this.clutUniform.set(clut);
    return this;
  }

  public ShaderOptionsPsx<T> tpage(final Vector2f tpage) {
    this.tpageUniform.set(tpage);
    return this;
  }
}
