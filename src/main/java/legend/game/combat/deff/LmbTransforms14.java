package legend.game.combat.deff;

import legend.core.IoHelper;
import legend.core.gte.SVECTOR;

public class LmbTransforms14 {
  public final SVECTOR scale_00 = new SVECTOR();
  public final SVECTOR trans_06 = new SVECTOR();
  public final SVECTOR rot_0c = new SVECTOR();

  public LmbTransforms14() { }

  public LmbTransforms14(final byte[] data, final int offset) {
    IoHelper.readSvec3(data, offset, this.scale_00);
    IoHelper.readSvec3(data, offset + 0x6, this.trans_06);
    IoHelper.readSvec3(data, offset + 0xc, this.rot_0c);
  }

  public LmbTransforms14 set(final LmbTransforms14 other) {
    this.scale_00.set(other.scale_00);
    this.trans_06.set(other.trans_06);
    this.rot_0c.set(other.rot_0c);
    return this;
  }
}
