package legend.core.memory.types;

import legend.core.memory.Value;

import javax.annotation.Nullable;

public class UnsignedShortRef implements MemoryRef {
  @Nullable
  private final Value ref;

  private int val;

  public UnsignedShortRef() {
    this.ref = null;
  }

  public UnsignedShortRef(final Value ref) {
    this.ref = ref;

    if(ref.getSize() != 2) {
      throw new IllegalArgumentException("Size of short refs must be 2");
    }
  }

  public int get() {
    if(this.ref != null) {
      return (int)this.ref.get();
    }

    return this.val;
  }

  public UnsignedShortRef set(final int val) {
    if((val & ~0xffff) != 0) {
      throw new IllegalArgumentException("Overflow: " + val);
    }

    if(this.ref != null) {
      this.ref.setu(val);
    } else {
      this.val = val & 0xffff;
    }

    return this;
  }

  public UnsignedShortRef set(final UnsignedShortRef val) {
    return this.set(val.get());
  }

  public UnsignedShortRef add(final int val) {
    return this.set(this.get() + val);
  }

  public UnsignedShortRef add(final UnsignedShortRef val) {
    return this.add(val.get());
  }

  public UnsignedShortRef sub(final int val) {
    return this.set(this.get() - val);
  }

  public UnsignedShortRef sub(final UnsignedShortRef val) {
    return this.sub(val.get());
  }

  public UnsignedShortRef mul(final long val) {
    return this.set((int)(this.get() * val));
  }

  public UnsignedShortRef mul(final UnsignedShortRef val) {
    return this.mul(val.get());
  }

  public UnsignedShortRef mulOverflow(final long val) {
    return this.set((int)(this.get() * val & 0xffff));
  }

  public UnsignedShortRef mulOverflow(final UnsignedShortRef val) {
    return this.mulOverflow(val.get());
  }

  public UnsignedShortRef div(final long val) {
    return this.set((int)(this.get() / val));
  }

  public UnsignedShortRef div(final UnsignedShortRef val) {
    return this.div(val.get());
  }

  public UnsignedShortRef incr() {
    return this.add(1);
  }

  public UnsignedShortRef decr() {
    return this.sub(1);
  }

  public UnsignedShortRef not() {
    return this.set(~this.get() & 0xffff);
  }

  public UnsignedShortRef and(final int val) {
    return this.set(this.get() & val);
  }

  public UnsignedShortRef and(final UnsignedShortRef val) {
    return this.and(val.get());
  }

  public UnsignedShortRef or(final int val) {
    return this.set(this.get() | val);
  }

  public UnsignedShortRef or(final UnsignedShortRef val) {
    return this.or(val.get());
  }

  public UnsignedShortRef xor(final int val) {
    return this.set(this.get() ^ val);
  }

  public UnsignedShortRef xor(final UnsignedShortRef val) {
    return this.xor(val.get());
  }

  public UnsignedShortRef shl(final int bits) {
    return this.set(this.get() << bits);
  }

  public UnsignedShortRef shl(final UnsignedShortRef bits) {
    return this.shl(bits.get());
  }

  public UnsignedShortRef shr(final int bits) {
    return this.set(this.get() >>> bits);
  }

  public UnsignedShortRef shr(final UnsignedShortRef bits) {
    return this.shr(bits.get());
  }

  public UnsignedShortRef shra(final int bits) {
    return this.set(this.get() >> bits);
  }

  public UnsignedShortRef shra(final UnsignedShortRef bits) {
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
    return Integer.toHexString(this.get()) + (this.ref == null ? " (local)" : " @ " + Long.toHexString(this.getAddress()));
  }
}
