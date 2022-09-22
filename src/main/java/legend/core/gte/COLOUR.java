package legend.core.gte;

import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.UnsignedByteRef;

import javax.annotation.Nullable;

public class COLOUR implements MemoryRef {
  @Nullable
  private final Value ref;

  public final UnsignedByteRef r;
  public final UnsignedByteRef g;
  public final UnsignedByteRef b;
  public final UnsignedByteRef pad;

  public COLOUR(final Value ref) {
    this.ref = ref;
    this.r = new UnsignedByteRef(ref.offset(1, 0x0L));
    this.g = new UnsignedByteRef(ref.offset(1, 0x1L));
    this.b = new UnsignedByteRef(ref.offset(1, 0x2L));
    this.pad = new UnsignedByteRef(ref.offset(1, 0x3L));
  }

  public COLOUR() {
    this.ref = null;
    this.r = new UnsignedByteRef();
    this.g = new UnsignedByteRef();
    this.b = new UnsignedByteRef();
    this.pad = new UnsignedByteRef();
  }

  public COLOUR set(final COLOUR other) {
    this.setR(other.getR());
    this.setG(other.getG());
    this.setB(other.getB());
    return this;
  }

  public COLOUR set(final int r, final int g, final int b) {
    return this.setR(r).setG(g).setB(b);
  }

  public int getR() {
    return this.r.get();
  }

  public COLOUR setR(final int r) {
    this.r.set(r);
    return this;
  }

  public int getG() {
    return this.g.get();
  }

  public COLOUR setG(final int g) {
    this.g.set(g);
    return this;
  }

  public int getB() {
    return this.b.get();
  }

  public COLOUR setB(final int b) {
    this.b.set(b);
    return this;
  }

  public long pack() {
    return this.getB() << 16 | this.getG() << 8 | this.getR();
  }

  public COLOUR unpack(final long packed) {
    this.setR((int)(packed & 0xff));
    this.setG((int)(packed >>>  8 & 0xff));
    this.setB((int)(packed >>> 16 & 0xff));
    return this;
  }

  public COLOUR add(final COLOUR other) {
    this.r.add(other.r.get());
    this.g.add(other.g.get());
    this.b.add(other.b.get());
    return this;
  }

  public COLOUR sub(final COLOUR other) {
    this.r.sub(other.r.get());
    this.g.sub(other.g.get());
    this.b.sub(other.b.get());
    return this;
  }

  public COLOUR div(final int divisor) {
    this.r.set(this.r.get() / divisor);
    this.g.set(this.g.get() / divisor);
    this.b.set(this.b.get() / divisor);
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
    return "COLOUR {r: " + this.r + ", g: " + this.g + ", b: " + this.b + '}' + " @ " + (this.ref == null ? " (local)" : " @ " + Long.toHexString(this.getAddress()));
  }
}
