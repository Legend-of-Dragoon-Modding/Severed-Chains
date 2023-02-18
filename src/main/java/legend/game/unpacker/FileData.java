package legend.game.unpacker;

import legend.core.MathHelper;
import legend.core.gpu.RECT;
import legend.core.gte.BVEC4;
import legend.core.gte.SVECTOR;

public record FileData(byte[] data, int offset, int size) {
  public FileData(final byte[] data) {
    this(data, 0, data.length);
  }

  public FileData slice(final int offset, final int size) {
    this.checkBounds(offset, size);
    return new FileData(this.data, this.offset + offset, size);
  }

  public FileData slice(final int offset) {
    this.checkBounds(offset, this.size - offset);
    return this.slice(offset, this.size - offset);
  }

  public void copyFrom(final int srcOffset, final byte[] dest, final int destOffset, final int size) {
    this.checkBounds(srcOffset, size);
    System.arraycopy(this.data, this.offset + srcOffset, dest, destOffset, size);
  }

  public void copyTo(final int srcOffset, final byte[] src, final int destOffset, final int size) {
    this.checkBounds(srcOffset, size);
    System.arraycopy(src, srcOffset, this.data, this.offset + destOffset, size);
  }

  public byte readByte(final int offset) {
    this.checkBounds(offset, 1);
    return this.data[this.offset + offset];
  }

  public void writeByte(final int offset, final int val) {
    this.checkBounds(offset, 1);
    MathHelper.set(this.data, this.offset + offset, 1, val);
  }

  public int readUByte(final int offset) {
    this.checkBounds(offset, 1);
    return this.data[this.offset + offset] & 0xff;
  }

  public short readShort(final int offset) {
    this.checkBounds(offset, 2);
    return (short)MathHelper.get(this.data, this.offset + offset, 2);
  }

  public void writeShort(final int offset, final int val) {
    this.checkBounds(offset, 2);
    MathHelper.set(this.data, this.offset + offset, 2, val);
  }

  public int readUShort(final int offset) {
    this.checkBounds(offset, 2);
    return (int)MathHelper.get(this.data, this.offset + offset, 2);
  }

  public int readInt(final int offset) {
    this.checkBounds(offset, 4);
    return (int)MathHelper.get(this.data, this.offset + offset, 4);
  }

  public void writeInt(final int offset, final int val) {
    this.checkBounds(offset, 4);
    MathHelper.set(this.data, this.offset + offset, 4, val);
  }

  public long readUInt(final int offset) {
    this.checkBounds(offset, 4);
    return MathHelper.get(this.data, this.offset + offset, 4);
  }

  public RECT readRect(final int offset, final RECT rect) {
    this.checkBounds(offset, 8);
    return rect.set(this.readShort(offset), this.readShort(offset + 0x2), this.readShort(offset + 0x4), this.readShort(offset + 0x6));
  }

  public BVEC4 readBvec3(final int offset, final BVEC4 bvec) {
    this.checkBounds(offset, 3);
    return bvec.set(this.readByte(offset), this.readByte(offset + 0x2), this.readByte(offset + 0x4));
  }

  public SVECTOR readSvec3(final int offset, final SVECTOR svec) {
    this.checkBounds(offset, 6);
    return svec.set(this.readShort(offset), this.readShort(offset + 0x2), this.readShort(offset + 0x4));
  }

  private void checkBounds(final int offset, final int size) {
    if(offset < 0) {
      throw new IndexOutOfBoundsException("Negative offset " + offset);
    }

    if(size < 0) {
      throw new IndexOutOfBoundsException("Negative size " + size);
    }

    if(offset + size > this.size) {
      throw new IndexOutOfBoundsException("Read end " + (offset + size) + " out of bounds " + this.size);
    }
  }
}
