package legend.core.opengl;

import java.util.HashMap;
import java.util.Map;

public final class ShaderManager {
  private ShaderManager() { }

  private static final Map<String, Shader> shaders = new HashMap<>();
  private static final Map<String, Shader.UniformBuffer> uniformBuffers = new HashMap<>();

  public static Shader getShader(final String name) {
    return shaders.get(name);
  }

  public static Shader addShader(final String name, final Shader shader) {
    shaders.put(name, shader);
    return shader;
  }

  public static Shader.UniformBuffer getUniformBuffer(final String name) {
    return uniformBuffers.get(name);
  }

  public static Shader.UniformBuffer addUniformBuffer(final String name, final Shader.UniformBuffer uniformBuffer) {
    uniformBuffers.put(name, uniformBuffer);
    return uniformBuffer;
  }
}
