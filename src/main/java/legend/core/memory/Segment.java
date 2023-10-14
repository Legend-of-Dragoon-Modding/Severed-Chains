package legend.core.memory;

public abstract class Segment {
  private final long address;
  private final int length;

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

  public void memfill(final int addr, final int length, final int value) {
    throw new UnsupportedOperationException("This memory segment does not support memcpy (address: " + Long.toHexString(this.getAddress() + addr) + ')');
  }
}
