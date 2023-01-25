package legend.game.sound;

import legend.core.MathHelper;

public class SubList {
  public final int count_00;
  public final int _01;
  public final int _02;
  public final int _04;
  public final int _05;
  public final int _06;
  public final SshdStruct10[] entry_08;

  public SubList(final byte[] data, final int offset, final int elementCount) {
    this.count_00 = MathHelper.getUbyte(data, offset);
    this._01 = MathHelper.getUbyte(data, offset + 0x1);
    this._02 = MathHelper.getUbyte(data, offset + 0x2);
    this._04 = MathHelper.getUbyte(data, offset + 0x4);
    this._05 = MathHelper.getUbyte(data, offset + 0x5);
    this._06 = MathHelper.getUbyte(data, offset + 0x6);
    this.entry_08 = new SshdStruct10[elementCount];

    for(int i = 0; i < this.entry_08.length; i++) {
      this.entry_08[i] = new SshdStruct10(data, offset + 0x8 + i * 0x10);
    }
  }
}
