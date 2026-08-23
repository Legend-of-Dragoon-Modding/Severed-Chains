package legend.core.renderer.noop;

import legend.core.renderer.Texture;
import legend.core.renderer.TextureDataFormat;
import legend.core.renderer.TextureDataType;
import legend.core.renderer.TextureInternalFormat;

import java.nio.ByteBuffer;

public final class NoopTexture extends Texture {
  public final TextureInternalFormat internalFormat;
  public final TextureDataFormat dataFormat;
  public final TextureDataType dataType;

  public final boolean minFilter;
  public final boolean magFilter;
  public final boolean wrapS;
  public final boolean wrapT;

  NoopTexture(final String name, final int w, final int h, final TextureInternalFormat internalFormat, final TextureDataFormat dataFormat, final TextureDataType dataType, final boolean minFilter, final boolean magFilter, final boolean wrapS, final boolean wrapT) {
    super(name, w, h);
    this.internalFormat = internalFormat;
    this.dataFormat = dataFormat;
    this.dataType = dataType;
    this.minFilter = minFilter;
    this.magFilter = magFilter;
    this.wrapS = wrapS;
    this.wrapT = wrapT;
  }

  @Override
  public void data(final int x, final int y, final int w, final int h, final TextureDataType dataType, final ByteBuffer data) {

  }

  @Override
  public void data(final int x, final int y, final int w, final int h, final TextureDataType dataType, final int[] data) {

  }

  @Override
  public void use(final int activeTexture) {

  }

  @Override
  public void use() {

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

  }
}
