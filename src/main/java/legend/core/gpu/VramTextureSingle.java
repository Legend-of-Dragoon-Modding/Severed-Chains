package legend.core.gpu;

import legend.core.opengl.Texture;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

import static org.lwjgl.opengl.GL11C.GL_RGBA;
import static org.lwjgl.opengl.GL12C.GL_UNSIGNED_INT_8_8_8_8_REV;

public class VramTextureSingle extends VramTexture {
  private final int[] data;

  public VramTextureSingle(final Bpp bpp, final Rect4i rect, final int[] data) {
    super(bpp, rect);
    this.data = data;
  }

  @Override
  public int getTexel(final VramTexture palette, final int pageX, final int x, final int y) {
    return palette.getPixel(this.getTexel(pageX, x, y), 0);
  }

  @Override
  public int getTexel(final int pageX, final int x, final int y) {
    final int textureOffset = (this.rect.x() - pageX) * this.bpp.widthDivisor;
    final int textureX = x - textureOffset;

//    this.checkBounds(textureX, y);
    return this.getPixel(textureX, y);
  }

  @Override
  public int getPixel(final int x, final int y) {
//    this.checkBounds(x, y);
    return this.data[y * this.rect.w() + x];
  }

  @Override
  public void setPixel(final int x, final int y, final int colour) {
//    this.checkBounds(x, y);
    this.data[y * this.rect.w() + x] = colour;
  }

  @Override
  public void copyRow(final int y, final int[] dest, final int destOffset) {
//    this.checkBounds(0, y);
    System.arraycopy(this.data, y * this.rect.w(), dest, destOffset, this.rect.w());
  }

  @Override
  public void getRegion(final Rect4i region, final int[] dest) {
    for(int y = 0; y < region.h(); y++) {
      System.arraycopy(this.data, (region.y() + y - this.rect.y()) * this.rect.w() + region.x() - this.rect.x(), dest, y * region.w(), region.w());
    }
  }

  @Override
  public void setRegion(final Rect4i region, final int[] src) {
    for(int y = 0; y < region.h(); y++) {
      System.arraycopy(src, y * region.w(), this.data, (region.y() + y - this.rect.y()) * this.rect.w() + region.x() - this.rect.x(), region.w());
    }
  }

  @Override
  public void fill(final int colour) {
    Arrays.fill(this.data, colour);
  }

  public int[] getData() {
    return this.data;
  }

  private void checkBounds(final int x, final int y) {
    if(x < 0) {
      throw new IllegalArgumentException("X out of bounds (%d < 0)".formatted(x));
    }

    if(y < 0) {
      throw new IllegalArgumentException("Y out of bounds (%d < 0)".formatted(y));
    }

    if(x >= this.rect.w()) {
      throw new IllegalArgumentException("X out of bounds (%d >= %d)".formatted(x, this.rect.w()));
    }

    if(y >= this.rect.h()) {
      throw new IllegalArgumentException("Y out of bounds (%d >= %d)".formatted(y, this.rect.h()));
    }
  }

  public int[] applyPalette(final VramTextureSingle palette, final Rect4i region) {
    final int[] paletteData = palette.getData();
    final int[] data = this.getData();
    final int[] newData = new int[region.w * region.h];
    int i = 0;

    for(int y = 0; y < region.h; y++) {
      for(int x = 0; x < region.w; x++) {
        newData[i++] = paletteData[data[(region.y + y) * this.rect.w + region.x + x]];
      }
    }

    return newData;
  }

  public Texture createOpenglTexture(final VramTextureSingle palette, final Rect4i region) {
    return Texture.create(builder -> {
      builder.data(this.applyPalette(palette, region), this.rect.w(), this.rect.h());
      builder.internalFormat(GL_RGBA);
      builder.dataFormat(GL_RGBA);
      builder.dataType(GL_UNSIGNED_INT_8_8_8_8_REV);
    });
  }

  public Texture createOpenglTexture(final VramTextureSingle palette) {
    return this.createOpenglTexture(palette, new Rect4i(0, 0, this.rect.w, this.rect.h));
  }

  public static void dumpToFile(final Rect4i rect, final int[] data, final Path path) {
    final BufferedImage image = new BufferedImage(rect.w(), rect.h(), BufferedImage.TYPE_INT_RGB);

    final int[] newData = Arrays.copyOf(data, data.length);
    for(int i = 0; i < newData.length; i++) {
      final int b = newData[i]        & 0xff;
      final int g = newData[i] >>>  8 & 0xff;
      final int r = newData[i] >>> 16 & 0xff;
      final int a = newData[i] >>> 24 & 0xff;
      newData[i] = a << 24 | b << 16 | g << 8 | r;
    }

    image.setRGB(0, 0, rect.w(), rect.h(), newData, 0, rect.w());

    try {
      Files.deleteIfExists(path);
      ImageIO.write(image, "png", path.toFile());
    } catch(final IOException e) {
      throw new RuntimeException(e);
    }
  }
}
