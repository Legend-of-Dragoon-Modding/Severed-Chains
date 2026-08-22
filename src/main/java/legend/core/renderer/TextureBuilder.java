package legend.core.renderer;

import org.lwjgl.system.MemoryStack;

import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static legend.core.GameEngine.RENDERER;
import static legend.core.IoHelper.pathToByteBuffer;
import static org.lwjgl.stb.STBImage.stbi_failure_reason;
import static org.lwjgl.stb.STBImage.stbi_load_from_memory;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.memFree;

public class TextureBuilder {
  private final String name;

  @Nullable
  private Buffer buffer;
  private int w;
  private int h;

  private TextureInternalFormat internalFormat = TextureInternalFormat.RGBA_8;
  private TextureDataFormat dataFormat = TextureDataFormat.RGBA;
  private TextureDataType dataType = TextureDataType.UBYTE;

  private boolean minFilter;
  private boolean magFilter;

  private boolean wrapS = true;
  private boolean wrapT = true;

  private final List<Runnable> cleanup = new ArrayList<>();

  public TextureBuilder(final String name) {
    this.name = name;
  }

  public void free() {
    for(final Runnable runnable : this.cleanup) {
      runnable.run();
    }
  }

  public void png(final Path path) {
    final ByteBuffer imageBuffer;
    try {
      imageBuffer = pathToByteBuffer(path);
    } catch(final IOException e) {
      throw new RuntimeException(e);
    }

    this.png(imageBuffer);
  }

  public void png(final ByteBuffer imageBuffer) {
    try(final MemoryStack stack = stackPush()) {
      final IntBuffer w = stack.mallocInt(1);
      final IntBuffer h = stack.mallocInt(1);
      final IntBuffer comp = stack.mallocInt(1);

      final ByteBuffer data = stbi_load_from_memory(imageBuffer, w, h, comp, 4);
      if(data == null) {
        throw new RuntimeException("Failed to load image: " + stbi_failure_reason());
      }

      this.data(data, w.get(0), h.get(0));

      this.cleanup.add(() -> memFree(data));
    }
  }

  public void size(final int w, final int h) {
    this.w = w;
    this.h = h;
  }

  public void data(final Buffer data, final int w, final int h) {
    this.buffer = data;
    this.size(w, h);
  }

  public void internalFormat(final TextureInternalFormat format) {
    this.internalFormat = format;
  }

  public void dataFormat(final TextureDataFormat format) {
    this.dataFormat = format;
  }

  public void dataType(final TextureDataType dataType) {
    this.dataType = dataType;
  }

  public void minFilter(final boolean minFilter) {
    this.minFilter = minFilter;
  }

  public void magFilter(final boolean magFilter) {
    this.magFilter = magFilter;
  }

  public void wrapS(final boolean wrapS) {
    this.wrapS = wrapS;
  }

  public void wrapT(final boolean wrapT) {
    this.wrapT = wrapT;
  }

  Texture build() {
    return RENDERER.api().makeTexture(this.buffer, this.name, this.w, this.h, this.internalFormat, this.dataFormat, this.dataType, this.minFilter, this.magFilter, this.wrapS, this.wrapT);
  }
}
