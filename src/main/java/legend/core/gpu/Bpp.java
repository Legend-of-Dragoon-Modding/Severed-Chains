package legend.core.gpu;

public enum Bpp {
  BITS_4(4, 0x3, 2, 0xf),
  BITS_8(2, 0x1, 3, 0xff),
  BITS_15(1, 0, 0, 0),
  BITS_24(1, 0, 0, 0),
  ;

  Bpp(final int widthDivisor, final int widthMask, final int indexShift, final int indexMask) {
    this.widthDivisor = widthDivisor;
    this.widthMask = widthMask;
    this.indexShift = indexShift;
    this.indexMask = indexMask;
  }

  public static Bpp of(final int value) {
    return values()[value];
  }

  public final int widthDivisor;
  public final int widthMask;
  public final int indexShift;
  public final int indexMask;
}
