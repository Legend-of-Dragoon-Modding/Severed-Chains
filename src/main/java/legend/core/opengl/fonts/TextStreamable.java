package legend.core.opengl.fonts;

import legend.core.opengl.FontShaderOptions;

public interface TextStreamable {
  void delete();
  float width();
  float draw(final FontShaderOptions shaderOptions);
}
