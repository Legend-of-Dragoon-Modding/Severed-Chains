package legend.game.combat.deff;

import legend.game.unpacker.FileData;

public class LmbType1 extends Lmb {
  public final short transformDataCount_08;
  public final short keyframeCount_0a;

  public final PartInfo04[] partAnimations_0c;
  public final LmbTransforms14[] transforms_10;
  public final short[] transformData_14;

  public LmbType1(final FileData data) {
    super(data);

    this.transformDataCount_08 = data.readShort(0x8);
    this.keyframeCount_0a = data.readShort(0xa);

    this.partAnimations_0c = new PartInfo04[this.objectCount_04];
    this.transforms_10 = new LmbTransforms14[this.objectCount_04];

    final int sub04Offset = data.readInt(0x0c);
    for(int i = 0; i < this.objectCount_04; i++) {
      this.partAnimations_0c[i] = new PartInfo04(data.slice(sub04Offset + i * 0x4, 0x4));
    }

    final int transformsOffset = data.readInt(0x10);
    for(int i = 0; i < this.objectCount_04; i++) {
      this.transforms_10[i] = new LmbTransforms14(data.slice(transformsOffset + i * 0x14, 0x14));
    }

    final int offset14 = data.readInt(0x14);
    this.transformData_14 = new short[this.transformDataCount_08 * (this.keyframeCount_0a - 1) / 2]; // Number of bytes, /2 to get array length
    for(int i = 0; i < this.transformData_14.length; i++) {
      this.transformData_14[i] = data.readShort(offset14 + i * 0x2);
    }
  }

  public static class PartInfo04 {
    public final int transformFlags_00;
    public final int partFlagIndex_03;

    public PartInfo04(final FileData data) {
      this.transformFlags_00 = data.readUShort(0x0);
      this.partFlagIndex_03 = data.readUByte(0x3);
    }
  }
}
