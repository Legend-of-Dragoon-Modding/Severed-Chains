package legend.core.opengl.fonts;

import legend.core.opengl.Shader;

public interface TextStreamable {
  void delete();
  float width();
  float draw(final Shader.UniformVec3 colourUniform);
}
