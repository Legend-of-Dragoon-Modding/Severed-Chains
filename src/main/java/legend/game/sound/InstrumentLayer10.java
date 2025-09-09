package legend.game.sound;

import legend.game.unpacker.FileData;

public class InstrumentLayer10 implements Sshd.Subfile {
  public final String name;

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
   *   <li>0x10 - use instrument's pitchBendMultiplier instead of instrument layer's</li>
   *   <li>0x20 - modulation on</li>
   *   <li>0x80 - reverb on</li>
   * </ul>
   */
  public int flags_0f;

  public InstrumentLayer10(final String name, final FileData data, final int offset) {
    this.name = name;
    this.minKeyRange_00 = data.readUByte(offset);
    this.maxKeyRange_01 = data.readUByte(offset + 0x1);
    this.rootKey_02 = data.readUByte(offset + 0x2);
    this.cents_03 = data.readByte(offset + 0x3);
    this.soundOffset_04 = data.readUShort(offset + 0x4);
    this.adsrLo_06 = data.readUShort(offset + 0x6);
    this.adsrHi_08 = data.readUShort(offset + 0x8);
    this._0a = data.readUByte(offset + 0xa);
    this.volume_0b = data.readUByte(offset + 0xb);
    this.pan_0c = data.readUByte(offset + 0xc);
    this.pitchBendMultiplier_0d = data.readUByte(offset + 0xd);
    this._0e = data.readUByte(offset + 0xe);
    this.flags_0f = data.readUByte(offset + 0xf);
  }
}
