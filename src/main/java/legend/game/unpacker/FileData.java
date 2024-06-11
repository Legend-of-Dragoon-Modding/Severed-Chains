package legend.game.unpacker;

import legend.core.MathHelper;
import legend.core.gpu.Rect4i;
import legend.core.gte.MV;
import legend.core.memory.types.IntRef;
import org.joml.Vector3f;
import org.joml.Vector3i;
import org.legendofdragoon.modloader.registries.RegistryId;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class FileData {
  protected byte[] data;
  protected final int offset;
  private final int size;
  private final int virtualSize;
  private final int realFileIndex;

  public FileData(final byte[] data, final int offset, final int size, final int virtualSize, final int realFileIndex) {
    this.data = data;
    this.offset = offset;
    this.size = size;
    this.virtualSize = virtualSize;
    this.realFileIndex = realFileIndex;
  }

  public static FileData virtual(final FileData real, final int virtualSize, final int realFileIndex) {
    return new FileData(real.data, real.offset, real.data.length, virtualSize, realFileIndex);
  }

  public FileData(final byte[] data) {
    this(data, 0, data.length, data.length, -1);
  }

  public FileData(final byte[] data, final int offset, final int size) {
    this(data, offset, size, size, -1);
  }

  /**
   * Not a virtual file and larger than zero bytes
   */
  public boolean real() {
    return this.realFileIndex == -1 && this.size() != 0;
  }

  public boolean hasVirtualSize() {
    return this.virtualSize != 0;
  }

  public FileData slice(final int offset, final int size) {
    this.checkBounds(offset, size);
    return new FileData(this.data, this.offset + offset, size, this.virtualSize, -1);
  }

  public FileData slice(final int offset) {
    this.checkBounds(offset, this.size() - offset);
    return this.slice(offset, this.size() - offset);
  }

  /**
   * Returns the original array if this file is the only thing it represents
   */
  public byte[] getBytes() {
    if(this.offset == 0 && this.size() == this.data.length) {
      return this.data;
    }

    return Arrays.copyOfRange(this.data, this.offset, this.offset + this.size());
  }

  public void read(final int srcOffset, final byte[] dest, final int destOffset, final int size) {
    this.checkBounds(srcOffset, size);
    System.arraycopy(this.data, this.offset + srcOffset, dest, destOffset, size);
  }

  public void read(final IntRef srcOffset, final byte[] dest, final int destOffset, final int size) {
    this.read(srcOffset.get(), dest, destOffset, size);
    srcOffset.add(size);
  }

  public void read(final int srcOffset, final FileData dest, final int destOffset, final int size) {
    this.read(srcOffset, dest.data, dest.offset + destOffset, size);
  }

  public void read(final IntRef srcOffset, final FileData dest, final int destOffset, final int size) {
    this.read(srcOffset, dest.data, dest.offset + destOffset, size);
  }

  public void read(final byte[] dest) {
    this.read(0, dest, 0, Math.min(this.size(), dest.length));
  }

  public void write(final int srcOffset, final byte[] src, final int destOffset, final int size) {
    this.checkBounds(destOffset, size);
    System.arraycopy(src, srcOffset, this.data, this.offset + destOffset, size);
  }

  public void write(final int srcOffset, final byte[] src, final IntRef destOffset, final int size) {
    this.write(srcOffset, src, destOffset.get(), size);
    destOffset.add(size);
  }

  public void write(final int srcOffset, final FileData src, final int destOffset, final int size) {
    this.write(srcOffset + src.offset, src.data, destOffset, size);
  }

  public void write(final int srcOffset, final FileData src, final IntRef destOffset, final int size) {
    this.write(srcOffset + src.offset, src.data, destOffset, size);
  }

  public byte readByte(final int offset) {
    this.checkBounds(offset, 1);
    return this.data[this.offset + offset];
  }

  public byte readByte(final IntRef offset) {
    final byte read = this.readByte(offset.get());
    offset.add(1);
    return read;
  }

  public void writeByte(final int offset, final int val) {
    this.checkBounds(offset, 1);
    MathHelper.set(this.data, this.offset + offset, 1, val);
  }

  public void writeByte(final IntRef offset, final int val) {
    this.writeByte(offset.get(), val);
    offset.add(1);
  }

  public int readUByte(final int offset) {
    return this.readByte(offset) & 0xff;
  }

  public int readUByte(final IntRef offset) {
    final int read = this.readUByte(offset.get());
    offset.add(1);
    return read;
  }

  public short readShort(final int offset) {
    this.checkBounds(offset, 2);
    return (short)MathHelper.get(this.data, this.offset + offset, 2);
  }

  public short readShort(final IntRef offset) {
    final short read = this.readShort(offset.get());
    offset.add(2);
    return read;
  }

  public void writeShort(final int offset, final int val) {
    this.checkBounds(offset, 2);
    MathHelper.set(this.data, this.offset + offset, 2, val);
  }

  public void writeShort(final IntRef offset, final int val) {
    this.writeShort(offset.get(), val);
    offset.add(2);
  }

  public int readUShort(final int offset) {
    return this.readShort(offset) & 0xffff;
  }

  public int readUShort(final IntRef offset) {
    final int read = this.readUShort(offset.get());
    offset.add(2);
    return read;
  }

  public int readInt(final int offset) {
    this.checkBounds(offset, 4);
    return (int)MathHelper.get(this.data, this.offset + offset, 4);
  }

  public int readInt(final IntRef offset) {
    final int read = this.readInt(offset.get());
    offset.add(4);
    return read;
  }

  public void writeInt(final int offset, final int val) {
    this.checkBounds(offset, 4);
    MathHelper.set(this.data, this.offset + offset, 4, val);
  }

  public void writeInt(final IntRef offset, final int val) {
    this.writeInt(offset.get(), val);
    offset.add(4);
  }

  public int readVarInt(int offset) {
    int tmp;
    if((tmp = this.readByte(offset++)) >= 0) {
      return tmp;
    }
    int result = tmp & 0x7f;
    if((tmp = this.readByte(offset++)) >= 0) {
      result |= tmp << 7;
    } else {
      result |= (tmp & 0x7f) << 7;
      if((tmp = this.readByte(offset++)) >= 0) {
        result |= tmp << 14;
      } else {
        result |= (tmp & 0x7f) << 14;
        if((tmp = this.readByte(offset++)) >= 0) {
          result |= tmp << 21;
        } else {
          result |= (tmp & 0x7f) << 21;
          result |= (tmp = this.readByte(offset++)) << 28;
          while(tmp < 0) {
            tmp = this.readByte(offset++);
          }
        }
      }
    }
    return result;
  }

  public int readVarInt(final IntRef offset) {
    int tmp;
    if((tmp = this.readByte(offset)) >= 0) {
      return tmp;
    }
    int result = tmp & 0x7f;
    if((tmp = this.readByte(offset)) >= 0) {
      result |= tmp << 7;
    } else {
      result |= (tmp & 0x7f) << 7;
      if((tmp = this.readByte(offset)) >= 0) {
        result |= tmp << 14;
      } else {
        result |= (tmp & 0x7f) << 14;
        if((tmp = this.readByte(offset)) >= 0) {
          result |= tmp << 21;
        } else {
          result |= (tmp & 0x7f) << 21;
          result |= (tmp = this.readByte(offset)) << 28;
          while(tmp < 0) {
            tmp = this.readByte(offset);
          }
        }
      }
    }
    return result;
  }

  public void writeVarInt(int offset, int val) {
    do {
      final int bits = val & 0x7f;
      val >>>= 7;
      final byte b = (byte)(bits + (val != 0 ? 0x80 : 0));
      this.writeByte(offset++, b);
    } while(val != 0);
  }

  public void writeVarInt(final IntRef offset, int val) {
    do {
      final int bits = val & 0x7f;
      val >>>= 7;
      final byte b = (byte)(bits + (val != 0 ? 0x80 : 0));
      this.writeByte(offset, b);
    } while(val != 0);
  }

  public long readUInt(final int offset) {
    return this.readInt(offset) & 0xffff_ffffL;
  }

  public long readUInt(final IntRef offset) {
    final long read = this.readUInt(offset.get());
    offset.add(4);
    return read;
  }

  public void writeAscii(final int offset, final String val) {
    this.writeAscii(offset, val, 3);
  }

  public void writeAscii(final IntRef offset, final String val) {
    this.writeAscii(offset.get(), val);
    offset.add(3 + val.length());
  }

  public void writeAscii(final int offset, final String val, final int lengthSize) {
    this.checkBounds(offset, val.length() + lengthSize);
    MathHelper.set(this.data, this.offset + offset, lengthSize, val.length());
    System.arraycopy(val.getBytes(StandardCharsets.US_ASCII), 0, this.data, this.offset + offset + lengthSize, val.length());
  }

  public void writeAscii(final IntRef offset, final String val, final int lengthSize) {
    this.writeAscii(offset.get(), val, lengthSize);
    offset.add(lengthSize + val.length());
  }

  public String readAscii(final int offset) {
    return this.readAscii(offset, 3);
  }

  public String readAscii(final IntRef offset) {
    final String read = this.readAscii(offset.get());
    offset.add(3 + read.length());
    return read;
  }

  public String readAscii(final int offset, final int lengthSize) {
    this.checkBounds(offset, lengthSize);
    final int length = (int)MathHelper.get(this.data, this.offset + offset, lengthSize);
    this.checkBounds(offset, length + lengthSize);
    return new String(this.data, this.offset + offset + lengthSize, length, StandardCharsets.US_ASCII);
  }

  public String readByte(final IntRef offset, final int lengthSize) {
    final String read = this.readAscii(offset.get(), lengthSize);
    offset.add(lengthSize + read.length());
    return read;
  }

  public String readFixedLengthAscii(final int offset, final int length) {
    this.checkBounds(offset, length);
    return new String(this.data, this.offset + offset, length, StandardCharsets.US_ASCII);
  }

  public String readFixedLengthAscii(final IntRef offset, final int length) {
    final String read = this.readFixedLengthAscii(offset.get(), length);
    offset.add(length);
    return read;
  }

  public void writeRegistryId(final int offset, final RegistryId id) {
    this.writeAscii(offset, id.toString());
  }

  public void writeRegistryId(final IntRef offset, final RegistryId id) {
    this.writeAscii(offset, id.toString());
  }

  public RegistryId readRegistryId(final int offset) {
    return new RegistryId(this.readAscii(offset));
  }

  public RegistryId readRegistryId(final IntRef offset) {
    return new RegistryId(this.readAscii(offset));
  }

  public Rect4i readRect(final int offset, final Rect4i rect) {
    this.checkBounds(offset, 8);
    return rect.set(this.readShort(offset), this.readShort(offset + 0x2), this.readShort(offset + 0x4), this.readShort(offset + 0x6));
  }

  public Rect4i readRect(final IntRef offset, final Rect4i rect) {
    final Rect4i read = this.readRect(offset.get(), rect);
    offset.add(8);
    return read;
  }

  public Vector3i readBvec3(final int offset, final Vector3i bvec) {
    this.checkBounds(offset, 3);
    return bvec.set(this.readByte(offset), this.readByte(offset + 0x1), this.readByte(offset + 0x2));
  }

  public Vector3i readBvec3(final IntRef offset, final Vector3i bvec) {
    final Vector3i read = this.readBvec3(offset.get(), bvec);
    offset.add(3);
    return read;
  }

  public Vector3f readSvec3_0(final int offset, final Vector3f svec) {
    this.checkBounds(offset, 6);
    return svec.set(this.readShort(offset), this.readShort(offset + 0x2), this.readShort(offset + 0x4));
  }

  public Vector3f readSvec3_0(final IntRef offset, final Vector3f svec) {
    final Vector3f read = this.readSvec3_0(offset.get(), svec);
    offset.add(6);
    return read;
  }

  public Vector3f readSvec3_12(final int offset, final Vector3f svec) {
    this.checkBounds(offset, 6);
    return svec.set(this.readShort(offset) / 4096.0f, this.readShort(offset + 0x2) / 4096.0f, this.readShort(offset + 0x4) / 4096.0f);
  }

  public Vector3f readSvec3_12(final IntRef offset, final Vector3f svec) {
    final Vector3f read = this.readSvec3_12(offset.get(), svec);
    offset.add(6);
    return read;
  }

  public Vector3f readSvec3Rotation(final int offset, final Vector3f svec) {
    this.checkBounds(offset, 6);
    return svec.set(MathHelper.psxDegToRad(this.readShort(offset)), MathHelper.psxDegToRad(this.readShort(offset + 0x2)), MathHelper.psxDegToRad(this.readShort(offset + 0x4)));
  }

  public Vector3f readSvec3Rotation(final IntRef offset, final Vector3f svec) {
    final Vector3f read = this.readSvec3Rotation(offset.get(), svec);
    offset.add(6);
    return read;
  }

  public Vector3i readColour(final int offset, final Vector3i colour) {
    this.checkBounds(offset, 3);
    return colour.set(this.readUByte(offset), this.readUByte(offset + 0x1), this.readUByte(offset + 0x2));
  }

  public Vector3i readColour(final IntRef offset, final Vector3i colour) {
    final Vector3i read = this.readColour(offset.get(), colour);
    offset.add(3);
    return read;
  }

  public MV readMv(final int offset, final MV mv) {
    this.checkBounds(offset, 0x18);
    mv.m00 = this.readShort(offset) / (float)0x1000;
    mv.m10 = this.readShort(offset + 2) / (float)0x1000;
    mv.m20 = this.readShort(offset + 4) / (float)0x1000;
    mv.m01 = this.readShort(offset + 6) / (float)0x1000;
    mv.m11 = this.readShort(offset + 8) / (float)0x1000;
    mv.m21 = this.readShort(offset + 10) / (float)0x1000;
    mv.m02 = this.readShort(offset + 12) / (float)0x1000;
    mv.m12 = this.readShort(offset + 14) / (float)0x1000;
    mv.m22 = this.readShort(offset + 16) / (float)0x1000;

    for(int i = 0; i < 3; i++) {
      mv.transfer.setComponent(i, this.readShort(offset + 18 + i * 2));
    }

    return mv;
  }

  public MV readMv(final IntRef offset, final MV mv) {
    final MV read = this.readMv(offset.get(), mv);
    offset.add(0x18);
    return read;
  }

  protected void checkBounds(final int offset, final int size) {
    if(offset < 0) {
      throw new IndexOutOfBoundsException("Negative offset " + offset);
    }

    if(size < 0) {
      throw new IndexOutOfBoundsException("Negative size " + size);
    }

    if(offset + size > this.size()) {
      throw new IndexOutOfBoundsException("Read end " + (offset + size) + " out of bounds " + this.size());
    }
  }

  public void write(final OutputStream out) throws IOException {
    out.write(this.data, this.offset, this.size());
  }

  public ByteBuffer toByteBuffer() {
    final ByteBuffer buffer = BufferUtils.createByteBuffer(this.size);
    buffer.put(this.getBytes());
    buffer.flip();
    return buffer;
  }

  public int size() {
    return this.size;
  }

  public int realFileIndex() {
    return this.realFileIndex;
  }
}
