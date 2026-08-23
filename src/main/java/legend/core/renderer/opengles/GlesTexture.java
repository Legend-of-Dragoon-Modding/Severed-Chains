package legend.core.renderer.opengles;

import legend.core.lang.RawText;
import legend.core.renderer.Texture;
import legend.core.renderer.TextureDataFormat;
import legend.core.renderer.TextureDataType;
import legend.core.renderer.TextureInternalFormat;
import legend.game.ui.GameOverlay;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.nio.Buffer;
import java.nio.ByteBuffer;

import static org.lwjgl.opengles.GLES20.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengles.GLES20.GL_DEPTH_COMPONENT;
import static org.lwjgl.opengles.GLES20.GL_FLOAT;
import static org.lwjgl.opengles.GLES20.GL_LINEAR;
import static org.lwjgl.opengles.GLES20.GL_NEAREST;
import static org.lwjgl.opengles.GLES20.GL_NO_ERROR;
import static org.lwjgl.opengles.GLES20.GL_REPEAT;
import static org.lwjgl.opengles.GLES20.GL_RGB;
import static org.lwjgl.opengles.GLES20.GL_RGBA;
import static org.lwjgl.opengles.GLES20.GL_TEXTURE0;
import static org.lwjgl.opengles.GLES20.GL_TEXTURE_2D;
import static org.lwjgl.opengles.GLES20.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengles.GLES20.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengles.GLES20.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengles.GLES20.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengles.GLES20.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengles.GLES20.GL_UNSIGNED_INT;
import static org.lwjgl.opengles.GLES20.glActiveTexture;
import static org.lwjgl.opengles.GLES20.glBindTexture;
import static org.lwjgl.opengles.GLES20.glDeleteTextures;
import static org.lwjgl.opengles.GLES20.glGenTextures;
import static org.lwjgl.opengles.GLES20.glGetError;
import static org.lwjgl.opengles.GLES20.glTexImage2D;
import static org.lwjgl.opengles.GLES20.glTexParameteri;
import static org.lwjgl.opengles.GLES20.glTexSubImage2D;
import static org.lwjgl.opengles.GLES30.GL_R32UI;
import static org.lwjgl.opengles.GLES30.GL_RED_INTEGER;
import static org.lwjgl.opengles.GLES30.GL_RGB8;
import static org.lwjgl.opengles.GLES30.GL_RGBA8;
import static org.lwjgl.system.MemoryUtil.memAddress;

public final class GlesTexture extends Texture {
  private static final Logger LOGGER = LogManager.getFormatterLogger(GlesTexture.class);

  static void unbind() {
    for(int i = 0; i < currentTextures.length; i++) {
      if(currentTextures[i] != 0) {
        currentTextures[i] = 0;
        glActiveTexture(GL_TEXTURE0 + i);
        glBindTexture(GL_TEXTURE_2D, 0);
      }
    }
  }

  private static final int[] currentTextures = new int[32];

  final int id;

  public final int width;
  public final int height;

  public final TextureInternalFormat internalFormat;
  public final TextureDataFormat dataFormat;
  public final TextureDataType dataType;

  public final boolean minFilter;
  public final boolean magFilter;
  public final boolean wrapS;
  public final boolean wrapT;

  private boolean actuallyDeleted;

  GlesTexture(@Nullable final Buffer buffer, final String name, final int w, final int h, final TextureInternalFormat internalFormat, final TextureDataFormat dataFormat, final TextureDataType dataType, final boolean minFilter, final boolean magFilter, final boolean wrapS, final boolean wrapT) {
    super(name, w, h);
    this.id = glGenTextures();
    this.width = w;
    this.height = h;
    this.internalFormat = internalFormat;
    this.dataFormat = dataFormat;
    this.dataType = dataType;
    this.minFilter = minFilter;
    this.magFilter = magFilter;
    this.wrapS = wrapS;
    this.wrapT = wrapT;
    this.use();

    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, minFilter ? GL_LINEAR : GL_NEAREST);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, magFilter ? GL_LINEAR : GL_NEAREST);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, wrapS ? GL_REPEAT : GL_CLAMP_TO_EDGE);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, wrapT ? GL_REPEAT : GL_CLAMP_TO_EDGE);

    final int internalFormatVal = switch(internalFormat) {
      case RGB_8 -> GL_RGB8;
      case RGBA_8 -> GL_RGBA8;
      case R_32_UINT -> GL_R32UI;
      case DEPTH_COMPONENT -> GL_DEPTH_COMPONENT;
    };

    if(buffer != null) {
      glTexImage2D(GL_TEXTURE_2D, 0, internalFormatVal, w, h, 0, this.getDataFormat(dataFormat), this.getDataType(dataType), memAddress(buffer));
    } else {
      glTexImage2D(GL_TEXTURE_2D, 0, internalFormatVal, w, h, 0, this.getDataFormat(dataFormat), this.getDataType(dataType), (ByteBuffer)null);
    }

    final int error = glGetError();
    if(error != GL_NO_ERROR) {
      throw new RuntimeException("Failed to create texture, glError: " + Long.toString(error, 16));
    }
  }

  private int getDataFormat(final TextureDataFormat format) {
    return switch(format) {
      case RGB -> GL_RGB;
      case RGBA -> GL_RGBA;
      case RED_INT -> GL_RED_INTEGER;
      case DEPTH_COMPONENT -> GL_DEPTH_COMPONENT;
    };
  }

  private int getDataType(final TextureDataType type) {
    return switch(type) {
      case UBYTE -> GL_UNSIGNED_BYTE;
      case UINT -> GL_UNSIGNED_INT;
      case FLOAT -> GL_FLOAT;
    };
  }

  @Override
  public void data(final int x, final int y, final int w, final int h, final TextureDataType dataType, final ByteBuffer data) {
    this.use();
    glTexSubImage2D(GL_TEXTURE_2D, 0, x, y, w, h, this.getDataFormat(this.dataFormat), this.getDataType(dataType), data);
  }

  @Override
  public void data(final int x, final int y, final int w, final int h, final TextureDataType dataType, final int[] data) {
    this.use();
    glTexSubImage2D(GL_TEXTURE_2D, 0, x, y, w, h, this.getDataFormat(this.dataFormat), this.getDataType(dataType), data);
  }

  @Override
  public void use(final int activeTexture) {
    if(this.actuallyDeleted) {
      LOGGER.warn("%s used after being deleted", this.name);
      GameOverlay.addNotification(3, new RawText("Texture " + this.name + " used after being deleted"));
    }

    if(currentTextures[activeTexture] != this.id) {
      currentTextures[activeTexture] = this.id;
      glActiveTexture(GL_TEXTURE0 + activeTexture);
      glBindTexture(GL_TEXTURE_2D, this.id);
    }
  }

  @Override
  public void use() {
    this.use(0);
  }

  @Override
  public TextureInternalFormat internalFormat() {
    return this.internalFormat;
  }

  @Override
  public TextureDataFormat dataFormat() {
    return this.dataFormat;
  }

  @Override
  public TextureDataType dataType() {
    return this.dataType;
  }

  @Override
  public boolean minFilter() {
    return this.minFilter;
  }

  @Override
  public boolean magFilter() {
    return this.magFilter;
  }

  @Override
  public boolean wrapS() {
    return this.wrapS;
  }

  @Override
  public boolean wrapT() {
    return this.wrapT;
  }

  @Override
  protected void performDelete() {
    this.actuallyDeleted = true;
    glDeleteTextures(this.id);
  }
}
