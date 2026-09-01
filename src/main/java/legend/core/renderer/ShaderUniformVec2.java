package legend.core.renderer;

import org.joml.Vector2fc;

import java.nio.FloatBuffer;

public interface ShaderUniformVec2 {
  void set(FloatBuffer buffer);
  void set(Vector2fc vec);
  void set(float x, float y);
}
