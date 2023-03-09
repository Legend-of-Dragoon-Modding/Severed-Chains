package legend.core.gpu;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class VramTexture2 {
  public final int width;
  public final int height;
  private final int[] data;

  public VramTexture2(final int width, final int height, final int[] data) {
    this.width = width;
    this.height = height;
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
