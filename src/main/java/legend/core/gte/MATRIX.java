package legend.core.gte;

import org.joml.Matrix4f;
import org.joml.Matrix4fc;

import java.util.Arrays;

public class MATRIX {
  // 0h-11h
  private final short[] data2 = new short[9];
  // 12h-13h skipped to align
  // 14h-1fh
  public final VECTOR transfer = new VECTOR();

  public short get(final int x, final int y) {
    return this.get(x * 3 + y);
  }

  public short get(final int index) {
    return this.data2[index];
  }

  public MATRIX set(final int x, final int y, final short val) {
    this.set(x * 3 + y, val);
    return this;
  }

  public MATRIX set(final int index, final short val) {
    this.data2[index] = val;
    return this;
  }

  public MATRIX set(final MATRIX other) {
    System.arraycopy(other.data2, 0, this.data2, 0, this.data2.length);

    for(int i = 0; i < 3; i++) {
      this.transfer.set(other.transfer);
    }

    return this;
  }

  /** NOTE: does not set translation */
  public MATRIX set(final Matrix4fc other) {
    return this
      .set(0, (short)(other.m00() * 4096)).set(1, (short)(other.m10() * 4096)).set(2, (short)(other.m20() * 4096))
      .set(3, (short)(other.m01() * 4096)).set(4, (short)(other.m11() * 4096)).set(5, (short)(other.m21() * 4096))
      .set(6, (short)(other.m02() * 4096)).set(7, (short)(other.m12() * 4096)).set(8, (short)(other.m22() * 4096));
  }

  public Matrix4f toMat4f(final Matrix4f mat) {
    return mat
      .m00(this.get(0) / 4096.0f).m10(this.get(1) / 4096.0f).m20(this.get(2) / 4096.0f)
      .m01(this.get(3) / 4096.0f).m11(this.get(4) / 4096.0f).m21(this.get(5) / 4096.0f)
      .m02(this.get(6) / 4096.0f).m12(this.get(7) / 4096.0f).m22(this.get(8) / 4096.0f);
  }

  public Matrix4f toMat4f() {
    return this.toMat4f(new Matrix4f());
  }

  public MATRIX clear() {
    Arrays.fill(this.data2, (short)0);
    this.transfer.set(0, 0, 0);
    return this;
  }

  public MATRIX normalize(final MATRIX out) {
    final VECTOR column0 = new VECTOR(this.get(0), this.get(1), this.get(2));
    final VECTOR column1 = new VECTOR(this.get(3), this.get(4), this.get(5));
    final VECTOR product0 = new VECTOR();
    final VECTOR product1 = new VECTOR();

    column1.cross(column0, product0);
    product0.cross(column1, product1);

    product1.normalize();
    column1.normalize();
    product0.normalize();

    out.set(0, (short)product1.getX());
    out.set(1, (short)product1.getY());
    out.set(2, (short)product1.getZ());

    out.set(3, (short)column1.getX());
    out.set(4, (short)column1.getY());
    out.set(5, (short)column1.getZ());

    out.set(6, (short)product0.getX());
    out.set(7, (short)product0.getY());
    out.set(8, (short)product0.getZ());

    return this;
  }

  public MATRIX normalize() {
    return this.normalize(this);
  }

  /** Rotate with parallel translation */
  public MATRIX compose(final MATRIX other, final MATRIX out) {
    final int t0 = this.get(0);
    final int t1 = this.get(1);
    final int t2 = this.get(2);
    final int t3 = this.get(3);
    final int t4 = this.get(4);
    final int t5 = this.get(5);
    final int t6 = this.get(6);
    final int t7 = this.get(7);
    final int t8 = this.get(8);
    final int o0 = other.get(0);
    final int o1 = other.get(1);
    final int o2 = other.get(2);
    final int o3 = other.get(3);
    final int o4 = other.get(4);
    final int o5 = other.get(5);
    final int o6 = other.get(6);
    final int o7 = other.get(7);
    final int o8 = other.get(8);

    out.set(0, (short)(o0 * t0 + o1 * t3 + o2 * t6 >> 12));
    out.set(1, (short)(o0 * t1 + o1 * t4 + o2 * t7 >> 12));
    out.set(2, (short)(o0 * t2 + o1 * t5 + o2 * t8 >> 12));
    out.set(3, (short)(o3 * t0 + o4 * t3 + o5 * t6 >> 12));
    out.set(4, (short)(o3 * t1 + o4 * t4 + o5 * t7 >> 12));
    out.set(5, (short)(o3 * t2 + o4 * t5 + o5 * t8 >> 12));
    out.set(6, (short)(o6 * t0 + o7 * t3 + o8 * t6 >> 12));
    out.set(7, (short)(o6 * t1 + o7 * t4 + o8 * t7 >> 12));
    out.set(8, (short)(o6 * t2 + o7 * t5 + o8 * t8 >> 12));

    this.transfer.rotate(other, out.transfer);
    out.transfer.add(other.transfer);

    return this;
  }

  /** Rotate with parallel translation */
  public MATRIX compose(final MATRIX rotation) {
    return this.compose(rotation, this);
  }
}
