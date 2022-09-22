package legend.core.memory.types;

import legend.core.memory.Value;

import javax.annotation.Nullable;

public class ShortRef implements MemoryRef {
  @Nullable
  private final Value ref;

  private short val;

  public ShortRef() {
    this.ref = null;
  }

  public ShortRef(final Value ref) {
    this.ref = ref;

    if(ref.getSize() != 2) {
      throw new IllegalArgumentException("Size of short refs must be 2");
    }
  }

  public short get() {
    if(this.ref != null) {
      return (short)this.ref.get();
    }

    return this.val;
  }

  public ShortRef set(final short val) {
    if(this.ref != null) {
      this.ref.setu(val);
    } else {
      this.val = val;
    }

    return this;
  }

  public ShortRef set(final ShortRef val) {
    return this.set(val.get());
  }

  public ShortRef add(final short val) {
    return this.set((short)(this.get() + val));
  }

  public ShortRef add(final ShortRef val) {
    return this.add(val.get());
  }

  public ShortRef sub(final short val) {
    return this.set((short)(this.get() - val));
  }

  public ShortRef sub(final ShortRef val) {
    return this.sub(val.get());
  }

  public ShortRef mul(final short val) {
    return this.set((short)(this.get() * val));
  }

  public ShortRef mul(final ShortRef val) {
    return this.mul(val.get());
  }

  public ShortRef div(final short val) {
    return this.set((short)(this.get() / val));
  }

  public ShortRef div(final ShortRef val) {
    return this.div(val.get());
  }

  public ShortRef incr() {
    return this.add((short)1);
  }

  public ShortRef decr() {
    return this.sub((short)1);
  }

  public ShortRef not() {
    return this.set((short)~this.get());
  }

  public ShortRef and(final int val) {
    return this.set((short)(this.get() & val));
  }

  public ShortRef and(final ShortRef val) {
    return this.and(val.get());
  }

  public ShortRef or(final int val) {
    return this.set((short)(this.get() | val));
  }

  public ShortRef or(final ShortRef val) {
    return this.or(val.get());
  }

  public ShortRef xor(final int val) {
    return this.set((short)(this.get() ^ val));
  }

  public ShortRef xor(final ShortRef val) {
    return this.xor(val.get());
  }

  public ShortRef shl(final int bits) {
    return this.set((short)(this.get() << bits));
  }

  public ShortRef shl(final ShortRef bits) {
    return this.shl(bits.get());
  }

  public ShortRef shr(final int bits) {
    return this.set((short)(this.get() >>> bits));
  }

  public ShortRef shr(final ShortRef bits) {
    return this.shr(bits.get());
  }

  public ShortRef shra(final int bits) {
    return this.set((short)(this.get() >> bits));
  }

  public ShortRef shra(final ShortRef bits) {
    return this.shra(bits.get());
  }

  public ShortRef neg() {
    return this.set((short)-this.get());
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
