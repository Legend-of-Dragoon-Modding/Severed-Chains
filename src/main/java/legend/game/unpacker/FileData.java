package legend.game.unpacker;

import legend.core.MathHelper;
import legend.core.gpu.RECT;
import legend.core.gte.BVEC4;
import legend.core.gte.COLOUR;
import org.joml.Vector3f;
import org.legendofdragoon.modloader.registries.RegistryId;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public record FileData(byte[] data, int offset, int size, int virtualSize, int realFileIndex) {
  public static FileData virtual(final FileData real, final int virtualSize, final int realFileIndex) {
    return new FileData(real.data, real.offset, real.data.length, virtualSize, realFileIndex);
  }

  public FileData(final byte[] data) {
    this(data, 0, data.length, data.length, -1);
  }

  public FileData(final byte[] data, final int offset, final int size) {
    this(data, offset, size, size, -1);
  }

  /** Not a virtual file and larger than zero bytes */
  public boolean real() {
    return this.realFileIndex == -1 && this.size != 0;
  }

  public boolean hasVirtualSize() {
    return this.virtualSize != 0;
  }

  public FileData slice(final int offset, final int size) {
    this.checkBounds(offset, size);
    return new FileData(this.data, this.offset + offset, size, this.virtualSize, -1);
  }

  public FileData slice(final int offset) {
    this.checkBounds(offset, this.size - offset);
    return this.slice(offset, this.size - offset);
  }

  /** Returns the original array if this file is the only thing it represents */
  public byte[] getBytes() {
    if(this.offset == 0 && this.size == this.data.length) {
      return this.data;
    }

    return Arrays.copyOfRange(this.data, this.offset, this.offset + this.size);
  }

  public void copyFrom(final int srcOffset, final byte[] dest, final int destOffset, final int size) {
    this.checkBounds(srcOffset, size);
    System.arraycopy(this.data, this.offset + srcOffset, dest, destOffset, size);
  }

  public void copyTo(final int srcOffset, final byte[] src, final int destOffset, final int size) {
    this.checkBounds(destOffset, size);
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

  public int readInt24(final int offset) {
    this.checkBounds(offset, 3);
    return (int)MathHelper.get(this.data, this.offset + offset, 3) << 8 >> 8;
  }

  public long readUInt(final int offset) {
    this.checkBounds(offset, 4);
    return MathHelper.get(this.data, this.offset + offset, 4);
  }

  public void writeAscii(final int offset, final String val) {
    this.writeAscii(offset, val, 3);
  }

  public void writeAscii(final int offset, final String val, final int lengthSize) {
    this.checkBounds(offset, val.length() + lengthSize);
    MathHelper.set(this.data, this.offset + offset, lengthSize, val.length());
    System.arraycopy(val.getBytes(StandardCharsets.US_ASCII), 0, this.data, this.offset + offset + lengthSize, val.length());
  }

  public String readAscii(final int offset) {
    return this.readAscii(offset, 3);
  }

  public String readAscii(final int offset, final int lengthSize) {
    this.checkBounds(offset, lengthSize);
    final int length = (int)MathHelper.get(this.data, this.offset + offset, lengthSize);
    this.checkBounds(offset, length + lengthSize);
    return new String(this.data, this.offset + offset + lengthSize, length, StandardCharsets.US_ASCII);
  }

  public void writeRegistryId(final int offset, final RegistryId id) {
    this.writeAscii(offset, id.toString());
  }

  public RegistryId readRegistryId(final int offset) {
    return RegistryId.of(this.readAscii(offset));
  }

  public RECT readRect(final int offset, final RECT rect) {
    this.checkBounds(offset, 8);
    return rect.set(this.readShort(offset), this.readShort(offset + 0x2), this.readShort(offset + 0x4), this.readShort(offset + 0x6));
  }

  public BVEC4 readBvec3(final int offset, final BVEC4 bvec) {
    this.checkBounds(offset, 3);
    return bvec.set(this.readByte(offset), this.readByte(offset + 0x1), this.readByte(offset + 0x2));
  }

  public Vector3f readSvec3_0(final int offset, final Vector3f svec) {
    this.checkBounds(offset, 6);
    return svec.set(this.readShort(offset), this.readShort(offset + 0x2), this.readShort(offset + 0x4));
  }

  public Vector3f readSvec3_12(final int offset, final Vector3f svec) {
    this.checkBounds(offset, 6);
    return svec.set(this.readShort(offset) / 4096.0f, this.readShort(offset + 0x2) / 4096.0f, this.readShort(offset + 0x4) / 4096.0f);
  }

  public Vector3f readSvec3Rotation(final int offset, final Vector3f svec) {
    this.checkBounds(offset, 6);
    return svec.set(MathHelper.psxDegToRad(this.readShort(offset)), MathHelper.psxDegToRad(this.readShort(offset + 0x2)), MathHelper.psxDegToRad(this.readShort(offset + 0x4)));
  }

  public COLOUR readColour(final int offset, final COLOUR colour) {
    this.checkBounds(offset, 3);
    return colour.set(this.readUByte(offset), this.readUByte(offset + 0x1), this.readUByte(offset + 0x2));
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
