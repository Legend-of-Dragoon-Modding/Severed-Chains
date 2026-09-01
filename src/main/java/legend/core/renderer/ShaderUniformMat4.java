package legend.core.renderer;

import org.joml.Matrix4fc;

import java.nio.FloatBuffer;

public interface ShaderUniformMat4 {
  void set(Matrix4fc mat);
  void set(Matrix4fc mat, boolean transpose);
  void set(FloatBuffer mat);
  void set(FloatBuffer mat, boolean transpose);
}
