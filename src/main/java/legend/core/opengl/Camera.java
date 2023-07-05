package legend.core.opengl;

import org.joml.Vector3fc;

import java.nio.FloatBuffer;

public interface Camera {
  float getX();
  float getY();
  float getZ();
  void moveTo(final float x, final float y, final float z);
  void move(final float amount);
  void strafe(final float amount);
  void jump(final float amount);
  void look(final float yaw, final float pitch);
  void get(final Shader.UniformMat4 uniform);
  void get(final FloatBuffer buffer);
  void get(final int index, final FloatBuffer buffer);
  Vector3fc getPos();
  void getPos(final FloatBuffer buffer);
  void getPos(final int index, final FloatBuffer buffer);
}
