package legend.core.gte;

import legend.core.memory.Value;
import legend.core.memory.types.ByteRef;
import legend.core.memory.types.MemoryRef;

import javax.annotation.Nullable;

public class BVEC4 implements MemoryRef {
  @Nullable
  private final Value ref;

  public final ByteRef x;
  public final ByteRef y;
  public final ByteRef z;
  public final ByteRef w;

  public BVEC4() {
    this.ref = null;
    this.x = new ByteRef();
    this.y = new ByteRef();
    this.z = new ByteRef();
    this.w = new ByteRef();
  }

  public BVEC4(final Value ref) {
    this.ref = ref;
    this.x = new ByteRef(ref.offset(1, 0x0L));
    this.y = new ByteRef(ref.offset(1, 0x1L));
    this.z = new ByteRef(ref.offset(1, 0x2L));
    this.w = new ByteRef(ref.offset(1, 0x3L));
  }

  public BVEC4 set(final BVEC4 other) {
    this.setX(other.getX());
    this.setY(other.getY());
    this.setZ(other.getZ());
    this.setW(other.getW());
    return this;
  }

  public BVEC4 set(final int x, final int y, final int z, final int w) {
    return this.setX(x).setY(y).setZ(z).setW(w);
  }

  public int get(final int element) {
    return switch(element) {
      case 0 -> this.getX();
      case 1 -> this.getY();
      case 2 -> this.getZ();
      case 3 -> this.getW();
      default -> throw new IllegalArgumentException("Invalid element");
    };
  }

  public ByteRef component(final int element) {
    return switch(element) {
      case 0 -> this.x;
      case 1 -> this.y;
      case 2 -> this.z;
      case 3 -> this.w;
      default -> throw new IllegalArgumentException("Invalid element");
    };
  }

  public short getX() {
    return this.x.get();
  }

  public BVEC4 setX(final int x) {
    this.x.set(x);
    return this;
  }

  public short getY() {
    return this.y.get();
  }

  public BVEC4 setY(final int y) {
    this.y.set(y);
    return this;
  }

  public int getZ() {
    return this.z.get();
  }

  public BVEC4 setZ(final int z) {
    this.z.set(z);
    return this;
  }

  public int getW() {
    return this.w.get();
  }

  public BVEC4 setW(final int w) {
    this.w.set(w);
    return this;
  }

  public long pack() {
    return this.getW() << 24 | this.getZ() << 16 | this.getY() << 8 | this.getX();
  }

  public BVEC4 unpack(final long packed) {
    this.setX((int)(packed & 0xff));
    this.setY((int)(packed >>>  8 & 0xff));
    this.setZ((int)(packed >>> 16 & 0xff));
    this.setW((int)(packed >>> 24 & 0xff));
    return this;
  }

  public BVEC4 add(final BVEC4 other) {
    this.x.add(other.x.get());
    this.y.add(other.y.get());
    this.z.add(other.z.get());
    this.w.add(other.w.get());
    return this;
  }

  public BVEC4 sub(final BVEC4 other) {
    this.x.sub(other.x.get());
    this.y.sub(other.y.get());
    this.z.sub(other.z.get());
    this.w.sub(other.w.get());
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
    return "BVEC4 {x: " + this.x + ", y: " + this.y + ", z: " + this.z + ", w: " + this.w + '}' + (this.ref == null ? " (local)" : " @ " + Long.toHexString(this.getAddress()));
  }
}
