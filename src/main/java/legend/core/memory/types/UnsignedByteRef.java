package legend.core.memory.types;

import legend.core.memory.Value;

import javax.annotation.Nullable;

public class UnsignedByteRef implements MemoryRef {
  @Nullable
  private final Value ref;

  private byte val;

  public UnsignedByteRef() {
    this.ref = null;
  }

  public UnsignedByteRef(final Value ref) {
    this.ref = ref;

    if(ref.getSize() != 1) {
      throw new IllegalArgumentException("Size of byte refs must be 1");
    }
  }

  public int get() {
    if(this.ref != null) {
      return (int)this.ref.get();
    }

    return this.val & 0xff;
  }

  public UnsignedByteRef set(final int val) {
    if((val & ~0xff) != 0) {
      throw new IllegalArgumentException("Overflow: " + val);
    }

    if(this.ref != null) {
      this.ref.setu(val);
    }

    this.val = (byte)val;
    return this;
  }

  public UnsignedByteRef set(final UnsignedByteRef val) {
    this.set(val.get());
    return this;
  }

  public UnsignedByteRef add(final int amount) {
    return this.set(this.get() + amount);
  }

  public UnsignedByteRef add(final UnsignedByteRef amount) {
    return this.add(amount.get());
  }

  public UnsignedByteRef sub(final int amount) {
    return this.set(this.get() - amount);
  }

  public UnsignedByteRef sub(final UnsignedByteRef amount) {
    return this.sub(amount.get());
  }

  public UnsignedByteRef incr() {
    return this.add(1);
  }

  public UnsignedByteRef decr() {
    return this.sub(1);
  }

  public UnsignedByteRef not() {
    return this.set(~this.get() & 0xff);
  }

  public UnsignedByteRef and(final int val) {
    return this.set(this.get() & val);
  }

  public UnsignedByteRef and(final UnsignedByteRef val) {
    return this.and(val.get());
  }

  public UnsignedByteRef or(final int val) {
    return this.set(this.get() | val);
  }

  public UnsignedByteRef or(final UnsignedByteRef val) {
    return this.or(val.get());
  }

  public UnsignedByteRef xor(final int val) {
    return this.set(this.get() ^ val);
  }

  public UnsignedByteRef xor(final UnsignedByteRef val) {
    return this.xor(val.get());
  }

  @Override
  public long getAddress() {
    return this.ref != null ? this.ref.getAddress() : 0;
  }

  @Override
  public String toString() {
    return Integer.toHexString(this.get()) + (this.ref == null ? " (local)" : " @ " + Long.toHexString(this.getAddress()));
  }
}
