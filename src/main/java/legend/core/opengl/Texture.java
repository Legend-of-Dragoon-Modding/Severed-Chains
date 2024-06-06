package legend.core.opengl;

import legend.core.memory.types.TriConsumer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.system.MemoryStack;

import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static legend.core.IoHelper.pathToByteBuffer;
import static org.lwjgl.opengl.GL11C.GL_LINEAR;
import static org.lwjgl.opengl.GL11C.GL_NEAREST;
import static org.lwjgl.opengl.GL11C.GL_NO_ERROR;
import static org.lwjgl.opengl.GL11C.GL_REPEAT;
import static org.lwjgl.opengl.GL11C.GL_RGB;
import static org.lwjgl.opengl.GL11C.GL_RGBA;
import static org.lwjgl.opengl.GL11C.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11C.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11C.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11C.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11C.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11C.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11C.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11C.glBindTexture;
import static org.lwjgl.opengl.GL11C.glDeleteTextures;
import static org.lwjgl.opengl.GL11C.glGenTextures;
import static org.lwjgl.opengl.GL11C.glGetError;
import static org.lwjgl.opengl.GL11C.glTexImage2D;
import static org.lwjgl.opengl.GL11C.glTexParameteri;
import static org.lwjgl.opengl.GL11C.glTexSubImage2D;
import static org.lwjgl.opengl.GL12C.GL_TEXTURE_MAX_LEVEL;
import static org.lwjgl.opengl.GL12C.GL_UNSIGNED_INT_8_8_8_8_REV;
import static org.lwjgl.opengl.GL13C.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13C.glActiveTexture;
import static org.lwjgl.opengl.GL21C.GL_SRGB_ALPHA;
import static org.lwjgl.opengl.GL30C.glGenerateMipmap;
import static org.lwjgl.stb.STBImage.stbi_failure_reason;
import static org.lwjgl.stb.STBImage.stbi_load_from_memory;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.memFree;

public final class Texture {
  private static final Logger LOGGER = LogManager.getFormatterLogger(Texture.class);

  public static Texture create(final Consumer<Builder> callback) {
    final Builder builder = new Builder();
    callback.accept(builder);
    final Texture texture = builder.build();
    builder.free();
    return texture;
  }

  public static Texture empty(final int w, final int h) {
    return Texture.create(builder -> {
      builder.size(w, h);
      builder.internalFormat(GL_RGBA);
      builder.dataFormat(GL_RGBA);
      builder.minFilter(GL_NEAREST);
      builder.magFilter(GL_NEAREST);
    });
  }

  public static Texture filteredEmpty(final int w, final int h) {
    return Texture.create(builder -> {
      builder.size(w, h);
      builder.internalFormat(GL_RGBA);
      builder.dataFormat(GL_RGBA);
      builder.minFilter(GL_LINEAR);
      builder.magFilter(GL_LINEAR);
    });
  }

  public static Texture png(final Path path) {
    return Texture.create(builder -> {
      builder.internalFormat(GL_RGBA);
      builder.dataFormat(GL_RGBA);
      builder.dataType(GL_UNSIGNED_INT_8_8_8_8_REV);
      builder.minFilter(GL_NEAREST);
      builder.magFilter(GL_NEAREST);
      builder.png(path);
    });
  }

  public static Texture png(final ByteBuffer data) {
    return Texture.create(builder -> {
      builder.internalFormat(GL_RGBA);
      builder.dataFormat(GL_RGBA);
      builder.dataType(GL_UNSIGNED_INT_8_8_8_8_REV);
      builder.minFilter(GL_NEAREST);
      builder.magFilter(GL_NEAREST);
      builder.png(data);
    });
  }

  public static Texture filteredPng(final Path path) {
    return Texture.create(builder -> {
      builder.internalFormat(GL_RGBA);
      builder.dataFormat(GL_RGBA);
      builder.dataType(GL_UNSIGNED_INT_8_8_8_8_REV);
      builder.minFilter(GL_LINEAR);
      builder.magFilter(GL_LINEAR);
      builder.png(path);
    });
  }

  public static void unbind() {
    for(int i = 0; i < currentTextures.length; i++) {
      if(currentTextures[i] != 0) {
        currentTextures[i] = 0;
        glActiveTexture(GL_TEXTURE0 + i);
        glBindTexture(GL_TEXTURE_2D, 0);
      }
    }
  }

  private static final List<Texture> texList = new ArrayList<>();
  private static final int[] currentTextures = new int[32];

  final int id;

  public final int width;
  public final int height;

  private final int dataFormat;

  private boolean deleted;

  private Texture(@Nullable final TriConsumer<Integer, Integer, Integer> texImage2d, final int w, final int h, final int internalFormat, final int dataFormat, final int dataType, final int minFilter, final int magFilter, final int wrapS, final int wrapT, final boolean generateMipmaps, final List<MipmapBuilder> mipmaps) {
    this.id = glGenTextures();
    this.width = w;
    this.height = h;
    this.dataFormat = dataFormat;
    this.use();

    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, minFilter);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, magFilter);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, wrapS);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, wrapT);

    if(texImage2d != null) {
      texImage2d.accept(internalFormat, dataFormat, dataType);
    }

    final int error = glGetError();
    if(error != GL_NO_ERROR) {
      throw new RuntimeException("Failed to create texture, glError: " + Long.toString(error, 16));
    }

    if(generateMipmaps) {
      glGenerateMipmap(GL_TEXTURE_2D);
    } else {
      if(!mipmaps.isEmpty()) {
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAX_LEVEL, mipmaps.size());
        mipmaps.forEach(MipmapBuilder::use);
      }
    }

    texList.add(this);
  }

  public void data(final int x, final int y, final int w, final int h, final ByteBuffer data) {
    this.use();
    glTexSubImage2D(GL_TEXTURE_2D, 0, x, y, w, h, this.dataFormat, GL_UNSIGNED_BYTE, data);

    final int error = glGetError();
    if(error != GL_NO_ERROR) {
      throw new RuntimeException("Failed to upload data, rect: (" + x + ", " + y + ", " + w + ", " + h + "), glError: " + Long.toString(error, 16));
    }
  }

  public void data(final int x, final int y, final int w, final int h, final int[] data) {
    this.use();
    glTexSubImage2D(GL_TEXTURE_2D, 0, x, y, w, h, this.dataFormat, GL_UNSIGNED_INT_8_8_8_8_REV, data);

    final int error = glGetError();
    if(error != GL_NO_ERROR) {
      throw new RuntimeException("Failed to upload data, rect: (" + x + ", " + y + ", " + w + ", " + h + "), glError: " + Long.toString(error, 16));
    }
  }

  public void dataInt(final int x, final int y, final int w, final int h, final int[] data) {
    this.use();
    glTexSubImage2D(GL_TEXTURE_2D, 0, x, y, w, h, this.dataFormat, GL_UNSIGNED_INT, data);

    final int error = glGetError();
    if(error != GL_NO_ERROR) {
      throw new RuntimeException("Failed to upload data, rect: (" + x + ", " + y + ", " + w + ", " + h + "), glError: " + Long.toString(error, 16));
    }
  }

  public void use(final int activeTexture) {
    if(currentTextures[activeTexture] != this.id) {
      currentTextures[activeTexture] = this.id;
      glActiveTexture(GL_TEXTURE0 + activeTexture);
      glBindTexture(GL_TEXTURE_2D, this.id);
    }
  }

  public void use() {
    this.use(0);
  }

  public void delete() {
    this.deleted = true;
  }

  private void performDelete() {
    glDeleteTextures(this.id);
  }

  public static void deleteTextures() {
    for(int i = texList.size() - 1; i >= 0; i--) {
      final Texture tex = texList.get(i);

      if(tex.deleted) {
        tex.performDelete();
        texList.remove(i);
      }
    }
  }

  public static class Builder {
    private TriConsumer<Integer, Integer, Integer> texImage2d = (internalFormat, dataFormat, dataType) -> glTexImage2D(GL_TEXTURE_2D, 0, internalFormat, this.w, this.h, 0, dataFormat, dataType, (ByteBuffer)null);
    private int w;
    private int h;

    private int internalFormat = GL_SRGB_ALPHA;
    private int dataFormat = GL_RGB;
    private int dataType = GL_UNSIGNED_BYTE;

    private int minFilter = GL_NEAREST;
    private int magFilter = GL_NEAREST;

    private int wrapS = GL_REPEAT;
    private int wrapT = GL_REPEAT;

    private boolean generateMipmaps;
    private final List<MipmapBuilder> mipmaps = new ArrayList<>();

    private final List<Runnable> cleanup = new ArrayList<>();

    Builder() {}

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

    public void data(final ByteBuffer data, final int w, final int h) {
      this.texImage2d = (internalFormat, dataFormat, dataType) -> glTexImage2D(GL_TEXTURE_2D, 0, internalFormat, w, h, 0, dataFormat, dataType, data);
      this.size(w, h);
    }

    public void data(final FloatBuffer data, final int w, final int h) {
      this.texImage2d = (internalFormat, dataFormat, dataType) -> glTexImage2D(GL_TEXTURE_2D, 0, internalFormat, w, h, 0, dataFormat, dataType, data);
      this.size(w, h);
    }

    public void data(final int[] data, final int w, final int h) {
      this.texImage2d = (internalFormat, dataFormat, dataType) -> glTexImage2D(GL_TEXTURE_2D, 0, internalFormat, w, h, 0, dataFormat, dataType, data);
      this.size(w, h);
    }

    public void internalFormat(final int format) {
      this.internalFormat = format;
    }

    public void dataFormat(final int format) {
      this.dataFormat = format;
    }

    public void dataType(final int dataType) {
      this.dataType = dataType;
    }

    public void minFilter(final int minFilter) {
      this.minFilter = minFilter;
    }

    public void magFilter(final int magFilter) {
      this.magFilter = magFilter;
    }

    public void wrapS(final int wrapS) {
      this.wrapS = wrapS;
    }

    public void wrapT(final int wrapT) {
      this.wrapT = wrapT;
    }

    public void generateMipmaps() {
      this.generateMipmaps = true;
    }

    public void mipmap(final int level, final Consumer<MipmapBuilder> callback) {
      final MipmapBuilder builder = new MipmapBuilder(level);

      callback.accept(builder);
      this.mipmaps.add(builder);
    }

    Texture build() {
      return new Texture(this.texImage2d, this.w, this.h, this.internalFormat, this.dataFormat, this.dataType, this.minFilter, this.magFilter, this.wrapS, this.wrapT, this.generateMipmaps, this.mipmaps);
    }
  }

  public static class MipmapBuilder {
    private final int level;

    @Nullable
    private ByteBuffer data;
    private int w;
    private int h;

    private int dataFormat = GL_SRGB_ALPHA;
    private int pixelFormat = GL_RGBA;
    private int dataType = GL_UNSIGNED_BYTE;

    MipmapBuilder(final int level) {
      this.level = level;
    }

    public void png(final Path file) {
      final ByteBuffer imageBuffer;
      try {
        imageBuffer = pathToByteBuffer(file);
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

        this.data(data, w.get(0), h.get(0));
      }
    }

    public void data(@Nullable final ByteBuffer data, final int w, final int h) {
      this.data = data;
      this.w = w;
      this.h = h;
    }

    public void dataFormat(final int format) {
      this.dataFormat = format;
    }

    public void pixelFormat(final int format) {
      this.pixelFormat = format;
    }

    public void dataType(final int dataType) {
      this.dataType = dataType;
    }

    void use() {
      glTexImage2D(GL_TEXTURE_2D, this.level, this.dataFormat, this.w, this.h, 0, this.pixelFormat, this.dataType, this.data);
    }
  }
}
