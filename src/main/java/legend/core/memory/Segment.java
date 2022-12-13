package legend.core.memory;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;

import javax.annotation.Nullable;
import java.lang.reflect.Method;

public abstract class Segment {
  private final long address;
  private final int length;

  private final Long2ObjectMap<MethodBinding> functions = new Long2ObjectOpenHashMap<>();

  public Segment(final long address, final int length) {
    this.address = address;
    this.length = length;
  }

  public long getAddress() {
    return this.address;
  }

  public int getLength() {
    return this.length;
  }

  public boolean accepts(final long address) {
    return address >= this.address && address < this.address + this.length;
  }

  public abstract byte get(final int offset);
  public abstract long get(final int offset, final int size);
  public abstract void set(final int offset, final byte value);
  public abstract void set(final int offset, final int size, final long value);

  public byte[] getBytes(final int offset, final int size) {
    throw new UnsupportedOperationException("This memory segment does not support direct reads (address: " + Long.toHexString(this.getAddress() + offset) + ')');
  }

  public void getBytes(final int offset, final byte[] dest, final int dataOffset, final int dataSize) {
    throw new UnsupportedOperationException("This memory segment does not support direct reads (address: " + Long.toHexString(this.getAddress() + offset) + ')');
  }

  public void setBytes(final int offset, final byte[] data) {
    throw new UnsupportedOperationException("This memory segment does not support direct writes (address: " + Long.toHexString(this.getAddress() + offset) + ')');
  }

  public void setBytes(final int offset, final byte[] data, final int dataOffset, final int dataLength) {
    throw new UnsupportedOperationException("This memory segment does not support direct writes (address: " + Long.toHexString(this.getAddress() + offset) + ')');
  }

  public void memcpy(final int dest, final int src, final int length) {
    throw new UnsupportedOperationException("This memory segment does not support memcpy (address: " + Long.toHexString(this.getAddress() + dest) + ')');
  }

  public void memfill(final int addr, final int length, final int value) {
    throw new UnsupportedOperationException("This memory segment does not support memcpy (address: " + Long.toHexString(this.getAddress() + addr) + ')');
  }

  protected void setFunction(final int offset, final Method function, @Nullable final Object instance, final boolean ignoreExtraParams) {
    function.setAccessible(true);

    this.functions.put(offset, new MethodBinding(function, instance, ignoreExtraParams));
  }

  protected void removeFunction(final int offset) {
    this.functions.remove(offset);
  }

  public void removeFunctions(final int start, final int end) {
    this.functions.long2ObjectEntrySet().removeIf(entry -> entry.getLongKey() >= start && entry.getLongKey() < end);
  }

  protected MethodBinding getFunction(final int offset) {
    final MethodBinding method = this.functions.get(offset);

    if(method == null) {
      throw new UnsupportedOperationException("There is no method at " + Long.toString(this.getAddress() + offset, 16) + ". The value is " + Long.toString(this.get(offset, 4), 16) + '.');
    }

    return method;
  }

  protected boolean isFunction(final int offset) {
    return this.functions.containsKey(offset & 0xffff_fffcL);
  }
}
