package legend.game.combat.deff;

import legend.core.IoHelper;

public class LmbType0 extends Lmb {
  public final PartInfo0c[] _08;

  public LmbType0(final byte[] data, final int offset) {
    super(data, offset);

    this._08 = new PartInfo0c[this.count_04];

    for(int i = 0; i < this.count_04; i++) {
      this._08[i] = new PartInfo0c(data, offset + 0x8 + i * 0xc, offset);
    }
  }

  public static class PartInfo0c {
    public final short _00;

    public final short count_04;

    public final LmbTransforms14[] _08;

    public PartInfo0c(final byte[] data, final int offset, final int baseOffset) {
      this._00 = IoHelper.readShort(data, offset);

      this.count_04 = IoHelper.readShort(data, offset + 0x4);

      final int transformsOffset = baseOffset + IoHelper.readInt(data, offset + 0x8);
      this._08 = new LmbTransforms14[this.count_04];
      for(int i = 0; i < this._08.length; i++) {
        this._08[i] = new LmbTransforms14(data, transformsOffset + i * 0x14);
      }
    }
  }
}
