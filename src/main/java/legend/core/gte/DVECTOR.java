package legend.core.gte;

import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.ShortRef;

import javax.annotation.Nullable;

/** A short 2D vector */
public class DVECTOR implements MemoryRef {
  @Nullable
  private final Value ref;

  public final ShortRef x;
  public final ShortRef y;

  public DVECTOR() {
    this.ref = null;
    this.x = new ShortRef();
    this.y = new ShortRef();
  }

  public DVECTOR(final Value ref) {
    this.ref = ref;
    this.x = new ShortRef(ref.offset(2, 0x0L));
    this.y = new ShortRef(ref.offset(2, 0x2L));
  }

  public DVECTOR set(final DVECTOR other) {
    this.setX(other.getX());
    this.setY(other.getY());
    return this;
  }

  public DVECTOR set(final short x, final short y) {
    return this.setX(x).setY(y);
  }

  public short getX() {
    return this.x.get();
  }

  public DVECTOR setX(final short x) {
    this.x.set(x);
    return this;
  }

  public short getY() {
    return this.y.get();
  }

  public DVECTOR setY(final short y) {
    this.y.set(y);
    return this;
  }

  public long getXY() {
    return (this.y.get() & 0xffffL) << 16 | this.x.get() & 0xffffL;
  }

  public DVECTOR setXY(final long xy) {
    this.setX((short)xy);
    this.setY((short)(xy >>> 16));
    return this;
  }

  public DVECTOR add(final DVECTOR other) {
    this.x.add(other.x);
    this.y.add(other.y);
    return this;
  }

  public DVECTOR sub(final DVECTOR other) {
    this.x.sub(other.x);
    this.y.sub(other.y);
    return this;
  }

  public DVECTOR div(final int divisor) {
    this.x.set((short)(this.x.get() / divisor));
    this.y.set((short)(this.y.get() / divisor));
    return this;
  }

  public DVECTOR negate() {
    this.x.set((short)-this.x.get());
    this.y.set((short)-this.y.get());
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
    return "DVECTOR {x: " + this.x + ", y: " + this.y + '}' + (this.ref == null ? " (local)" : " @ " + Long.toHexString(this.getAddress()));
  }
}
