package legend.core.renderer;

import org.joml.Vector4fc;

import java.nio.FloatBuffer;

public interface ShaderUniformVec4 {
  void set(FloatBuffer buffer);
  void set(Vector4fc vec);
  void set(float x, float y, float z, float w);
}
