package legend.core.opengl;

import legend.core.gte.MV;
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
  void lookAt(final float x, final float y, final float z);
  void get(final Shader.UniformMat4 uniform);
  void get(final FloatBuffer buffer);
  void get(final int index, final FloatBuffer buffer);
  void set(final MV view);
  Matrix4f getView();
  Vector3fc getPos();
  void getPos(final FloatBuffer buffer);
  void getPos(final int index, final FloatBuffer buffer);

  default void moveTo(final Vector3f pos) {
    this.moveTo(pos.x, pos.y, pos.z);
  }

  default void lookAt(final Vector3f pos) {
    this.lookAt(pos.x, pos.y, pos.z);
  }
}
