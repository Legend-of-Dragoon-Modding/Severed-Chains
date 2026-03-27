package legend.game.textures;

import legend.core.memory.types.IntRef;
import legend.game.unpacker.ExpandableFileData;
import legend.game.unpacker.FileData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.stb.STBIWriteCallback;
import org.lwjgl.stb.STBImage;
import org.lwjgl.stb.STBImageWrite;
import org.lwjgl.system.MemoryStack;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import static legend.core.IoHelper.pathToByteBuffer;
import static org.lwjgl.stb.STBImage.stbi_failure_reason;
import static org.lwjgl.stb.STBImage.stbi_load_from_memory;
import static org.lwjgl.system.MemoryStack.stackPush;

public final class ImageLoader {
  private ImageLoader() { }

  private static final Logger LOGGER = LogManager.getFormatterLogger(ImageLoader.class);

  private static final ArrayImage INVALID = new ArrayImage(new byte[] {-1, -1, 0, -1, 0, 0, 0, 0, 0, 0, 0, 0, -1, -1, 0, -1}, 2, 2);

  public static Image safeLoadImage(final Path path) {
    try {
      return loadImage(path);
    } catch(final IOException e) {
      LOGGER.warn("Failed to load image " + path, e);
      return INVALID;
    }
  }

  public static BufferImage loadImage(final Path path) throws IOException {
    return loadImage(pathToByteBuffer(path));
  }

  public static Image safeLoadImage(final ByteBuffer buffer) {
    try {
      return loadImage(buffer);
    } catch(final IOException e) {
      LOGGER.warn("Failed to load image", e);
      return INVALID;
    }
  }

  public static BufferImage loadImage(final ByteBuffer buffer) throws IOException {
    try(final MemoryStack stack = stackPush()) {
      final IntBuffer w = stack.mallocInt(1);
      final IntBuffer h = stack.mallocInt(1);
      final IntBuffer comp = stack.mallocInt(1);

      final ByteBuffer decompressed = stbi_load_from_memory(buffer, w, h, comp, 4);
      if(decompressed == null) {
        throw new IOException("Failed to load image: " + stbi_failure_reason());
      }

      return new BufferImage(decompressed, w.get(0), h.get(0));
    }
  }

  public static byte[] compressPng(final ByteBuffer buffer, final int width, final int height) {
    final FileData compressed = new ExpandableFileData(buffer.capacity());
    final IntRef offset = new IntRef();
    final STBIWriteCallback callback = STBIWriteCallback.create((context, data, size) -> accumulatePngData(compressed, offset, data, size));
    STBImageWrite.stbi_write_png_to_func(callback, 0L, width, height, STBImage.STBI_rgb_alpha, buffer, width * 4);
    callback.free();

    final byte[] out = new byte[offset.get()];
    compressed.read(0, out, 0, out.length);
    return out;
  }

  public static void writePng(final Path path, final ByteBuffer data, final int width, final int height) throws IOException {
    final byte[] compressed = compressPng(data, width, height);
    Files.write(path, compressed, StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
  }

  private static void accumulatePngData(final FileData out, final IntRef offset, final long data, final int size) {
    final ByteBuffer newData = STBIWriteCallback.getData(data, size);
    out.write(0, newData, offset.get(), newData.limit());
    offset.add(newData.limit());
  }
}
