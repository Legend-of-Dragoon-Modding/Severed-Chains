package legend.core.memory.types;

import legend.core.memory.Value;

import javax.annotation.Nullable;

public class UnsignedInt24Ref implements MemoryRef {
  @Nullable
  private final Value ref;

  private int val;

  public UnsignedInt24Ref() {
    this.ref = null;
  }

  public UnsignedInt24Ref(final Value ref) {
    this.ref = ref;

    if(ref.getSize() != 3) {
      throw new IllegalArgumentException("Size of int24 refs must be 3");
    }
  }

  public long get() {
    if(this.ref != null) {
      return this.ref.get();
    }

    return this.val & 0xff_ffffL;
  }

  public UnsignedInt24Ref set(final long val) {
    if((val & ~0xff_ffffL) != 0) {
      throw new IllegalArgumentException("Overflow: " + val);
    }

    if(this.ref != null) {
      this.ref.setu(val);
    } else {
      this.val = (int)val;
    }

    return this;
  }

  public UnsignedInt24Ref set(final UnsignedInt24Ref val) {
    return this.set(val.get());
  }

  public UnsignedInt24Ref add(final long val) {
    return this.set(this.get() + val);
  }

  public UnsignedInt24Ref add(final UnsignedInt24Ref val) {
    return this.add(val.get());
  }

  public UnsignedInt24Ref sub(final long val) {
    return this.set(this.get() - val);
  }

  public UnsignedInt24Ref sub(final UnsignedInt24Ref val) {
    return this.sub(val.get());
  }

  public UnsignedInt24Ref incr() {
    return this.add(1);
  }

  public UnsignedInt24Ref decr() {
    return this.sub(1);
  }

  public UnsignedInt24Ref not() {
    return this.set(~this.get() & 0xffff_ffffL);
  }

  public UnsignedInt24Ref and(final long val) {
    return this.set(this.get() & val);
  }

  public UnsignedInt24Ref and(final UnsignedInt24Ref val) {
    return this.and(val.get());
  }

  public UnsignedInt24Ref or(final long val) {
    return this.set(this.get() | val);
  }

  public UnsignedInt24Ref or(final UnsignedInt24Ref val) {
    return this.or(val.get());
  }

  public UnsignedInt24Ref xor(final long val) {
    return this.set(this.get() ^ val);
  }

  public UnsignedInt24Ref xor(final UnsignedInt24Ref val) {
    return this.xor(val.get());
  }

  public UnsignedInt24Ref shl(final long bits) {
    return this.set(this.get() << bits);
  }

  public UnsignedInt24Ref shl(final UnsignedInt24Ref bits) {
    return this.shl(bits.get());
  }

  public UnsignedInt24Ref shr(final long bits) {
    return this.set(this.get() >>> bits);
  }

  public UnsignedInt24Ref shr(final UnsignedInt24Ref bits) {
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
