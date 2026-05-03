package legend.game.textures;

import org.lwjgl.system.MemoryStack;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.file.Path;

import static legend.core.IoHelper.pathToByteBuffer;
import static org.lwjgl.stb.STBImage.stbi_failure_reason;
import static org.lwjgl.stb.STBImage.stbi_load_from_memory;
import static org.lwjgl.system.MemoryStack.stackPush;

public class Image {
  public final byte[] data;
  public final int width;
  public final int height;

  public Image(final byte[] data, final int width, final int height) {
    this.data = data;
    this.width = width;
    this.height = height;
  }

  public static Image load(final Path path) {
    final ByteBuffer imageBuffer;
    try {
      imageBuffer = pathToByteBuffer(path);
    } catch(final IOException e) {
      throw new RuntimeException(e);
    }

    try(final MemoryStack stack = stackPush()) {
      final IntBuffer w = stack.mallocInt(1);
      final IntBuffer h = stack.mallocInt(1);
      final IntBuffer comp = stack.mallocInt(1);

      final ByteBuffer data = stbi_load_from_memory(imageBuffer, w, h, comp, 4);
      if(data == null) {
        throw new RuntimeException("Failed to load image: " + stbi_failure_reason());
      }

      final byte[] decompressed = new byte[data.limit()];
      data.get(0, decompressed);

      return new Image(decompressed, w.get(0), h.get(0));
    }
  }
}
