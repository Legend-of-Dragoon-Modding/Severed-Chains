package legend.core.gpu;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class VramTexture {
  public final int palettes;
  public final int width;
  public final int height;
  private final int[][] data;
  public final PaletteRegion[] paletteRegions;

  public VramTexture(final int width, final int height, final int[][] data, final PaletteRegion... paletteRegions) {
    this.palettes = data.length;
    this.width = width;
    this.height = height;
    this.paletteRegions = paletteRegions;
    this.data = data;
  }

  public int getTexel(final int palette, final int x, final int y) {
    if(x >= this.width) {
      throw new IllegalArgumentException("X out of bounds (%d >= %d)".formatted(x, this.width));
    }

    if(y >= this.height) {
      throw new IllegalArgumentException("Y out of bounds (%d >= %d)".formatted(y, this.height));
    }

    return this.data[palette - this.getPaletteOffset(x, y)][y * this.width + x];
  }

  public void copyRow(final int palette, final int y, final int[] dest, final int destOffset) {
    if(y >= this.height) {
      throw new IllegalArgumentException("Y out of bounds (%d >= %d)".formatted(y, this.height));
    }

    System.arraycopy(this.data[palette], y * this.width, dest, destOffset, this.width);
  }

  public void getRegion(final int palette, final Rect4i region, final int[] dest) {
    for(int y = 0; y < region.h(); y++) {
      System.arraycopy(this.data[palette], (region.y() + y) * this.width, dest, y * region.w(), region.w());
    }
  }

  public void setRegion(final int palette, final Rect4i region, final int[] src) {
    for(int y = 0; y < region.h(); y++) {
      System.arraycopy(src, y * region.w(), this.data[palette], (region.y() + y) * this.width, region.w());
    }
  }

  public int getPaletteOffset(final int x, final int y) {
    for(final var region : this.paletteRegions) {
      if(region.rect.contains(x, y)) {
        return region.offset;
      }
    }

    return 0;
  }

  public void dumpToFile() {
    final BufferedImage image = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_RGB);

    for(int palette = 0; palette < this.data.length; palette++) {
      image.setRGB(0, 0, this.width, this.height, this.data[palette], 0, this.width);

      try {
        ImageIO.write(image, "png", new File("dump." + palette + ".png"));
      } catch(final IOException e) {
        throw new RuntimeException(e);
      }
    }
  }

  public record PaletteRegion(Rect4i rect, int offset) { }
}
