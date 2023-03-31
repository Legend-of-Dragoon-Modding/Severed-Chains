package legend.core.gpu;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

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
    final int textureOffset = (this.rect.x() - pageX) * this.bpp.widthScale;
    final int textureX = x - textureOffset;

    if(textureX >= this.rect.w()) {
      throw new IllegalArgumentException("X out of bounds (%d >= %d)".formatted(x, this.rect.w()));
    }

    if(y >= this.rect.h()) {
      throw new IllegalArgumentException("Y out of bounds (%d >= %d)".formatted(y, this.rect.h()));
    }

    return this.getPixel(textureX, y);
  }

  @Override
  public int getPixel(final int x, final int y) {
    if(x >= this.rect.w()) {
      throw new IllegalArgumentException("X out of bounds (%d >= %d)".formatted(x, this.rect.w()));
    }

    if(y >= this.rect.h()) {
      throw new IllegalArgumentException("Y out of bounds (%d >= %d)".formatted(y, this.rect.h()));
    }

    return this.data[y * this.rect.w() + x];
  }

  @Override
  public void copyRow(final int y, final int[] dest, final int destOffset) {
    if(y >= this.rect.h()) {
      throw new IllegalArgumentException("Y out of bounds (%d >= %d)".formatted(y, this.rect.h()));
    }

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

  public void dumpToFile() {
    final BufferedImage image = new BufferedImage(this.rect.w(), this.rect.h(), BufferedImage.TYPE_INT_RGB);

    image.setRGB(0, 0, this.rect.w(), this.rect.h(), this.data, 0, this.rect.w());

    try {
      ImageIO.write(image, "png", new File("dump.png"));
    } catch(final IOException e) {
      throw new RuntimeException(e);
    }
  }
}
