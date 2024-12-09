package legend.game.combat.deff;

import legend.game.unpacker.FileData;

public class LmbType2 extends Lmb {
  public final short transformDataPairCount_08;
  public final short keyframeCount_0a;
  /**
   * Bits:
   * <ul>
   *   <li>0x80 - disable z rotation step</li>
   *   <li>0x100 - disable y rotation step</li>
   *   <li>0x200 - disable x rotation step</li>
   *   <li>0x400 - disable z translation step</li>
   *   <li>0x800 - disable y translation step</li>
   *   <li>0x1000 - disable x translation step</li>
   *   <li>0x2000 - disable z scale step</li>
   *   <li>0x4000 - disable y scale step</li>
   *   <li>0x8000 - disable x scale step</li>
   *   <li>0x100_0000 - index into LmbAnimationEffect 5c deffFlags_14</li>
   * </ul>
   */
  public final int[] transformFlags_0c;
  public final LmbTransforms14[] initialTransforms_10;
  public final byte[] packedTransformData_14;

  public LmbType2(final FileData data) {
    super(data);

    this.transformDataPairCount_08 = data.readShort(0x8);
    this.keyframeCount_0a = data.readShort(0xa);

    final int offset0c = data.readInt(0x0c);
    final int offset10 = data.readInt(0x10);
    final int offset14 = data.readInt(0x14);

    this.transformFlags_0c = new int[this.objectCount_04];
    this.initialTransforms_10 = new LmbTransforms14[this.objectCount_04];

    for(int i = 0; i < this.objectCount_04; i++) {
      this.transformFlags_0c[i] = data.readInt(offset0c + i * 0x4);
      this.initialTransforms_10[i] = new LmbTransforms14(data.slice(offset10 + i * 0x14, 0x14));
    }

    this.packedTransformData_14 = new byte[this.transformDataPairCount_08 * (this.keyframeCount_0a - 1)];

    for(int i = 0; i < this.packedTransformData_14.length; i++) {
      this.packedTransformData_14[i] = data.readByte(offset14 + i);
    }
  }
}
