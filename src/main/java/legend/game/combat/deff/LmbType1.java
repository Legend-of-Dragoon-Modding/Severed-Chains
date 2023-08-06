package legend.game.combat.deff;

import legend.game.unpacker.FileData;

public class LmbType1 extends Lmb {
  public final short _08;
  public final short _0a;

  public final Sub04[] _0c;
  public final LmbTransforms14[] _10;
  public final short[] _14;

  public LmbType1(final FileData data) {
    super(data);

    this._08 = data.readShort(0x8);
    this._0a = data.readShort(0xa);

    this._0c = new Sub04[this.objectCount_04];
    this._10 = new LmbTransforms14[this.objectCount_04];

    final int sub04Offset = data.readInt(0x0c);
    for(int i = 0; i < this.objectCount_04; i++) {
      this._0c[i] = new Sub04(data.slice(sub04Offset + i * 0x4, 0x4));
    }

    final int transformsOffset = data.readInt(0x10);
    for(int i = 0; i < this.objectCount_04; i++) {
      this._10[i] = new LmbTransforms14(data.slice(transformsOffset + i * 0x14, 0x14));
    }

    final int offset14 = data.readInt(0x14);
    this._14 = new short[this._08 * (this._0a - 1) / 2]; // Number of bytes, /2 to get array length
    for(int i = 0; i < this._14.length; i++) {
      this._14[i] = data.readShort(offset14 + i * 0x2);
    }
  }

  public static class Sub04 {
    public final int _00;
    public final int _03;

    public Sub04(final FileData data) {
      this._00 = data.readUShort(0x0);
      this._03 = data.readUByte(0x3);
    }
  }
}
