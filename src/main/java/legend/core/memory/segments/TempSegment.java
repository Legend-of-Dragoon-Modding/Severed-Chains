package legend.core.memory.segments;

import legend.core.memory.IllegalAddressException;
import legend.core.memory.Segment;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.BitSet;

public class TempSegment extends Segment {
  private final byte[] temp = new byte[0x1000];
  private BitSet tempUsage = new BitSet(0x1000);

  public TempSegment() {
    super(0xffff_0000L, 0x1000);
  }

  public int allocate(final int length) {
    outer:
    for(int i = 0; i < this.temp.length; i++) {
      for(int n = 0; n < length; n++) {
        if(this.tempUsage.get(i + n)) {
          continue outer;
        }
      }

      this.tempUsage.set(i, i + length);
      return i;
    }

    throw new RuntimeException("Ran out of temporary space");
  }

  public void release(final int offset, final int length) {
    this.tempUsage.clear(offset, offset + length);
  }

  @Override
  public byte get(final int offset) {
    if(!this.tempUsage.get(offset)) {
      throw new IllegalAddressException("There's no temp value reserved at " + Integer.toHexString(offset));
    }

    return this.temp[offset];
  }

  @Override
  public long get(final int offset, final int size) {
    if(!this.tempUsage.get(offset)) {
      throw new IllegalAddressException("There's no temp value reserved at " + Integer.toHexString(offset));
    }

    long value = 0;
    for(int i = 0; i < size; i++) {
      value |= (this.temp[offset + i] & 0xffL) << i * 8;
    }

    return value;
  }

  @Override
  public void set(final int offset, final byte value) {
    if(!this.tempUsage.get(offset)) {
      throw new IllegalAddressException("There's no temp value reserved at " + Integer.toHexString(offset));
    }

    this.temp[offset] = value;
  }

  @Override
  public void set(final int offset, final int size, final long value) {
    for(int i = 0; i < size; i++) {
      if(!this.tempUsage.get(offset + i)) {
        throw new IllegalAddressException("There's no temp value reserved at " + Integer.toHexString(offset + i));
      }

      this.temp[offset + i] = (byte)(value >> i * 8 & 0xff);
    }
  }

  @Override
  public byte[] getBytes(final int offset, final int size) {
    if(this.tempUsage.nextClearBit(offset) < offset + size) {
      throw new IllegalAddressException("There's no temp value reserved from %08x to %08x".formatted(offset, offset + size));
    }

    final byte[] data = new byte[size];
    System.arraycopy(this.temp, offset, data, 0, size);
    return data;
  }

  @Override
  public void getBytes(final int offset, final byte[] dest, final int dataOffset, final int dataSize) {
    if(this.tempUsage.nextClearBit(offset) < offset + dataSize) {
      throw new IllegalAddressException("There's no temp value reserved from %08x to %08x".formatted(offset, offset + dataSize));
    }

    System.arraycopy(this.temp, offset, dest, dataOffset, dataSize);
  }

  @Override
  public void setBytes(final int offset, final byte[] data) {
    if(this.tempUsage.nextClearBit(offset) < offset + data.length) {
      throw new IllegalAddressException("There's no temp value reserved from %08x to %08x".formatted(offset, offset + data.length));
    }

    this.removeFunctions(offset, offset + data.length);
    System.arraycopy(data, 0, this.temp, offset, data.length);
  }

  @Override
  public void setBytes(final int offset, final byte[] data, final int dataOffset, final int dataLength) {
    if(this.tempUsage.nextClearBit(offset) < offset + dataLength) {
      throw new IllegalAddressException("There's no temp value reserved from %08x to %08x".formatted(offset, offset + dataLength));
    }

    this.removeFunctions(offset, offset + data.length);
    System.arraycopy(data, dataOffset, this.temp, offset, dataLength);
  }

  @Override
  public void memcpy(final int dest, final int src, final int length) {
    if(this.tempUsage.nextClearBit(src) < src + length) {
      throw new IllegalAddressException("There's no temp value reserved for src from %08x to %08x".formatted(src, src + length));
    }

    if(this.tempUsage.nextClearBit(dest) < dest + length) {
      throw new IllegalAddressException("There's no temp value reserved for dest from %08x to %08x".formatted(dest, dest + length));
    }

    this.removeFunctions(dest, dest + length);
    System.arraycopy(this.temp, src, this.temp, dest, length);
  }

  @Override
  public void memfill(final int addr, final int length, final int value) {
    if(this.tempUsage.nextClearBit(addr) < addr + length) {
      throw new IllegalAddressException("There's no temp value reserved for src from %08x to %08x".formatted(addr, addr + length));
    }

    this.removeFunctions(addr, addr + length);
    Arrays.fill(this.temp, addr, addr + length, (byte)value);
  }

  @Override
  public void dump(final ByteBuffer stream) {
    stream.put(this.temp);

    final byte[] bits = this.tempUsage.toByteArray();
    stream.putInt(bits.length);
    stream.put(bits);
  }

  @Override
  public void load(final ByteBuffer stream) {
    stream.get(this.temp);

    final byte[] bits = new byte[stream.getInt()];
    stream.get(bits);
    this.tempUsage = BitSet.valueOf(bits);
  }
}
