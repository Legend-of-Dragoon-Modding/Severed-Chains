package legend.core;

import legend.core.gpu.RECT;
import legend.core.gte.BVEC4;
import legend.core.gte.SVECTOR;
import legend.core.gte.USCOLOUR;
import legend.core.memory.Value;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.lwjgl.BufferUtils.createByteBuffer;
import static org.lwjgl.system.MemoryUtil.memSlice;

public final class IoHelper {
  private IoHelper() { }

  public static boolean getPackedFlag(final int[] array, final int packed) {
    return (array[packed >>> 5] & 0x1 << (packed & 0x1f)) != 0;
  }

  /**
   * Reads the specified resource and returns the raw data as a ByteBuffer.
   *
   * @param path   the resource to read
   * @return the resource data
   * @throws IOException if an IO error occurs
   */
  public static ByteBuffer pathToByteBuffer(final Path path) throws IOException {
    final ByteBuffer buffer;

    try(final SeekableByteChannel fc = Files.newByteChannel(path)) {
      buffer = createByteBuffer((int)fc.size() + 1);
      while(fc.read(buffer) != -1) {
      }
    }

    buffer.flip();
    return memSlice(buffer);
  }

  public static void write(final ByteBuffer stream, final boolean value) {
    stream.put((byte)(value ? 1 : 0));
  }

  public static void write(final ByteBuffer stream, final Enum<?> value) {
    stream.put((byte)value.ordinal());
  }

  public static void write(final ByteBuffer stream, final byte value) {
    stream.put(value);
  }

  public static void write(final ByteBuffer stream, final short value) {
    stream.putShort(value);
  }

  public static void write(final ByteBuffer stream, final int value) {
    stream.putInt(value);
  }

  /** Write a 24-bit int */
  public static void write3(final ByteBuffer stream, final int value) {
    stream.putInt(value << 8);
    stream.position(stream.position() - 1);
  }

  public static void write(final ByteBuffer stream, final long value) {
    write(stream, (int)value);
  }

  public static void write(final ByteBuffer stream, final double value) {
    stream.putDouble(value);
  }

  public static void write(final ByteBuffer stream, final String value) {
    write(stream, value.length());
    stream.put(value.getBytes());
  }

  public static void write(final ByteBuffer stream, final RECT value) {
    write(stream, value.x.get());
    write(stream, value.y.get());
    write(stream, value.w.get());
    write(stream, value.h.get());
  }

  public static void read(final ByteBuffer stream, final Value value) {
    for(int i = 0; i < value.getSize(); i++) {
      value.offset(1, i).setu(stream.get());
    }
  }

  public static boolean readBool(final ByteBuffer stream) {
    return stream.get() != 0;
  }

  public static <T extends Enum<T>> T readEnum(final ByteBuffer stream, final Class<T> cls) {
    return cls.getEnumConstants()[stream.get()];
  }

  public static int readUByte(final ByteBuffer stream) {
    return stream.get() & 0xff;
  }

  public static int readUByte(final byte[] data, final int offset) {
    return data[offset] & 0xff;
  }

  public static byte readByte(final ByteBuffer stream) {
    return stream.get();
  }

  public static byte readByte(final byte[] data, final int offset) {
    return data[offset];
  }

  public static int readUShort(final ByteBuffer stream) {
    return stream.getShort() & 0xffff;
  }

  public static int readUShort(final byte[] data, final int offset) {
    return (int)MathHelper.get(data, offset, 2);
  }

  public static short readShort(final ByteBuffer stream) {
    return stream.getShort();
  }

  public static short readShort(final byte[] data, final int offset) {
    return (short)MathHelper.get(data, offset, 2);
  }

  public static int readInt(final ByteBuffer stream) {
    return stream.getInt();
  }

  public static int readInt(final byte[] data, final int offset) {
    return (int)MathHelper.get(data, offset, 4);
  }

  public static long readUInt(final ByteBuffer stream) {
    return readInt(stream) & 0xffff_ffffL;
  }

  public static long readUInt(final byte[] data, final int offset) {
    return MathHelper.get(data, offset, 4);
  }

  public static double readDouble(final ByteBuffer stream) {
    return stream.getDouble();
  }

  public static String readString(final ByteBuffer stream) {
    return readString(stream, readInt(stream));
  }

  public static String readString(final ByteBuffer stream, final int length) {
    final byte[] data = new byte[length];
    stream.get(data);
    return new String(data);
  }

  public static RECT readRect(final ByteBuffer stream, final RECT rect) {
    return rect.set(readShort(stream), readShort(stream), readShort(stream), readShort(stream));
  }

  public static RECT readRect(final byte[] data, final int offset, final RECT rect) {
    return rect.set(readShort(data, offset), readShort(data, offset), readShort(data, offset), readShort(data, offset));
  }

  public static BVEC4 readBvec3(final ByteBuffer stream, final BVEC4 bvec) {
    return bvec.set(readByte(stream), readByte(stream), readByte(stream));
  }

  public static BVEC4 readBvec3(final byte[] data, final int offset, final BVEC4 bvec) {
    return bvec.set(readByte(data, offset), readByte(data, offset), readByte(data, offset));
  }

  public static SVECTOR readSvec3(final ByteBuffer stream, final SVECTOR svec) {
    return svec.set(readShort(stream), readShort(stream), readShort(stream));
  }

  public static SVECTOR readSvec3(final byte[] data, final int offset, final SVECTOR svec) {
    return svec.set(readShort(data, offset), readShort(data, offset), readShort(data, offset));
  }

  public static USCOLOUR readColour(final ByteBuffer stream, final USCOLOUR colour) {
    return colour.set(readUShort(stream), readUShort(stream), readUShort(stream));
  }

  public static USCOLOUR readColour(final byte[] data, final int offset, final USCOLOUR colour) {
    return colour.set(readUShort(data, offset), readUShort(data, offset), readUShort(data, offset));
  }
}
