package legend.core.gpu;

public abstract class VramTexture {
  public final Bpp bpp;
  public final Rect4i rect;

  public VramTexture(final Bpp bpp, final Rect4i rect) {
    this.bpp = bpp;
    this.rect = rect;
  }

  public abstract int getTexel(final VramTexture palette, final int pageX, final int x, final int y);
  public abstract int getTexel(final int pageX, final int x, final int y);
  public abstract int getPixel(final int x, final int y);
  public abstract void copyRow(final int y, final int[] dest, final int destOffset);
  public abstract void getRegion(final Rect4i region, final int[] dest);
  public abstract void setRegion(final Rect4i region, final int[] src);
}
