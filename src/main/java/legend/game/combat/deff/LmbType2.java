package legend.game.combat.deff;

import legend.game.unpacker.FileData;

public class LmbType2 extends Lmb {
  public final short _08;
  public final short _0a;
  public final int[] _0c;
  public final LmbTransforms14[] _10;
  public final byte[] _14;

  public LmbType2(final FileData data) {
    super(data);

    this._08 = data.readShort(0x8);
    this._0a = data.readShort(0xa);

    final int offset0c = data.readInt(0x0c);
    final int offset10 = data.readInt(0x10);
    final int offset14 = data.readInt(0x14);

    this._0c = new int[this.count_04];
    this._10 = new LmbTransforms14[this.count_04];

    for(int i = 0; i < this.count_04; i++) {
      this._0c[i] = data.readInt(offset0c + i * 0x4);
      this._10[i] = new LmbTransforms14(data.slice(offset10 + i * 0x14, 0x14));
    }

    this._14 = new byte[this._08 * (this._0a - 1)];

    for(int i = 0; i < this._14.length; i++) {
      this._14[i] = data.readByte(offset14 + i);
    }
  }
}
