package legend.core.renderer.opengl;

import legend.core.renderer.ShaderUniformBuffer;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL15C.GL_DYNAMIC_DRAW;
import static org.lwjgl.opengl.GL15C.glBindBuffer;
import static org.lwjgl.opengl.GL15C.glBufferData;
import static org.lwjgl.opengl.GL15C.glBufferSubData;
import static org.lwjgl.opengl.GL15C.glDeleteBuffers;
import static org.lwjgl.opengl.GL15C.glGenBuffers;
import static org.lwjgl.opengl.GL30C.glBindBufferBase;
import static org.lwjgl.opengl.GL31C.GL_UNIFORM_BUFFER;

public class GlShaderUniformBuffer implements ShaderUniformBuffer {
  private final int id;

  GlShaderUniformBuffer(final long size, final int binding) {
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
