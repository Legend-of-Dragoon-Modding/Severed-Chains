package legend.core.memory.types;

import legend.core.memory.Value;

import javax.annotation.Nullable;

public class ByteRef implements MemoryRef {
  @Nullable
  private final Value ref;

  private byte val;

  public ByteRef() {
    this.ref = null;
  }

  public ByteRef(final Value ref) {
    this.ref = ref;

    if(ref.getSize() != 1) {
      throw new IllegalArgumentException("Size of byte refs must be 1");
    }
  }

  public byte get() {
    if(this.ref != null) {
      return (byte)this.ref.get();
    }

    return this.val;
  }

  public ByteRef set(final int val) {
    if(this.ref != null) {
      this.ref.setu(val);
    }

    this.val = (byte)val;
    return this;
  }

  public ByteRef set(final ByteRef val) {
    this.set(val.get());
    return this;
  }

  public long getUnsigned() {
    return this.ref.get() & 0xffL;
  }

  public ByteRef setUnsigned(final long val) {
    this.ref.setu(val);
    return this;
  }

  public ByteRef add(final int amount) {
    return this.set((byte)(this.get() + amount));
  }

  public ByteRef addUnsigned(final long amount) {
    return this.setUnsigned(this.getUnsigned() + amount);
  }

  public ByteRef sub(final int amount) {
    return this.set((byte)(this.get() - amount));
  }

  public ByteRef subUnsigned(final long amount) {
    return this.setUnsigned(this.getUnsigned() - amount);
  }

  public ByteRef incr() {
    return this.add(1);
  }

  public ByteRef decr() {
    return this.sub(1);
  }

  public ByteRef not() {
    return this.set(~this.get());
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
