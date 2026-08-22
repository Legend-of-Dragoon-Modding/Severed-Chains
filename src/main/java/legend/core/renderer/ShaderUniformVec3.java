package legend.core.renderer;

import org.joml.Vector3fc;

import java.nio.FloatBuffer;

public interface ShaderUniformVec3 {
  void set(FloatBuffer buffer);
  void set(Vector3fc vec);
  void set(float x, float y, float z);
}
