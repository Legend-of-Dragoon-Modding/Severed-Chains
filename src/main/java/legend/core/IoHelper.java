package legend.core;

import legend.core.gpu.RECT;
import legend.core.gte.SVECTOR;
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

  public static byte readByte(final ByteBuffer stream) {
    return stream.get();
  }

  public static short readShort(final ByteBuffer stream) {
    return stream.getShort();
  }

  public static int readInt(final ByteBuffer stream) {
    return stream.getInt();
  }

  public static long readLong(final ByteBuffer stream) {
    return readInt(stream) & 0xffff_ffffL;
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

  public static void readRect(final ByteBuffer stream, final RECT rect) {
    rect.set(readShort(stream), readShort(stream), readShort(stream), readShort(stream));
  }

  /** NOTE: DOES NOT READ PADDING */
  public static void readSvec(final ByteBuffer stream, final SVECTOR svec) {
    svec.set(readShort(stream), readShort(stream), readShort(stream));
  }
}
