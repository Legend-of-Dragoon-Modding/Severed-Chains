package legend.core.renderer;

import java.nio.FloatBuffer;

public interface ShaderUniformBuffer {
  int TRANSFORM = 0;
  int TRANSFORM2 = 1;
  int LIGHTING = 2;
  int PROJECTION_INFO = 3;
  int SCISSOR = 4;
  int CLUT_ANIMATION = 5;

  void delete();
  void set(FloatBuffer buffer);
  void set(long offset, FloatBuffer buffer);
}
