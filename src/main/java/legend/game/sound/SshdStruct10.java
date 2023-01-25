package legend.game.sound;

import legend.core.MathHelper;

public class SshdStruct10 implements Sshd.Subfile {
  public int _00;
  public int _01;
  public int _02;
  /** signed */
  public int _03;
  public int soundOffset_04;
  public int adsrLo_06;
  public int adsrHi_08;
  public int _0a;
  public int _0b;
  public int _0c;
  public int _0d;
  public int _0e;
  /**
   * <ul>
   *   <li>0x2 - noise on</li>
   *   <li>0x80 - reverb on</li>
   * </ul>
   */
  public int flags_0f;

  public SshdStruct10(final byte[] data, final int offset) {
    this._00 = MathHelper.getUbyte(data, offset);
    this._01 = MathHelper.getUbyte(data, offset + 0x1);
    this._02 = MathHelper.getUbyte(data, offset + 0x2);
    this._03 = MathHelper.getByte(data, offset + 0x3);
    this.soundOffset_04 = MathHelper.getUshort(data, offset + 0x4);
    this.adsrLo_06 = MathHelper.getUshort(data, offset + 0x6);
    this.adsrHi_08 = MathHelper.getUshort(data, offset + 0x8);
    this._0a = MathHelper.getUbyte(data, offset + 0xa);
    this._0b = MathHelper.getUbyte(data, offset + 0xb);
    this._0c = MathHelper.getUbyte(data, offset + 0xc);
    this._0d = MathHelper.getUbyte(data, offset + 0xd);
    this._0e = MathHelper.getUbyte(data, offset + 0xe);
    this.flags_0f = MathHelper.getUbyte(data, offset + 0xf);
  }
}
