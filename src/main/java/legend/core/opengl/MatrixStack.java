package legend.core.opengl;

import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.nio.FloatBuffer;
import java.util.LinkedList;

public class MatrixStack {
  private final LinkedList<Matrix4f> stack = new LinkedList<>();

  public MatrixStack() {
    this.stack.push(new Matrix4f());
  }

  public void get(final FloatBuffer transformsBuffer) {
    this.stack.element().get(transformsBuffer);
  }

  public void push() {
    this.push(new Matrix4f(this.stack.element()));
  }

  public void push(final Matrix4f matrix) {
    this.stack.push(new Matrix4f(matrix));
  }

  public void push(final Matrix3f matrix) {
    this.stack.push(new Matrix4f(matrix));
  }

  public void pop() {
    this.stack.pop();
  }

  public Matrix4f top() {
    return this.stack.element();
  }

  public void translate(final Vector3f translation) {
    this.stack.element().translate(translation);
  }

  public void translate(final float x, final float y, final float z) {
    this.stack.element().translate(x, y, z);
  }

  public void rotate(final Quaternionf rotation) {
    this.stack.element().rotate(rotation);
  }
}
