package legend.game.textures;

import legend.core.memory.types.IntRef;
import legend.game.unpacker.ExpandableFileData;
import legend.game.unpacker.FileData;
import org.lwjgl.stb.STBIWriteCallback;
import org.lwjgl.stb.STBImage;
import org.lwjgl.stb.STBImageWrite;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public final class PngWriter {
  private PngWriter() { }

  public static byte[] compress(final ByteBuffer buffer, final int width, final int height) {
    final FileData compressed = new ExpandableFileData(buffer.capacity());
    final IntRef offset = new IntRef();
    final STBIWriteCallback callback = STBIWriteCallback.create((context, data, size) -> accumulatePngData(compressed, offset, data, size));
    STBImageWrite.stbi_write_png_to_func(callback, 0L, width, height, STBImage.STBI_rgb_alpha, buffer, width * 4);
    callback.free();

    final byte[] out = new byte[offset.get()];
    compressed.read(0, out, 0, out.length);
    return out;
  }

  public static void write(final Path path, final ByteBuffer data, final int width, final int height) throws IOException {
    final byte[] compressed = compress(data, width, height);
    Files.write(path, compressed, StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
  }

  private static void accumulatePngData(final FileData out, final IntRef offset, final long data, final int size) {
    final ByteBuffer newData = STBIWriteCallback.getData(data, size);
    out.write(0, newData, offset.get(), newData.limit());
    offset.add(newData.limit());
  }
}
