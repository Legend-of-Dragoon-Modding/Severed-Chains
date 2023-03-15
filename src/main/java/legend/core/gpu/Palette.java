package legend.core.gpu;

public class Palette {
  private final int[] palette;

  public Palette(final int[] palette) {
    this.palette = palette;
  }

  public int getColour(final int index) {
    return this.palette[index];
  }
}
