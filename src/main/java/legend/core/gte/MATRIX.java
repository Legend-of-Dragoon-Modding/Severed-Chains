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
}
