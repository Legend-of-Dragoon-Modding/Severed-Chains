package legend.game.combat.deff;

import legend.core.IoHelper;

public class LmbType1 extends Lmb {
  public final short _08;
  public final short _0a;

  public final LmbTransforms14[] _10;

  public LmbType1(final byte[] data, final int offset) {
    super(data, offset);

    this._08 = IoHelper.readShort(data, offset + 0x8);
    this._0a = IoHelper.readShort(data, offset + 0xa);

    this._10 = new LmbTransforms14[this.count_04];

    final int transformsOffset = IoHelper.readInt(data, offset + 0x10);
    for(int i = 0; i < this.count_04; i++) {
      this._10[i] = new LmbTransforms14(data, offset + transformsOffset + i * 0x14);
    }
  }
}
