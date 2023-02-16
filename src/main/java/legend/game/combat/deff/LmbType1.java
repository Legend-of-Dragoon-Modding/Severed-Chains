package legend.game.combat.deff;

import legend.core.IoHelper;

public class LmbType1 extends Lmb {
  public final short _08;
  public final short _0a;

  public final Sub04[] _0c;
  public final LmbTransforms14[] _10;
  public final short[] _14;

  public LmbType1(final byte[] data, final int offset) {
    super(data, offset);

    this._08 = IoHelper.readShort(data, offset + 0x8);
    this._0a = IoHelper.readShort(data, offset + 0xa);

    this._0c = new Sub04[this.count_04];
    this._10 = new LmbTransforms14[this.count_04];

    final int sub04Offset = IoHelper.readInt(data, offset + 0x0c);
    for(int i = 0; i < this.count_04; i++) {
      this._0c[i] = new Sub04(data, offset + sub04Offset + i * 0x4);
    }

    final int transformsOffset = IoHelper.readInt(data, offset + 0x10);
    for(int i = 0; i < this.count_04; i++) {
      this._10[i] = new LmbTransforms14(data, offset + transformsOffset + i * 0x14);
    }

    final int offset14 = IoHelper.readInt(data, offset + 0x14);
    this._14 = new short[this._08 * (this._0a - 1) / 2]; // Number of bytes, /2 to get array length
    for(int i = 0; i < this._14.length; i++) {
      this._14[i] = IoHelper.readShort(data, offset + offset14 + i * 0x2);
    }
  }

  public static class Sub04 {
    public final int _00;
    public final int _03;

    public Sub04(final byte[] data, final int offset) {
      this._00 = IoHelper.readUShort(data, offset);
      this._03 = IoHelper.readUByte(data, offset + 0x3);
    }
  }
}
