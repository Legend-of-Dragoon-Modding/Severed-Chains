package legend.game.tmd;

import legend.core.MathHelper;
import legend.core.gte.TmdObjTable1c;

public class UvAdjustmentMetrics14 {
  public static final UvAdjustmentMetrics14 NONE = new UvAdjustmentMetrics14(0, 0x0, 0xffff_ffff, 0x0, 0xffff_ffff, 0x0) {
    @Override
    public void apply(final TmdObjTable1c.Primitive primitive) { }
  };

  public final int index;
  public final int clutMaskOr_00;
  public final int clutMaskAnd_04;
  public final int tpageMaskOr_08;
  public final int tpageMaskAnd_0c;
  public final int uvOffset_10;

  public UvAdjustmentMetrics14(final int index, final int clutMaskOr, final int clutMaskAnd, final int tpageMaskOr, final int tpageMaskAnd, final int uvOffset) {
    this.index = index;
    this.clutMaskOr_00 = clutMaskOr;
    this.clutMaskAnd_04 = clutMaskAnd;
    this.tpageMaskOr_08 = tpageMaskOr;
    this.tpageMaskAnd_0c = tpageMaskAnd;
    this.uvOffset_10 = uvOffset;
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
