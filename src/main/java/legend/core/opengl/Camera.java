package legend.core.opengl;

import org.joml.Matrix4f;
import org.joml.Vector3f;
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
  void lookAt(final Vector3f position, final Vector3f reference);
  void get(final FloatBuffer buffer);
  void get(final int index, final FloatBuffer buffer);
  Matrix4f getView();
  Vector3fc getPos();
}
