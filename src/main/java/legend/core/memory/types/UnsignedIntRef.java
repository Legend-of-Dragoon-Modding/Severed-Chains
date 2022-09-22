package legend.core.memory.types;

import legend.core.memory.Value;

import javax.annotation.Nullable;

public class UnsignedIntRef implements MemoryRef {
  @Nullable
  private final Value ref;

  private int val;

  public UnsignedIntRef() {
    this.ref = null;
  }

  public UnsignedIntRef(final Value ref) {
    this.ref = ref;

    if(ref.getSize() != 4) {
      throw new IllegalArgumentException("Size of int refs must be 4");
    }
  }

  public long get() {
    if(this.ref != null) {
      return this.ref.get();
    }

    return this.val & 0xffff_ffffL;
  }

  public UnsignedIntRef set(final long val) {
    if((val & ~0xffff_ffffL) != 0) {
      throw new IllegalArgumentException("Overflow: " + val);
    }

    if(this.ref != null) {
      this.ref.setu(val);
    } else {
      this.val = (int)val;
    }

    return this;
  }

  public UnsignedIntRef set(final UnsignedIntRef val) {
    return this.set(val.get());
  }

  public UnsignedIntRef add(final long val) {
    return this.set(this.get() + val);
  }

  public UnsignedIntRef add(final UnsignedIntRef val) {
    return this.add(val.get());
  }

  public UnsignedIntRef addOverflow(final long val) {
    return this.set(this.get() + val & 0xffff_ffffL);
  }

  public UnsignedIntRef addOverflow(final UnsignedIntRef val) {
    return this.addOverflow(val.get());
  }

  public UnsignedIntRef sub(final long val) {
    return this.set(this.get() - val);
  }

  public UnsignedIntRef sub(final UnsignedIntRef val) {
    return this.sub(val.get());
  }

  public UnsignedIntRef subOverflow(final long val) {
    return this.set(this.get() - val & 0xffff_ffffL);
  }

  public UnsignedIntRef subOverflow(final UnsignedIntRef val) {
    return this.subOverflow(val.get());
  }

  public UnsignedIntRef mul(final long val) {
    return this.set(this.get() * val);
  }

  public UnsignedIntRef mul(final UnsignedIntRef val) {
    return this.mul(val.get());
  }

  public UnsignedIntRef mulOverflow(final long val) {
    return this.set(this.get() * val & 0xffff_ffffL);
  }

  public UnsignedIntRef mulOverflow(final UnsignedIntRef val) {
    return this.mulOverflow(val.get());
  }

  public UnsignedIntRef div(final long val) {
    return this.set(this.get() / val);
  }

  public UnsignedIntRef div(final UnsignedIntRef val) {
    return this.div(val.get());
  }

  public UnsignedIntRef incr() {
    return this.add(1);
  }

  public UnsignedIntRef incrOverflow() {
    return this.addOverflow(1);
  }

  public UnsignedIntRef decr() {
    return this.sub(1);
  }

  public UnsignedIntRef decrOverflow() {
    return this.subOverflow(1);
  }

  public UnsignedIntRef not() {
    return this.set(~this.get() & 0xffff_ffffL);
  }

  public UnsignedIntRef and(final long val) {
    return this.set(this.get() & val);
  }

  public UnsignedIntRef and(final UnsignedIntRef val) {
    return this.and(val.get());
  }

  public UnsignedIntRef or(final long val) {
    return this.set(this.get() | val);
  }

  public UnsignedIntRef or(final UnsignedIntRef val) {
    return this.or(val.get());
  }

  public UnsignedIntRef xor(final long val) {
    return this.set(this.get() ^ val);
  }

  public UnsignedIntRef xor(final UnsignedIntRef val) {
    return this.xor(val.get());
  }

  public UnsignedIntRef shl(final long bits) {
    return this.set(this.get() << bits & 0xffff_ffffL);
  }

  public UnsignedIntRef shl(final UnsignedIntRef bits) {
    return this.shl(bits.get());
  }

  public UnsignedIntRef shr(final long bits) {
    return this.set(this.get() >>> bits);
  }

  public UnsignedIntRef shr(final UnsignedIntRef bits) {
    return this.shr(bits.get());
  }

  @Override
  public long getAddress() {
    if(this.ref == null) {
      return 0;
    }

    return this.ref.getAddress();
  }

  @Override
  public String toString() {
    return Long.toHexString(this.get()) + (this.ref == null ? " (local)" : " @ " + Long.toHexString(this.getAddress()));
  }
}
