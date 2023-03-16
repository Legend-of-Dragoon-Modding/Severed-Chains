package legend.core.gpu;

public enum Bpp {
  BITS_4(4),
  BITS_8(2),
  BITS_15(1),
  BITS_24(1),
  ;

  Bpp(final int widthScale) {
    this.widthScale = widthScale;
  }

  public static Bpp of(final int value) {
    return values()[value];
  }

  public final int widthScale;
}
