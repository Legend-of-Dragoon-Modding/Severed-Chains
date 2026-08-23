package legend.core.renderer.noop;

import legend.core.renderer.Shader;
import legend.core.renderer.ShaderOptions;
import legend.core.renderer.ShaderUniformFloat;
import legend.core.renderer.ShaderUniformInt;
import legend.core.renderer.ShaderUniformMat4;
import legend.core.renderer.ShaderUniformVec2;
import legend.core.renderer.ShaderUniformVec3;
import legend.core.renderer.ShaderUniformVec4;
import org.joml.Matrix4fc;
import org.joml.Vector2fc;
import org.joml.Vector3fc;
import org.joml.Vector4fc;

import java.nio.FloatBuffer;
import java.util.function.Function;
import java.util.function.Supplier;

public class NoopShader<Options extends ShaderOptions> implements Shader<Options> {
  private final Supplier<Options> options;

  NoopShader(final Function<Shader<Options>, Supplier<Options>> options) {
    this.options = options.apply(this);
  }

  @Override
  public void reload() {

  }

  @Override
  public Options makeOptions() {
    return this.options.get();
  }

  @Override
  public void bindUniformBlock(final CharSequence name, final int binding) {

  }

  @Override
  public void use() {

  }

  @Override
  public void delete() {

  }

  @Override
  public ShaderUniformVec2 uniformVec2(final String name) {
    return new UniformVec2();
  }

  @Override
  public ShaderUniformVec3 uniformVec3(final String name) {
    return new UniformVec3();
  }

  @Override
  public ShaderUniformVec4 uniformVec4(final String name) {
    return new UniformVec4();
  }

  @Override
  public ShaderUniformMat4 uniformMat4(final String name) {
    return new UniformMat4();
  }

  @Override
  public ShaderUniformInt uniformInt(final String name) {
    return new UniformInt();
  }

  @Override
  public ShaderUniformFloat uniformFloat(final String name) {
    return new UniformFloat();
  }

  public static class UniformVec2 implements ShaderUniformVec2 {
    @Override
    public void set(final FloatBuffer buffer) {

    }

    @Override
    public void set(final Vector2fc vec) {

    }

    @Override
    public void set(final float x, final float y) {

    }
  }

  public static class UniformVec3 implements ShaderUniformVec3 {
    @Override
    public void set(final FloatBuffer buffer) {

    }

    @Override
    public void set(final Vector3fc vec) {

    }

    @Override
    public void set(final float x, final float y, final float z) {

    }
  }

  public static class UniformVec4 implements ShaderUniformVec4 {
    @Override
    public void set(final FloatBuffer buffer) {

    }

    @Override
    public void set(final Vector4fc vec) {

    }

    @Override
    public void set(final float x, final float y, final float z, final float w) {

    }
  }

  public static class UniformMat4 implements ShaderUniformMat4 {
    @Override
    public void set(final Matrix4fc mat) {

    }

    @Override
    public void set(final Matrix4fc mat, final boolean transpose) {

    }

    @Override
    public void set(final FloatBuffer mat) {

    }

    @Override
    public void set(final FloatBuffer mat, final boolean transpose) {

    }
  }

  public static class UniformInt implements ShaderUniformInt {
    @Override
    public void set(final int val) {

    }
  }

  public static class UniformFloat implements ShaderUniformFloat {
    @Override
    public void set(final float val) {

    }
  }
}
