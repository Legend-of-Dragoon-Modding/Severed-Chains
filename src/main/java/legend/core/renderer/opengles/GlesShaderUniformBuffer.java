package legend.core.renderer.opengles;

import legend.core.renderer.ShaderUniformBuffer;

import java.nio.FloatBuffer;

import static org.lwjgl.opengles.GLES20.GL_DYNAMIC_DRAW;
import static org.lwjgl.opengles.GLES20.glBindBuffer;
import static org.lwjgl.opengles.GLES20.glBufferData;
import static org.lwjgl.opengles.GLES20.glBufferSubData;
import static org.lwjgl.opengles.GLES20.glDeleteBuffers;
import static org.lwjgl.opengles.GLES20.glGenBuffers;
import static org.lwjgl.opengles.GLES30.GL_UNIFORM_BUFFER;
import static org.lwjgl.opengles.GLES30.glBindBufferBase;

public class GlesShaderUniformBuffer implements ShaderUniformBuffer {
  private final int id;

  GlesShaderUniformBuffer(final long size, final int binding) {
    this.id = glGenBuffers();

    glBindBuffer(GL_UNIFORM_BUFFER, this.id);
    glBufferData(GL_UNIFORM_BUFFER, size, GL_DYNAMIC_DRAW);
    glBindBuffer(GL_UNIFORM_BUFFER, 0);

    glBindBufferBase(GL_UNIFORM_BUFFER, binding, this.id);
  }

  @Override
  public void delete() {
    glDeleteBuffers(this.id);
  }

  @Override
  public void set(final FloatBuffer buffer) {
    this.set(0L, buffer);
  }

  @Override
  public void set(final long offset, final FloatBuffer buffer) {
    glBindBuffer(GL_UNIFORM_BUFFER, this.id);
    glBufferSubData(GL_UNIFORM_BUFFER, offset, buffer);
    glBindBuffer(GL_UNIFORM_BUFFER, 0);
  }
}
