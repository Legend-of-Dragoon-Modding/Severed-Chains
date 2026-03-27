package legend.core.opengl;

import legend.core.memory.types.TriConsumer;
import legend.game.textures.Image;

import javax.annotation.Nullable;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static org.lwjgl.opengl.GL11C.GL_LINEAR;
import static org.lwjgl.opengl.GL11C.GL_NEAREST;
import static org.lwjgl.opengl.GL11C.GL_NO_ERROR;
import static org.lwjgl.opengl.GL11C.GL_RGBA;
import static org.lwjgl.opengl.GL11C.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11C.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11C.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11C.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11C.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11C.glBindTexture;
import static org.lwjgl.opengl.GL11C.glDeleteTextures;
import static org.lwjgl.opengl.GL11C.glGenTextures;
import static org.lwjgl.opengl.GL11C.glGetError;
import static org.lwjgl.opengl.GL11C.glTexImage2D;
import static org.lwjgl.opengl.GL11C.glTexParameteri;
import static org.lwjgl.opengl.GL11C.glTexSubImage2D;
import static org.lwjgl.opengl.GL13C.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13C.glActiveTexture;

public final class Texture {
  public static Texture create(final Consumer<Builder> callback) {
    final Builder builder = new Builder();
    callback.accept(builder);
    return builder.build();
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

  public static Texture fromImage(final Image image) {
    return Texture.create(builder -> {
      builder.internalFormat(GL_RGBA);
      builder.dataFormat(GL_RGBA);
      builder.dataType(GL_UNSIGNED_BYTE);
      builder.minFilter(GL_NEAREST);
      builder.magFilter(GL_NEAREST);
      builder.data(image.getBuffer(), image.width, image.height);
    });
  }

  public static Texture fromImageFiltered(final Image image) {
    return Texture.create(builder -> {
      builder.internalFormat(GL_RGBA);
      builder.dataFormat(GL_RGBA);
      builder.dataType(GL_UNSIGNED_BYTE);
      builder.minFilter(GL_LINEAR);
      builder.magFilter(GL_LINEAR);
      builder.data(image.getBuffer(), image.width, image.height);
    });
  }

  public static Texture copyAttributesFrom(final Texture other) {
    return Texture.create(builder -> {
      builder.size(other.width, other.height);
      builder.internalFormat(other.internalFormat);
      builder.dataFormat(other.dataFormat);
      builder.dataType(other.dataType);
      builder.minFilter(other.minFilter);
      builder.magFilter(other.magFilter);
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

  public final int internalFormat;
  public final int dataFormat;
  public final int dataType;

  public final int minFilter;
  public final int magFilter;

  private boolean deleted;

  private Texture(@Nullable final TriConsumer<Integer, Integer, Integer> texImage2d, final int w, final int h, final int internalFormat, final int dataFormat, final int dataType, final int minFilter, final int magFilter) {
    this.id = glGenTextures();
    this.width = w;
    this.height = h;
    this.internalFormat = internalFormat;
    this.dataFormat = dataFormat;
    this.dataType = dataType;
    this.minFilter = minFilter;
    this.magFilter = magFilter;
    this.use();

    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, minFilter);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, magFilter);

    if(texImage2d != null) {
      texImage2d.accept(internalFormat, dataFormat, dataType);
    }

    final int error = glGetError();
    if(error != GL_NO_ERROR) {
      throw new RuntimeException("Failed to create texture, glError: " + Integer.toString(error, 16));
    }

    texList.add(this);
  }

  public void data(final int x, final int y, final int w, final int h, final ByteBuffer data) {
    this.use();
    glTexSubImage2D(GL_TEXTURE_2D, 0, x, y, w, h, this.dataFormat, GL_UNSIGNED_BYTE, data);

    final int error = glGetError();
    if(error != GL_NO_ERROR) {
      throw new RuntimeException("Failed to upload data, rect: (" + x + ", " + y + ", " + w + ", " + h + "), glError: " + Integer.toString(error, 16));
    }
  }

  public void data(final int x, final int y, final int w, final int h, final int[] data) {
    this.use();
    glTexSubImage2D(GL_TEXTURE_2D, 0, x, y, w, h, this.dataFormat, GL_UNSIGNED_BYTE, data);

    final int error = glGetError();
    if(error != GL_NO_ERROR) {
      throw new RuntimeException("Failed to upload data, rect: (" + x + ", " + y + ", " + w + ", " + h + "), glError: " + Integer.toString(error, 16));
    }
  }

  public void dataInt(final int x, final int y, final int w, final int h, final int[] data) {
    this.use();
    glTexSubImage2D(GL_TEXTURE_2D, 0, x, y, w, h, this.dataFormat, GL_UNSIGNED_INT, data);
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

    private int internalFormat = GL_RGBA;
    private int dataFormat = GL_RGBA;
    private int dataType = GL_UNSIGNED_BYTE;

    private int minFilter = GL_NEAREST;
    private int magFilter = GL_NEAREST;

    Builder() {}

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

    Texture build() {
      return new Texture(this.texImage2d, this.w, this.h, this.internalFormat, this.dataFormat, this.dataType, this.minFilter, this.magFilter);
    }
  }
}
