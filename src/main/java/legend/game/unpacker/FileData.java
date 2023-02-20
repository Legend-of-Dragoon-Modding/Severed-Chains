package legend.game.unpacker;

import legend.core.MathHelper;

import java.util.Arrays;

public record FileData(byte[] data, int offset, int size, boolean virtual) {
  public static FileData virtual(final byte[] data) {
    return new FileData(data, 0, data.length, true);
  }

  public FileData(final byte[] data) {
    this(data, 0, data.length, false);
  }

  public FileData(final byte[] data, final int offset, final int size) {
    this(data, offset, size, false);
  }

  /** Not a virtual file and larger than zero bytes */
  public boolean real() {
    return !this.virtual && this.size != 0;
  }

  public FileData slice(final int offset, final int size) {
    return new FileData(this.data, this.offset + offset, size, false);
  }

  public FileData slice(final int offset) {
    return this.slice(offset, this.size - offset);
  }

  /** Returns the original array if this file is the only thing it represents */
  public byte[] getBytes() {
    if(this.offset == 0 && this.size == this.data.length) {
      return this.data;
    }

    return Arrays.copyOfRange(this.data, this.offset, this.size);
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
