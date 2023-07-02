package legend.core.gpu;

public class TimHeader {
  /** 0x0 */
  public int flags;
  /** 0x4 */
  public final RECT imageRect = new RECT();
  /** 0xc */
  public long imageAddress;
  /** 0x10 */
  public final RECT clutRect = new RECT();
  /** 0x18 */
  public long clutAddress;

  public Bpp getBpp() {
    return switch((this.flags & 0b111)) {
      case 0 -> Bpp.BITS_4;
      case 1 -> Bpp.BITS_8;
      case 2 -> Bpp.BITS_15;
      case 3 -> Bpp.BITS_24;
      default -> throw new RuntimeException("Unsupported mixed bpp");
    };
  }

  public void setImage(final RECT rect, final long address) {
    this.imageRect.set(rect);
    this.imageAddress = address;
  }

  public void setClut(final RECT rect, final long address) {
    this.clutRect.set(rect);
    this.clutAddress = address;
  }

  public boolean hasClut() {
    return this.clutAddress != 0;
  }

  public RECT getImageRect() {
    return this.imageRect;
  }

  public long getImageAddress() {
    return this.imageAddress;
  }

  public RECT getClutRect() {
    return this.clutRect;
  }

  public long getClutAddress() {
    return this.clutAddress;
  }
}
