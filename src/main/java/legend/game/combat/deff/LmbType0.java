package legend.game.combat.deff;

import legend.game.unpacker.FileData;

public class LmbType0 extends Lmb {
  public final PartInfo0c[] partAnimations_08;

  public LmbType0(final FileData data) {
    super(data);

    this.partAnimations_08 = new PartInfo0c[this.objectCount_04];

    for(int i = 0; i < this.objectCount_04; i++) {
      this.partAnimations_08[i] = new PartInfo0c(data.slice(0x8 + i * 0xc, 0xc), data);
    }
  }

  public static class PartInfo0c {
    public final short _00;

    public final short count_04;

    public final LmbTransforms14[] keyframes_08;

    public PartInfo0c(final FileData data, final FileData baseData) {
      this._00 = data.readShort(0x0);

      this.count_04 = data.readShort(0x4);

      final int transformsOffset = data.readInt(0x8);
      this.keyframes_08 = new LmbTransforms14[this.count_04];
      for(int i = 0; i < this.keyframes_08.length; i++) {
        this.keyframes_08[i] = new LmbTransforms14(baseData.slice(transformsOffset + i * 0x14, 0x14));
      }
    }
  }
}
