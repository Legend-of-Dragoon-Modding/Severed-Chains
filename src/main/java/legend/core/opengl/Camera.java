package legend.core.opengl;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.nio.FloatBuffer;

public class Camera {
  private final Matrix4f view = new Matrix4f();
  private final Vector3f pos = new Vector3f();

  public Camera(final float x, final float y) {
    this.moveTo(x, y);
  }

  public void moveTo(final float x, final float y) {
    this.pos.set(x, y, 0.0f);
    this.update();
  }

  private void update() {
    this.view.translation(this.pos);
  }

  public void get(final FloatBuffer buffer) {
    this.view.get(buffer);
  }
}
