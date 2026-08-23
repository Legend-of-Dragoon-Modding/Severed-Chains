package legend.core.renderer.noop;

import legend.core.renderer.ShaderUniformBuffer;

import java.nio.FloatBuffer;

public class NoopShaderUniformBuffer implements ShaderUniformBuffer {
  NoopShaderUniformBuffer() {

  }

  @Override
  public void delete() {

  }

  @Override
  public void set(final FloatBuffer buffer) {

  }

  @Override
  public void set(final long offset, final FloatBuffer buffer) {

  }
}
