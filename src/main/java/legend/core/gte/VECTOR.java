package legend.core.gte;

import legend.core.memory.Value;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;
import org.joml.Vector3f;

import javax.annotation.Nullable;

public class VECTOR implements MemoryRef {
  @Nullable
  private final Value ref;

  public final IntRef x;
  public final IntRef y;
  public final IntRef z;

  public VECTOR() {
    this.ref = null;
    this.x = new IntRef();
    this.y = new IntRef();
    this.z = new IntRef();
  }

  public VECTOR(final int x, final int y, final int z) {
    this();
    this.set(x, y, z);
  }

  public VECTOR(final Vector3f other) {
    this();
    this.set(other);
  }

  public VECTOR(final Value ref) {
    this.ref = ref;
    this.x = new IntRef(ref.offset(4, 0x0L));
    this.y = new IntRef(ref.offset(4, 0x4L));
    this.z = new IntRef(ref.offset(4, 0x8L));
  }

  /** NOTE: does NOT set pad */
  public VECTOR set(final Vector3f other) {
    this.setX((int)other.x);
    this.setY((int)other.y);
    this.setZ((int)other.z);
    return this;
  }

  /** NOTE: does NOT set pad */
  public VECTOR set(final VECTOR other) {
    this.setX(other.getX());
    this.setY(other.getY());
    this.setZ(other.getZ());
    return this;
  }

  /** NOTE: does NOT set pad */
  public VECTOR set(final SVECTOR other) {
    this.setX(other.getX());
    this.setY(other.getY());
    this.setZ(other.getZ());
    return this;
  }

  /** NOTE: does NOT set pad */
  public VECTOR set(final USCOLOUR other) {
    this.setX(other.getX());
    this.setY(other.getY());
    this.setZ(other.getZ());
    return this;
  }

  /** NOTE: does NOT set pad */
  public VECTOR set(final BVEC4 other) {
    this.setX(other.getX());
    this.setY(other.getY());
    this.setZ(other.getZ());
    return this;
  }

  /** NOTE: does NOT set pad */
  public VECTOR set(final int x, final int y, final int z) {
    this.setX(x);
    this.setY(y);
    this.setZ(z);
    return this;
  }

  public int get(final int element) {
    return switch(element) {
      case 0 -> this.getX();
      case 1 -> this.getY();
      case 2 -> this.getZ();
      default -> throw new IllegalArgumentException("Invalid element");
    };
  }

  public void get(final Vector3f dest) {
    dest.set(this.getX(), this.getY(), this.getZ());
  }

  public IntRef component(final int element) {
    return switch(element) {
      case 0 -> this.x;
      case 1 -> this.y;
      case 2 -> this.z;
      default -> throw new IllegalArgumentException("Invalid element");
    };
  }

  public int getX() {
    return this.x.get();
  }

  public void setX(final int x) {
    this.x.set(x);
  }

  public int getY() {
    return this.y.get();
  }

  public void setY(final int y) {
    this.y.set(y);
  }

  public int getZ() {
    return this.z.get();
  }

  public void setZ(final int z) {
    this.z.set(z);
  }

  public VECTOR add(final VECTOR other) {
    this.x.add(other.x);
    this.y.add(other.y);
    this.z.add(other.z);
    return this;
  }

  public VECTOR add(final SVECTOR other) {
    this.x.add(other.x.get());
    this.y.add(other.y.get());
    this.z.add(other.z.get());
    return this;
  }

  public VECTOR add(final int value) {
    this.x.add(value);
    this.y.add(value);
    this.z.add(value);
    return this;
  }

  public VECTOR add(final int x, final int y, final int z) {
    this.x.add(x);
    this.y.add(y);
    this.z.add(z);
    return this;
  }

  public VECTOR sub(final VECTOR other) {
    this.x.sub(other.x);
    this.y.sub(other.y);
    this.z.sub(other.z);
    return this;
  }

  public VECTOR sub(final Vector3f other) {
    this.x.sub((int)other.x);
    this.y.sub((int)other.y);
    this.z.sub((int)other.z);
    return this;
  }

  public VECTOR sub(final SVECTOR other) {
    this.x.sub(other.x.get());
    this.y.sub(other.y.get());
    this.z.sub(other.z.get());
    return this;
  }

  public VECTOR sub(final USCOLOUR other) {
    this.x.sub(other.x.get());
    this.y.sub(other.y.get());
    this.z.sub(other.z.get());
    return this;
  }

  public VECTOR sub(final int value) {
    this.x.sub(value);
    this.y.sub(value);
    this.z.sub(value);
    return this;
  }

  public VECTOR sub(final int x, final int y, final int z) {
    this.x.sub(x);
    this.y.sub(y);
    this.z.sub(z);
    return this;
  }

  public VECTOR mul(final VECTOR value) {
    this.x.mul(value.x);
    this.y.mul(value.y);
    this.z.mul(value.z);
    return this;
  }

  public VECTOR mul(final SVECTOR value) {
    this.x.mul(value.x.get());
    this.y.mul(value.y.get());
    this.z.mul(value.z.get());
    return this;
  }

  public VECTOR mul(final int value) {
    this.x.mul(value);
    this.y.mul(value);
    this.z.mul(value);
    return this;
  }

  public VECTOR mul(final int x, final int y, final int z) {
    this.x.mul(x);
    this.y.mul(y);
    this.z.mul(z);
    return this;
  }

  public VECTOR div(final VECTOR divisor) {
    this.x.div(divisor.x);
    this.y.div(divisor.y);
    this.z.div(divisor.z);
    return this;
  }

  public VECTOR div(final SVECTOR divisor) {
    this.x.div(divisor.x.get());
    this.y.div(divisor.y.get());
    this.z.div(divisor.z.get());
    return this;
  }

  public VECTOR div(final int divisor) {
    this.x.div(divisor);
    this.y.div(divisor);
    this.z.div(divisor);
    return this;
  }

  public VECTOR div(final int x, final int y, final int z) {
    this.x.div(x);
    this.y.div(y);
    this.z.div(z);
    return this;
  }

  public VECTOR mod(final VECTOR divisor) {
    this.x.mod(divisor.x);
    this.y.mod(divisor.y);
    this.z.mod(divisor.z);
    return this;
  }

  public VECTOR mod(final SVECTOR divisor) {
    this.x.mod(divisor.x.get());
    this.y.mod(divisor.y.get());
    this.z.mod(divisor.z.get());
    return this;
  }

  public VECTOR mod(final int divisor) {
    this.x.mod(divisor);
    this.y.mod(divisor);
    this.z.mod(divisor);
    return this;
  }

  public VECTOR mod(final int x, final int y, final int z) {
    this.x.mod(x);
    this.y.mod(y);
    this.z.mod(z);
    return this;
  }

  public VECTOR negate() {
    this.x.set(-this.x.get());
    this.y.set(-this.y.get());
    this.z.set(-this.z.get());
    return this;
  }

  public VECTOR shl(final int bits) {
    this.x.shl(bits);
    this.y.shl(bits);
    this.z.shl(bits);
    return this;
  }

  public VECTOR shra(final int bits) {
    this.x.shra(bits);
    this.y.shra(bits);
    this.z.shra(bits);
    return this;
  }

  public VECTOR and(final int val) {
    this.x.and(val);
    this.y.and(val);
    this.z.and(val);
    return this;
  }

  public int length() {
    return (int)Math.sqrt(this.lengthSquared());
  }

  public long lengthSquared() {
    return (long)this.getX() * this.getX() + this.getY() * this.getY() + this.getZ() * this.getZ();
  }

  public VECTOR normalize() {
    if(this.getX() == 0 && this.getY() == 0 && this.getZ() == 0) {
      return this;
    }

    final int length = this.length();
    return this.shl(12).div(length);
  }

  public VECTOR cross(final VECTOR right, final VECTOR out) {
    out.set(
      this.getZ() * right.getY() - this.getY() * right.getZ() >> 12,
      this.getX() * right.getZ() - this.getZ() * right.getX() >> 12,
      this.getY() * right.getX() - this.getX() * right.getY() >> 12
    );

    return out;
  }

  public VECTOR cross(final VECTOR right) {
    return this.cross(right, this);
  }

  public Vector3f toVec3() {
    return new Vector3f(this.getX(), this.getY(), this.getZ());
  }

  @Override
  public long getAddress() {
    if(this.ref == null) {
      throw new NullPointerException("Can't get address of non-heap object");
    }

    return this.ref.getAddress();
  }

  @Override
  public String toString() {
    return "VECTOR {x: " + this.x + ", y: " + this.y + ", z: " + this.z + '}' + (this.ref == null ? " (local)" : " @ " + Long.toHexString(this.getAddress()));
  }
}
