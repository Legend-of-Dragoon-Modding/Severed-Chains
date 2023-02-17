package legend.game.combat.deff;

import legend.game.unpacker.FileData;

public class LmbType0 extends Lmb {
  public final PartInfo0c[] _08;

  public LmbType0(final FileData data) {
    super(data);

    this._08 = new PartInfo0c[this.count_04];

    for(int i = 0; i < this.count_04; i++) {
      this._08[i] = new PartInfo0c(data.slice(0x8 + i * 0xc, 0xc), data);
    }
  }

  public static class PartInfo0c {
    public final short _00;

    public final short count_04;

    public final LmbTransforms14[] _08;

    public PartInfo0c(final FileData data, final FileData baseData) {
      this._00 = data.readShort(0x0);

      this.count_04 = data.readShort(0x4);

      final int transformsOffset = data.readInt(0x8);
      this._08 = new LmbTransforms14[this.count_04];
      for(int i = 0; i < this._08.length; i++) {
        this._08[i] = new LmbTransforms14(baseData.slice(transformsOffset + i * 0x14, 0x14));
      }
    }
  }
}
