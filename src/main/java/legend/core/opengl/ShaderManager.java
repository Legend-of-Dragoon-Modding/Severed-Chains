package legend.core.opengl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public final class ShaderManager {
  private ShaderManager() { }

  private static final Map<ShaderType, Shader> shaders = new HashMap<>();
  private static final Map<String, Shader.UniformBuffer> uniformBuffers = new HashMap<>();

  public static <Options extends ShaderOptions<Options>> Shader<Options> getShader(final ShaderType<Options> type) {
    return shaders.get(type);
  }

  public static <Options extends ShaderOptions<Options>> Shader<Options> addShader(final ShaderType<Options> type) {
    final Shader<Options> shader = type.shaderConstructor.apply(type.optionsConstructor);
    shaders.put(type, shader);
    return shader;
  }

  public static Shader.UniformBuffer getUniformBuffer(final String name) {
    return uniformBuffers.get(name);
  }

  public static Shader.UniformBuffer addUniformBuffer(final String name, final Shader.UniformBuffer uniformBuffer) {
    uniformBuffers.put(name, uniformBuffer);
    return uniformBuffer;
  }

  public static void reload() throws IOException {
    for(final Shader<?> shader : shaders.values()) {
      shader.reload();
    }
  }

  public static void delete() {
    for(final Shader<?> shader : shaders.values()) {
      shader.delete();
    }

    for(final Shader.UniformBuffer buffer : uniformBuffers.values()) {
      buffer.delete();
    }

    shaders.clear();
    uniformBuffers.clear();
  }
}
