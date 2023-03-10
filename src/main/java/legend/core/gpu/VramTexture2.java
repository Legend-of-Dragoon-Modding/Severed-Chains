package legend.core.gpu;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class VramTexture2 {
  public final int width;
  public final int height;
  public final int vramX;
  public final int vramY;
  private final int[] data;

  public VramTexture2(final int width, final int height, final int vramX, final int vramY, final int[] data) {
    this.width = width;
    this.height = height;
    this.vramX = vramX;
    this.vramY = vramY;
    this.data = data;
  }

  public int getTexel(final VramTexture2 palette, final int x, final int y) {
    if(x >= this.width) {
      throw new IllegalArgumentException("X out of bounds (%d >= %d)".formatted(x, this.width));
    }

    if(y >= this.height) {
      throw new IllegalArgumentException("Y out of bounds (%d >= %d)".formatted(y, this.height));
    }

    return palette.getPixel(this.getPixel(x, y), 0);
  }

  public int getPixel(final int x, final int y) {
    if(x >= this.width) {
      throw new IllegalArgumentException("X out of bounds (%d >= %d)".formatted(x, this.width));
    }

    if(y >= this.height) {
      throw new IllegalArgumentException("Y out of bounds (%d >= %d)".formatted(y, this.height));
    }

    return this.data[y * this.width + x];
  }

  public void copyRow(final int y, final int[] dest, final int destOffset) {
    if(y >= this.height) {
      throw new IllegalArgumentException("Y out of bounds (%d >= %d)".formatted(y, this.height));
    }

    System.arraycopy(this.data, y * this.width, dest, destOffset, this.width);
  }

  public void getRegion(final Rect4i region, final int[] dest) {
    for(int y = 0; y < region.h(); y++) {
      System.arraycopy(this.data, (region.y() + y - this.vramY) * this.width + region.x() - this.vramX, dest, y * region.w(), region.w());
    }
  }

  public void setRegion(final Rect4i region, final int[] src) {
    for(int y = 0; y < region.h(); y++) {
      System.arraycopy(src, y * region.w(), this.data, (region.y() + y - this.vramY) * this.width + region.x() - this.vramX, region.w());
    }
  }

  public void dumpToFile() {
    final BufferedImage image = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_RGB);

    image.setRGB(0, 0, this.width, this.height, this.data, 0, this.width);

    try {
      ImageIO.write(image, "png", new File("dump.png"));
    } catch(final IOException e) {
      throw new RuntimeException(e);
    }
  }
}
