package legend.game.sound;

import legend.core.MathHelper;

public class Instrument10 implements Sshd.Subfile {
  public int minKeyRange_00;
  public int maxKeyRange_01;
  public int rootKey_02;
  /** signed */
  public int cents_03;
  public int soundOffset_04;
  public int adsrLo_06;
  public int adsrHi_08;
  public int _0a;
  public int volume_0b;
  public int pan_0c;
  public int pitchBendMultiplier_0d;
  public int _0e;
  /**
   * <ul>
   *   <li>0x2 - noise on</li>
   *   <li>0x20 - modulation on</li>
   *   <li>0x80 - reverb on</li>
   * </ul>
   */
  public int flags_0f;

  public Instrument10(final byte[] data, final int offset) {
    this.minKeyRange_00 = MathHelper.getUbyte(data, offset);
    this.maxKeyRange_01 = MathHelper.getUbyte(data, offset + 0x1);
    this.rootKey_02 = MathHelper.getUbyte(data, offset + 0x2);
    this.cents_03 = MathHelper.getByte(data, offset + 0x3);
    this.soundOffset_04 = MathHelper.getUshort(data, offset + 0x4);
    this.adsrLo_06 = MathHelper.getUshort(data, offset + 0x6);
    this.adsrHi_08 = MathHelper.getUshort(data, offset + 0x8);
    this._0a = MathHelper.getUbyte(data, offset + 0xa);
    this.volume_0b = MathHelper.getUbyte(data, offset + 0xb);
    this.pan_0c = MathHelper.getUbyte(data, offset + 0xc);
    this.pitchBendMultiplier_0d = MathHelper.getUbyte(data, offset + 0xd);
    this._0e = MathHelper.getUbyte(data, offset + 0xe);
    this.flags_0f = MathHelper.getUbyte(data, offset + 0xf);
  }
}
