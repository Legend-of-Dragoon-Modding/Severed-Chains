package legend.core.renderer;

import legend.core.lang.RawText;
import legend.game.ui.GameOverlay;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class Texture {
  private static final Logger LOGGER = LogManager.getFormatterLogger(Texture.class);

  public static Texture create(final String name, final Consumer<TextureBuilder> callback) {
    final TextureBuilder builder = new TextureBuilder(name);
    callback.accept(builder);
    final Texture texture = builder.build();
    builder.free();
    return texture;
  }

  public static Texture empty(final String name, final int w, final int h) {
    return Texture.create(name, builder -> {
      builder.size(w, h);
      builder.internalFormat(TextureInternalFormat.RGBA_8);
      builder.dataFormat(TextureDataFormat.RGBA);
    });
  }

  public static Texture filteredEmpty(final String name, final int w, final int h) {
    return Texture.create(name, builder -> {
      builder.size(w, h);
      builder.internalFormat(TextureInternalFormat.RGBA_8);
      builder.dataFormat(TextureDataFormat.RGBA);
      builder.minFilter(true);
      builder.magFilter(true);
    });
  }

  public static Texture png(final String name, final ByteBuffer data) {
    return Texture.create(name, builder -> {
      builder.internalFormat(TextureInternalFormat.RGBA_8);
      builder.dataFormat(TextureDataFormat.RGBA);
      builder.dataType(TextureDataType.UBYTE);
      builder.png(data);
    });
  }

  public static Texture png(final String name, final Path path) {
    return Texture.create(name, builder -> {
      builder.internalFormat(TextureInternalFormat.RGBA_8);
      builder.dataFormat(TextureDataFormat.RGBA);
      builder.dataType(TextureDataType.UBYTE);
      builder.png(path);
    });
  }

  public static Texture filteredPng(final String name, final Path path) {
    return Texture.create(name, builder -> {
      builder.internalFormat(TextureInternalFormat.RGBA_8);
      builder.dataFormat(TextureDataFormat.RGBA);
      builder.dataType(TextureDataType.UBYTE);
      builder.minFilter(true);
      builder.magFilter(true);
      builder.png(path);
    });
  }

  public static Texture copyAttributesFrom(final String name, final Texture other) {
    return Texture.create(name, builder -> {
      builder.size(other.width, other.height);
      builder.internalFormat(other.internalFormat());
      builder.dataFormat(other.dataFormat());
      builder.dataType(other.dataType());
      builder.minFilter(other.minFilter());
      builder.magFilter(other.magFilter());
      builder.wrapS(other.wrapS());
      builder.wrapT(other.wrapT());
    });
  }

  public final String name;
  public final int width;
  public final int height;

  protected Texture(final String name, final int width, final int height) {
    this.name = name;
    this.width = width;
    this.height = height;
    texList.add(this);
  }

  public abstract void data(int x, int y, int w, int h, TextureDataType dataType, ByteBuffer data);
  public abstract void data(int x, int y, int w, int h, TextureDataType dataType, int[] data);
  public abstract void use(int activeTexture);
  public abstract void use();

  public abstract TextureInternalFormat internalFormat();
  public abstract TextureDataFormat dataFormat();
  public abstract TextureDataType dataType();
  public abstract boolean minFilter();
  public abstract boolean magFilter();
  public abstract boolean wrapS();
  public abstract boolean wrapT();

  private static final List<Texture> texList = new ArrayList<>();
  private static boolean shouldLog = true;

  /** This Obj won't be deleted on state transition */
  public boolean persistent;
  protected boolean deleted;
  protected abstract void performDelete();

  public void delete() {
    this.deleted = true;
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

  public static void setShouldLog(final boolean shouldLog) {
    Texture.shouldLog = shouldLog;
  }

  public static void clearTextureList(final boolean clearPersistent) {
    for(int i = texList.size() - 1; i >= 0; i--) {
      final Texture tex = texList.get(i);

      if(!tex.deleted && (!tex.persistent || clearPersistent)) {
        if(shouldLog) {
          LOGGER.warn("Leaked: %s", tex.name);
          GameOverlay.addNotification(5, new RawText("Leaked texture: " + tex.name));
        }

        tex.delete();
        texList.remove(i);
      }
    }
  }
}
