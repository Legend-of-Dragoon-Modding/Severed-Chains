package legend.core.renderer;

import java.io.IOException;

public interface Shader<Options extends ShaderOptions> {
  void reload() throws IOException;
  Options makeOptions();
  void bindUniformBlock(CharSequence name, int binding);
  void use();
  void delete();

  ShaderUniformVec2 uniformVec2(final String name);
  ShaderUniformVec3 uniformVec3(final String name);
  ShaderUniformVec4 uniformVec4(final String name);
  ShaderUniformMat4 uniformMat4(final String name);
  ShaderUniformInt uniformInt(final String name);
  ShaderUniformFloat uniformFloat(final String name);
}
