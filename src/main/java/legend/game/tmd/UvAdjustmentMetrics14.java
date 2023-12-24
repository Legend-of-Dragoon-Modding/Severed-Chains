package legend.game.tmd;

import legend.core.MathHelper;
import legend.core.gte.TmdObjTable1c;

public class UvAdjustmentMetrics14 {
  public static final UvAdjustmentMetrics14 NONE = new UvAdjustmentMetrics14(0, 0x0, 0xffff_ffff, 0x0, 0xffff_ffff, 0x0) {
    @Override
    public void apply(final TmdObjTable1c.Primitive primitive) { }
  };

  public final int index;
  public final int clutMaskOff_00;
  public final int clutMaskOn_04;
  public final int tpageMaskOff_08;
  public final int tpageMaskOn_0c;
  public final int uvOffset_10;

  public UvAdjustmentMetrics14(final int index, final int clutMaskOff, final int clutMaskOn, final int tpageMaskOff, final int tpageMaskOn, final int uvOffset) {
    this.index = index;
    this.clutMaskOff_00 = clutMaskOff;
    this.clutMaskOn_04 = clutMaskOn;
    this.tpageMaskOff_08 = tpageMaskOff;
    this.tpageMaskOn_0c = tpageMaskOn;
    this.uvOffset_10 = uvOffset;
  }

  public void apply(final TmdObjTable1c.Primitive primitive) {
    for(final byte[] data : primitive.data()) {
      MathHelper.set(data, 0x0, 4, (MathHelper.get(data, 0x0, 4) & this.clutMaskOn_04 | this.clutMaskOff_00) + this.uvOffset_10);
      MathHelper.set(data, 0x4, 4, (MathHelper.get(data, 0x4, 4) & this.tpageMaskOn_0c | this.tpageMaskOff_08) + this.uvOffset_10);
      MathHelper.set(data, 0x8, 4, MathHelper.get(data, 0x8, 4) + this.uvOffset_10);

      if((primitive.header() & 0x800_0000) != 0) { // Quad
        MathHelper.set(data, 0xc, 4, MathHelper.get(data, 0xc, 4) + this.uvOffset_10);
      }
    }
  }
}
