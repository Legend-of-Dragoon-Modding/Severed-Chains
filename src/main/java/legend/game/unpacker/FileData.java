package legend.game.unpacker;

import legend.core.MathHelper;

public record FileData(byte[] data, int offset, int size) {
  public FileData(final byte[] data) {
    this(data, 0, data.length);
  }

  public FileData slice(final int offset, final int size) {
    return new FileData(this.data, this.offset + offset, size);
  }

  public FileData slice(final int offset) {
    return this.slice(offset, this.size - offset);
  }

  public void copyTo(final int srcOffset, final byte[] dest, final int destOffset, final int size) {
    System.arraycopy(this.data, this.offset + srcOffset, dest, destOffset, size);
  }

  public byte readByte(final int offset) {
    return this.data[this.offset + offset];
  }

  public int readUByte(final int offset) {
    return this.data[this.offset + offset] & 0xff;
  }

  public short readShort(final int offset) {
    return (short)MathHelper.get(this.data, this.offset + offset, 2);
  }

  public int readUShort(final int offset) {
    return (int)MathHelper.get(this.data, this.offset + offset, 2);
  }

  public int readInt(final int offset) {
    return (int)MathHelper.get(this.data, this.offset + offset, 4);
  }

  public long readUInt(final int offset) {
    return MathHelper.get(this.data, this.offset + offset, 4);
  }
}
