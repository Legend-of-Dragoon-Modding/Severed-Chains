package legend.core.opengl;

import java.util.function.Function;
import java.util.function.Supplier;

public class ShaderType<Options extends ShaderOptions<Options>>  {
  public final Function<Function<Shader<Options>, Supplier<Options>>, Shader<Options>> shaderConstructor;
  public final Function<Shader<Options>, Supplier<Options>> optionsConstructor;

  public ShaderType(final Function<Function<Shader<Options>, Supplier<Options>>, Shader<Options>> shaderConstructor, final Function<Shader<Options>, Supplier<Options>> optionsConstructor) {
    this.shaderConstructor = shaderConstructor;
    this.optionsConstructor = optionsConstructor;
  }
}
