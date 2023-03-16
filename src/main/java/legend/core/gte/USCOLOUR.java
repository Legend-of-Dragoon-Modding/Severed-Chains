package legend.core.gte;

import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.UnsignedShortRef;

import javax.annotation.Nullable;

public class USCOLOUR implements MemoryRef {
  @Nullable
  private final Value ref;

  public final UnsignedShortRef x;
  public final UnsignedShortRef y;
  public final UnsignedShortRef z;

  public USCOLOUR() {
    this.ref = null;
    this.x = new UnsignedShortRef();
    this.y = new UnsignedShortRef();
    this.z = new UnsignedShortRef();
  }

  public USCOLOUR(final Value ref) {
    this.ref = ref;
    this.x = new UnsignedShortRef(ref.offset(2, 0x0L));
    this.y = new UnsignedShortRef(ref.offset(2, 0x2L));
    this.z = new UnsignedShortRef(ref.offset(2, 0x4L));
  }

  public USCOLOUR set(final USCOLOUR other) {
    this.setX(other.getX());
    this.setY(other.getY());
    this.setZ(other.getZ());
    return this;
  }

  public USCOLOUR set(final VECTOR other) {
    this.setX(other.getX());
    this.setY(other.getY());
    this.setZ(other.getZ());
    return this;
  }

  public USCOLOUR set(final int x, final int y, final int z) {
    return this.setX(x).setY(y).setZ(z);
  }

  public int get(final int element) {
    return switch(element) {
      case 0 -> this.getX();
      case 1 -> this.getY();
      case 2 -> this.getZ();
      default -> throw new IllegalArgumentException("Invalid element");
    };
  }

  public UnsignedShortRef component(final int element) {
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

  public USCOLOUR setX(final int x) {
    this.x.set(x);
    return this;
  }

  public int getY() {
    return this.y.get();
  }

  public USCOLOUR setY(final int y) {
    this.y.set(y);
    return this;
  }

  public long getXY() {
    return (this.y.get() & 0xffffL) << 16 | this.x.get() & 0xffffL;
  }

  public USCOLOUR setXY(final long xy) {
    this.setX((int)xy);
    this.setY((int)(xy >>> 16));
    return this;
  }

  public int getZ() {
    return this.z.get();
  }

  public USCOLOUR setZ(final int z) {
    this.z.set(z);
    return this;
  }

  public USCOLOUR add(final VECTOR other) {
    this.x.add(other.x.get());
    this.y.add(other.y.get());
    this.z.add(other.z.get());
    return this;
  }

  public USCOLOUR add(final USCOLOUR other) {
    this.x.add(other.x);
    this.y.add(other.y);
    this.z.add(other.z);
    return this;
  }

  public USCOLOUR add(final int value) {
    this.x.add(value);
    this.y.add(value);
    this.z.add(value);
    return this;
  }

  public USCOLOUR add(final int x, final int y, final int z) {
    this.x.add(x);
    this.y.add(y);
    this.z.add(z);
    return this;
  }

  public USCOLOUR sub(final VECTOR other) {
    this.x.sub(other.x.get());
    this.y.sub(other.y.get());
    this.z.sub(other.z.get());
    return this;
  }

  public USCOLOUR sub(final USCOLOUR other) {
    this.x.sub(other.x);
    this.y.sub(other.y);
    this.z.sub(other.z);
    return this;
  }

  public USCOLOUR sub(final int value) {
    this.x.sub(value);
    this.y.sub(value);
    this.z.sub(value);
    return this;
  }

  public USCOLOUR sub(final int x, final int y, final int z) {
    this.x.sub(x);
    this.y.sub(y);
    this.z.sub(z);
    return this;
  }

  public USCOLOUR mul(final VECTOR amount) {
    this.x.mul(amount.getX());
    this.y.mul(amount.getY());
    this.z.mul(amount.getZ());
    return this;
  }

  public USCOLOUR mul(final USCOLOUR amount) {
    this.x.mul(amount.getX());
    this.y.mul(amount.getY());
    this.z.mul(amount.getZ());
    return this;
  }

  public USCOLOUR mul(final int amount) {
    this.x.mul(amount);
    this.y.mul(amount);
    this.z.mul(amount);
    return this;
  }

  public USCOLOUR mul(final int x, final int y, final int z) {
    this.x.mul(x);
    this.y.mul(y);
    this.z.mul(z);
    return this;
  }

  public USCOLOUR div(final VECTOR amount) {
    this.x.div(amount.getX());
    this.y.div(amount.getY());
    this.z.div(amount.getZ());
    return this;
  }

  public USCOLOUR div(final USCOLOUR amount) {
    this.x.div(amount.getX());
    this.y.div(amount.getY());
    this.z.div(amount.getZ());
    return this;
  }

  public USCOLOUR div(final int divisor) {
    this.x.div(divisor);
    this.y.div(divisor);
    this.z.div(divisor);
    return this;
  }

  public USCOLOUR div(final int x, final int y, final int z) {
    this.x.div(x);
    this.y.div(y);
    this.z.div(z);
    return this;
  }

  public USCOLOUR mod(final VECTOR divisor) {
    this.x.mod(divisor.x.get());
    this.y.mod(divisor.y.get());
    this.z.mod(divisor.z.get());
    return this;
  }

  public USCOLOUR mod(final USCOLOUR divisor) {
    this.x.mod(divisor.x.get());
    this.y.mod(divisor.y.get());
    this.z.mod(divisor.z.get());
    return this;
  }

  public USCOLOUR mod(final int divisor) {
    this.x.mod(divisor);
    this.y.mod(divisor);
    this.z.mod(divisor);
    return this;
  }

  public USCOLOUR mod(final int x, final int y, final int z) {
    this.x.mod(x);
    this.y.mod(y);
    this.z.mod(z);
    return this;
  }

  public USCOLOUR negate() {
    this.x.set(-this.x.get());
    this.y.set(-this.y.get());
    this.z.set(-this.z.get());
    return this;
  }

  public USCOLOUR shr(final int amount) {
    this.x.shr(amount);
    this.y.shr(amount);
    this.z.shr(amount);
    return this;
  }

  public USCOLOUR shra(final int amount) {
    this.x.shra(amount);
    this.y.shra(amount);
    this.z.shra(amount);
    return this;
  }

  public USCOLOUR shl(final int amount) {
    this.x.shl(amount);
    this.y.shl(amount);
    this.z.shl(amount);
    return this;
  }

  public USCOLOUR and(final int value) {
    this.x.and(value);
    this.y.and(value);
    this.z.and(value);
    return this;
  }

  public USCOLOUR or(final int value) {
    this.x.or(value);
    this.y.or(value);
    this.z.or(value);
    return this;
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
    return "USCOLOUR {x: " + this.x + ", y: " + this.y + ", z: " + this.z + '}' + (this.ref == null ? " (local)" : " @ " + Long.toHexString(this.getAddress()));
  }
}
