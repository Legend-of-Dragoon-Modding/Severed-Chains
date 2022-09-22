package legend.core.memory.segments;

import legend.core.memory.IllegalAddressException;
import legend.core.memory.Segment;

import java.nio.ByteBuffer;
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
