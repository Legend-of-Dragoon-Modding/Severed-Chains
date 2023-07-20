package legend.core.gte;

import org.joml.Matrix4f;
import org.joml.Matrix4fc;

import java.util.Arrays;

import static legend.game.Scus94491BpeSegment.rcos;
import static legend.game.Scus94491BpeSegment.sin;

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

  public MATRIX identity() {
    this.set(0, (short)0x1000);
    this.set(1, (short)0);
    this.set(2, (short)0);
    this.set(3, (short)0);
    this.set(4, (short)0x1000);
    this.set(5, (short)0);
    this.set(6, (short)0);
    this.set(7, (short)0);
    this.set(8, (short)0x1000);
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

  public MATRIX transpose() {
    final short m1 = this.data2[1];
    final short m2 = this.data2[2];
    final short m5 = this.data2[5];

    this.data2[1] = this.data2[3];
    this.data2[2] = this.data2[6];
    this.data2[3] = m1;
    this.data2[5] = this.data2[7];
    this.data2[6] = m2;
    this.data2[7] = m5;
    return this;
  }

  public MATRIX transpose(final MATRIX out) {
    out.data2[0] = this.data2[0];
    out.data2[1] = this.data2[3];
    out.data2[2] = this.data2[6];
    out.data2[3] = this.data2[1];
    out.data2[4] = this.data2[4];
    out.data2[5] = this.data2[7];
    out.data2[6] = this.data2[2];
    out.data2[7] = this.data2[5];
    out.data2[8] = this.data2[8];
    return this;
  }

  /** Rotate with parallel translation */
  public MATRIX compose(final MATRIX other, final MATRIX out) {
    final int vx = other.transfer.getX();
    final int vy = other.transfer.getY();
    final int vz = other.transfer.getZ();
    this.transfer.mul(other, out.transfer);
    out.transfer.add(vx, vy, vz);

    return this.mul(other, out);
  }

  /** Rotate with parallel translation */
  public MATRIX compose(final MATRIX rotation) {
    return this.compose(rotation, this);
  }

  public MATRIX mul(final MATRIX other, final MATRIX out) {
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

    return this;
  }

  public MATRIX mul(final MATRIX rotation) {
    return this.mul(rotation, this);
  }

  public MATRIX rotateX(final int amount) {
    final short sin = sin(amount);
    final short cos = rcos(Math.abs(amount));

    final short m10 = this.get(1, 0);
    final short m11 = this.get(1, 1);
    final short m12 = this.get(1, 2);
    final short m20 = this.get(2, 0);
    final short m21 = this.get(2, 1);
    final short m22 = this.get(2, 2);

    this.set(1, 0, (short)(cos * m10 - sin * m20 >> 12));
    this.set(1, 1, (short)(cos * m11 - sin * m21 >> 12));
    this.set(1, 2, (short)(cos * m12 - sin * m22 >> 12));
    this.set(2, 0, (short)(sin * m10 + cos * m20 >> 12));
    this.set(2, 1, (short)(sin * m11 + cos * m21 >> 12));
    this.set(2, 2, (short)(sin * m12 + cos * m22 >> 12));

    return this;
  }

  public MATRIX rotateY(final int amount) {
    final short sin = (short)-sin(amount);
    final short cos = rcos(Math.abs(amount));

    final short m0 = this.get(0);
    final short m1 = this.get(1);
    final short m2 = this.get(2);
    final short m6 = this.get(6);
    final short m7 = this.get(7);
    final short m8 = this.get(8);
    this.set(0, (short)(cos * m0 - sin * m6 >> 12));
    this.set(1, (short)(cos * m1 - sin * m7 >> 12));
    this.set(2, (short)(cos * m2 - sin * m8 >> 12));
    this.set(6, (short)(sin * m0 + cos * m6 >> 12));
    this.set(7, (short)(sin * m1 + cos * m7 >> 12));
    this.set(8, (short)(sin * m2 + cos * m8 >> 12));

    return this;
  }

  public MATRIX rotateZ(final int amount) {
    final short sin = sin(amount);
    final short cos = rcos(Math.abs(amount));

    final long m00 = this.get(0, 0);
    final long m01 = this.get(0, 1);
    final long m02 = this.get(0, 2);
    final long m10 = this.get(1, 0);
    final long m11 = this.get(1, 1);
    final long m12 = this.get(1, 2);

    this.set(0, 0, (short)(cos * m00 - sin * m10 >> 12));
    this.set(0, 1, (short)(cos * m01 - sin * m11 >> 12));
    this.set(0, 2, (short)(cos * m02 - sin * m12 >> 12));
    this.set(1, 0, (short)(sin * m00 + cos * m10 >> 12));
    this.set(1, 1, (short)(sin * m01 + cos * m11 >> 12));
    this.set(1, 2, (short)(sin * m02 + cos * m12 >> 12));

    return this;
  }

  public MATRIX scale(final VECTOR scale, final MATRIX out) {
    out.set(0, (short)(this.get(0) * scale.getX() >> 12));
    out.set(1, (short)(this.get(1) * scale.getX() >> 12));
    out.set(2, (short)(this.get(2) * scale.getX() >> 12));
    out.set(3, (short)(this.get(3) * scale.getY() >> 12));
    out.set(4, (short)(this.get(4) * scale.getY() >> 12));
    out.set(5, (short)(this.get(5) * scale.getY() >> 12));
    out.set(6, (short)(this.get(6) * scale.getZ() >> 12));
    out.set(7, (short)(this.get(7) * scale.getZ() >> 12));
    out.set(8, (short)(this.get(8) * scale.getZ() >> 12));
    return this;
  }

  public MATRIX scale(final VECTOR scale) {
    return this.scale(scale, this);
  }

  /** Dunno what the L means, but it's scaled by XYZXYZXYZ instead of XXXYYYZZZ */
  public MATRIX scaleL(final VECTOR scale, final MATRIX out) {
    out.set(0, (short)(this.get(0) * scale.getX() >> 12));
    out.set(1, (short)(this.get(1) * scale.getY() >> 12));
    out.set(2, (short)(this.get(2) * scale.getZ() >> 12));
    out.set(3, (short)(this.get(3) * scale.getX() >> 12));
    out.set(4, (short)(this.get(4) * scale.getY() >> 12));
    out.set(5, (short)(this.get(5) * scale.getZ() >> 12));
    out.set(6, (short)(this.get(6) * scale.getX() >> 12));
    out.set(7, (short)(this.get(7) * scale.getY() >> 12));
    out.set(8, (short)(this.get(8) * scale.getZ() >> 12));
    return this;
  }

  /** Dunno what the L means, but it's scaled by XYZXYZXYZ instead of XXXYYYZZZ */
  public MATRIX scaleL(final VECTOR scale) {
    return this.scaleL(scale, this);
  }

  /** Dunno what the L means, but it's scaled by XYZXYZXYZ instead of XXXYYYZZZ */
  public MATRIX scaleL(final SVECTOR scale, final MATRIX out) {
    out.set(0, (short)(this.get(0) * scale.getX() >> 12));
    out.set(1, (short)(this.get(1) * scale.getY() >> 12));
    out.set(2, (short)(this.get(2) * scale.getZ() >> 12));
    out.set(3, (short)(this.get(3) * scale.getX() >> 12));
    out.set(4, (short)(this.get(4) * scale.getY() >> 12));
    out.set(5, (short)(this.get(5) * scale.getZ() >> 12));
    out.set(6, (short)(this.get(6) * scale.getX() >> 12));
    out.set(7, (short)(this.get(7) * scale.getY() >> 12));
    out.set(8, (short)(this.get(8) * scale.getZ() >> 12));
    return this;
  }

  /** Dunno what the L means, but it's scaled by XYZXYZXYZ instead of XXXYYYZZZ */
  public MATRIX scaleL(final SVECTOR scale) {
    return this.scaleL(scale, this);
  }
}
