package legend.core.gte;

import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.ShortRef;
import org.joml.Vector3f;

import javax.annotation.Nullable;

public class SVECTOR implements MemoryRef {
  @Nullable
  private final Value ref;

  public final ShortRef x;
  public final ShortRef y;
  public final ShortRef z;

  public SVECTOR() {
    this.ref = null;
    this.x = new ShortRef();
    this.y = new ShortRef();
    this.z = new ShortRef();
  }

  public SVECTOR(final Value ref) {
    this.ref = ref;
    this.x = new ShortRef(ref.offset(2, 0x0L));
    this.y = new ShortRef(ref.offset(2, 0x2L));
    this.z = new ShortRef(ref.offset(2, 0x4L));
  }

  public SVECTOR set(final SVECTOR other) {
    this.setX(other.getX());
    this.setY(other.getY());
    this.setZ(other.getZ());
    return this;
  }

  public SVECTOR set(final VECTOR other) {
    this.setX((short)other.getX());
    this.setY((short)other.getY());
    this.setZ((short)other.getZ());
    return this;
  }

  public SVECTOR set(final Vector3f other) {
    this.setX((short)other.x);
    this.setY((short)other.y);
    this.setZ((short)other.z);
    return this;
  }

  public SVECTOR set(final short x, final short y, final short z) {
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

  public ShortRef component(final int element) {
    return switch(element) {
      case 0 -> this.x;
      case 1 -> this.y;
      case 2 -> this.z;
      default -> throw new IllegalArgumentException("Invalid element");
    };
  }

  public short getX() {
    return this.x.get();
  }

  public SVECTOR setX(final short x) {
    this.x.set(x);
    return this;
  }

  public short getY() {
    return this.y.get();
  }

  public SVECTOR setY(final short y) {
    this.y.set(y);
    return this;
  }

  public long getXY() {
    return (this.y.get() & 0xffffL) << 16 | this.x.get() & 0xffffL;
  }

  public SVECTOR setXY(final long xy) {
    this.setX((short)xy);
    this.setY((short)(xy >>> 16));
    return this;
  }

  public short getZ() {
    return this.z.get();
  }

  public SVECTOR setZ(final short z) {
    this.z.set(z);
    return this;
  }

  public SVECTOR add(final VECTOR other) {
    this.x.add((short)other.x.get());
    this.y.add((short)other.y.get());
    this.z.add((short)other.z.get());
    return this;
  }

  public SVECTOR add(final SVECTOR other) {
    this.x.add(other.x);
    this.y.add(other.y);
    this.z.add(other.z);
    return this;
  }

  public SVECTOR add(final short value) {
    this.x.add(value);
    this.y.add(value);
    this.z.add(value);
    return this;
  }

  public SVECTOR add(final short x, final short y, final short z) {
    this.x.add(x);
    this.y.add(y);
    this.z.add(z);
    return this;
  }

  public SVECTOR sub(final VECTOR other) {
    this.x.sub((short)other.x.get());
    this.y.sub((short)other.y.get());
    this.z.sub((short)other.z.get());
    return this;
  }

  public SVECTOR sub(final SVECTOR other) {
    this.x.sub(other.x);
    this.y.sub(other.y);
    this.z.sub(other.z);
    return this;
  }

  public SVECTOR sub(final short value) {
    this.x.sub(value);
    this.y.sub(value);
    this.z.sub(value);
    return this;
  }

  public SVECTOR sub(final short x, final short y, final short z) {
    this.x.sub(x);
    this.y.sub(y);
    this.z.sub(z);
    return this;
  }

  public SVECTOR mul(final VECTOR amount) {
    this.x.mul((short)amount.getX());
    this.y.mul((short)amount.getY());
    this.z.mul((short)amount.getZ());
    return this;
  }

  public SVECTOR mul(final SVECTOR amount) {
    this.x.mul(amount.getX());
    this.y.mul(amount.getY());
    this.z.mul(amount.getZ());
    return this;
  }

  public SVECTOR mul(final int amount) {
    this.x.mul((short)amount);
    this.y.mul((short)amount);
    this.z.mul((short)amount);
    return this;
  }

  public SVECTOR mul(final short value) {
    this.x.mul(value);
    this.y.mul(value);
    this.z.mul(value);
    return this;
  }

  public SVECTOR mul(final short x, final short y, final short z) {
    this.x.mul(x);
    this.y.mul(y);
    this.z.mul(z);
    return this;
  }

  public SVECTOR div(final VECTOR amount) {
    this.x.div((short)amount.getX());
    this.y.div((short)amount.getY());
    this.z.div((short)amount.getZ());
    return this;
  }

  public SVECTOR div(final SVECTOR amount) {
    this.x.div(amount.getX());
    this.y.div(amount.getY());
    this.z.div(amount.getZ());
    return this;
  }

  public SVECTOR div(final int divisor) {
    this.x.div((short)divisor);
    this.y.div((short)divisor);
    this.z.div((short)divisor);
    return this;
  }

  public SVECTOR div(final short divisor) {
    this.x.div(divisor);
    this.y.div(divisor);
    this.z.div(divisor);
    return this;
  }

  public SVECTOR div(final short x, final short y, final short z) {
    this.x.div(x);
    this.y.div(y);
    this.z.div(z);
    return this;
  }

  public SVECTOR mod(final VECTOR divisor) {
    this.x.mod((short)divisor.x.get());
    this.y.mod((short)divisor.y.get());
    this.z.mod((short)divisor.z.get());
    return this;
  }

  public SVECTOR mod(final SVECTOR divisor) {
    this.x.mod(divisor.x.get());
    this.y.mod(divisor.y.get());
    this.z.mod(divisor.z.get());
    return this;
  }

  public SVECTOR mod(final short divisor) {
    this.x.mod(divisor);
    this.y.mod(divisor);
    this.z.mod(divisor);
    return this;
  }

  public SVECTOR mod(final short x, final short y, final short z) {
    this.x.mod(x);
    this.y.mod(y);
    this.z.mod(z);
    return this;
  }

  public SVECTOR negate() {
    this.x.set((short)-this.x.get());
    this.y.set((short)-this.y.get());
    this.z.set((short)-this.z.get());
    return this;
  }

  public SVECTOR shr(final int amount) {
    this.x.shr(amount);
    this.y.shr(amount);
    this.z.shr(amount);
    return this;
  }

  public SVECTOR shra(final int amount) {
    this.x.shra(amount);
    this.y.shra(amount);
    this.z.shra(amount);
    return this;
  }

  public SVECTOR shl(final int amount) {
    this.x.shl(amount);
    this.y.shl(amount);
    this.z.shl(amount);
    return this;
  }

  public SVECTOR and(final int value) {
    this.x.and(value);
    this.y.and(value);
    this.z.and(value);
    return this;
  }

  public SVECTOR or(final int value) {
    this.x.or(value);
    this.y.or(value);
    this.z.or(value);
    return this;
  }

  public SVECTOR mul(final MATRIX matrix, final SVECTOR out) {
    out.set(
      (short)((long)matrix.get(0, 0) * this.getX() + matrix.get(0, 1) * this.getY() + matrix.get(0, 2) * this.getZ() >> 12),
      (short)((long)matrix.get(1, 0) * this.getX() + matrix.get(1, 1) * this.getY() + matrix.get(1, 2) * this.getZ() >> 12),
      (short)((long)matrix.get(2, 0) * this.getX() + matrix.get(2, 1) * this.getY() + matrix.get(2, 2) * this.getZ() >> 12)
    );

    return this;
  }

  public SVECTOR mul(final MATRIX matrix, final Vector3f out) {
    out.set(
      (short)((long)matrix.get(0, 0) * this.getX() + matrix.get(0, 1) * this.getY() + matrix.get(0, 2) * this.getZ() >> 12) / 4096.0f,
      (short)((long)matrix.get(1, 0) * this.getX() + matrix.get(1, 1) * this.getY() + matrix.get(1, 2) * this.getZ() >> 12) / 4096.0f,
      (short)((long)matrix.get(2, 0) * this.getX() + matrix.get(2, 1) * this.getY() + matrix.get(2, 2) * this.getZ() >> 12) / 4096.0f
    );

    return this;
  }

  public SVECTOR mul(final MATRIX matrix, final VECTOR out) {
    out.set(
      (int)((long)matrix.get(0, 0) * this.getX() + matrix.get(0, 1) * this.getY() + matrix.get(0, 2) * this.getZ() >> 12),
      (int)((long)matrix.get(1, 0) * this.getX() + matrix.get(1, 1) * this.getY() + matrix.get(1, 2) * this.getZ() >> 12),
      (int)((long)matrix.get(2, 0) * this.getX() + matrix.get(2, 1) * this.getY() + matrix.get(2, 2) * this.getZ() >> 12)
    );

    return this;
  }

  public SVECTOR mul(final MATRIX matrix) {
    return this.mul(matrix, this);
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
    return "SVECTOR {x: " + this.x + ", y: " + this.y + ", z: " + this.z + '}' + (this.ref == null ? " (local)" : " @ " + Long.toHexString(this.getAddress()));
  }
}
