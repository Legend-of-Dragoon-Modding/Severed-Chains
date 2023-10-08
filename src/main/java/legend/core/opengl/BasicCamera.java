package legend.core.opengl;

import legend.core.gte.MV;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector3fc;

import java.nio.FloatBuffer;

public class BasicCamera implements Camera {
  private final Matrix4f view = new Matrix4f();
  private final Vector3f pos = new Vector3f();

  public BasicCamera(final float x, final float y) {
    this.moveTo(x, y, 0.0f);
  }

  @Override
  public void moveTo(final float x, final float y, final float z) {
    this.pos.set(x, y, z);
    this.update();
  }

  private void update() {
    this.view.translation(this.pos);
  }

  @Override
  public float getX() {
    return 0;
  }

  @Override
  public float getY() {
    return 0;
  }

  @Override
  public float getZ() {
    return 0;
  }

  @Override
  public void move(final float amount) {

  }

  @Override
  public void strafe(final float amount) {

  }

  @Override
  public void jump(final float amount) {

  }

  @Override
  public void look(final float yaw, final float pitch) {

  }

  @Override
  public void lookAt(final float x, final float y, final float z) {

  }

  @Override
  public void get(final Shader.UniformMat4 uniform) {

  }

  @Override
  public void get(final FloatBuffer buffer) {
    this.view.get(buffer);
  }

  @Override
  public void get(final int index, final FloatBuffer buffer) {

  }

  @Override
  public void set(final MV view) {
    this.view.set(view);
    this.view.m30(view.transfer.x);
    this.view.m31(view.transfer.y);
    this.view.m32(view.transfer.z);
  }

  @Override
  public Matrix4f getView() {
    return this.view;
  }

  @Override
  public Vector3fc getPos() {
    return null;
  }

  @Override
  public void getPos(final FloatBuffer buffer) {

  }

  @Override
  public void getPos(final int index, final FloatBuffer buffer) {

  }
}
