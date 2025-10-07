package legend.core.font;

public class Glyph {
  public final char chr;
  public final int texU;
  public final int texV;
  public final int texW;
  public final int texH;
  public final int x;
  public final int y;
  public final int w;
  public final int h;
  public final boolean colour;

  int index;

  public Glyph(final char chr, final int texU, final int texV, final int texW, final int texH, final int x, final int y, final int w, final int h, final boolean colour) {
    this.chr = chr;
    this.texU = texU;
    this.texV = texV;
    this.texW = texW;
    this.texH = texH;
    this.x = x;
    this.y = y;
    this.w = w;
    this.h = h;
    this.colour = colour;
  }
}
