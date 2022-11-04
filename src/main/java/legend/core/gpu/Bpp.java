package legend.core.gpu;

public enum Bpp {
  BITS_4,
  BITS_8,
  BITS_15,
  BITS_24,
  ;

  public static Bpp of(final int value) {
    return values()[value];
  }
}
