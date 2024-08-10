package legend.game.tmd;

import legend.core.MathHelper;
import legend.core.gte.TmdObjTable1c;

public class UvAdjustmentMetrics14 {
  public static final UvAdjustmentMetrics14 NONE = new UvAdjustmentMetrics14(0, 0, 0, 0, 0, false) {
    @Override
    public void apply(final TmdObjTable1c.Primitive primitive) { }
  };

  public static final UvAdjustmentMetrics14 PNG = new UvAdjustmentMetrics14(0, 0, 0, 0, 0, false) {
    @Override
    public void apply(final TmdObjTable1c.Primitive primitive) {
      for(final byte[] data : primitive.data()) {
        MathHelper.set(data, 0x4, 4, MathHelper.get(data, 0x4, 4) & 0x60_ffff | 0x180_0000); // 24bpp
      }
    }
  };

  public final int index;

  public final int clutX;
  public final int clutY;
  public final int tpageX;
  public final int tpageY;

  public final int clutMaskOr_00;
  public final int clutMaskAnd_04;
  public final int tpageMaskOr_08;
  public final int tpageMaskAnd_0c;
  public final int uvOffset_10;

  public UvAdjustmentMetrics14(final int index, final int x, final int y, final boolean ignoreClutX) {
    this(index, x, y + 112, x, y, ignoreClutX);
  }

  public UvAdjustmentMetrics14(final int index, final int clutX, final int clutY, final int tpageX, final int tpageY, final boolean ignoreClutX) {
    final int u = tpageX % 64 * 4;
    final int v = tpageY % 256;
    final int clut = clutX / 16 | clutY << 6;
    final int tpage = tpageX / 64 | tpageY / 256 << 4;
    final int uv = u | v << 8;

    this.clutX = clutX;
    this.clutY = clutY;
    this.tpageX = tpageX;
    this.tpageY = tpageY;

    this.index = index;
    this.clutMaskOr_00 = clut << 16;
    this.clutMaskAnd_04 = ignoreClutX ? 0x3c0ffff : 0x3c3ffff;
    this.tpageMaskOr_08 = tpage << 16;
    this.tpageMaskAnd_0c = 0xffe0ffff;
    this.uvOffset_10 = uv;
  }

  public void apply(final TmdObjTable1c.Primitive primitive) {
    for(final byte[] data : primitive.data()) {
      MathHelper.set(data, 0x0, 4, (MathHelper.get(data, 0x0, 4) & this.clutMaskAnd_04 | this.clutMaskOr_00) + this.uvOffset_10);
      MathHelper.set(data, 0x4, 4, (MathHelper.get(data, 0x4, 4) & this.tpageMaskAnd_0c | this.tpageMaskOr_08) + this.uvOffset_10);
      MathHelper.set(data, 0x8, 4, MathHelper.get(data, 0x8, 4) + this.uvOffset_10);

      if((primitive.header() & 0x800_0000) != 0) { // Quad
        MathHelper.set(data, 0xc, 4, MathHelper.get(data, 0xc, 4) + this.uvOffset_10);
      }
    }
  }
}
