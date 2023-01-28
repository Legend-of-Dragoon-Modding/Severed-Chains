package legend.game.sound;

import legend.core.MathHelper;

/** Note: values before the entry list can mean different things */
public class SubList {
  /**
   * <li>
   *   <ul>When this is a subfile of a {@link Sssqish, it's always 0xff}.</ul>
   *   <ul>If flag 0x80 is set on the count, then all instruments in the preset are played at once.</ul>
   * </li>
   */
  public final int count_00;
  /** If this value is 0xff, {@link Sequencer#instrumentCanPlayNote} always returns true */
  public final int patchVolume_01;
  public final int pan_02;

  public final int pitchBendMultiplier_04;
  public final int _05;
  /** The starting key from which each instruments' min and max key range is relative */
  public final int startingKeyPosition_06;

  public final SshdStruct10[] entry_08;

  public SubList(final byte[] data, final int offset, final int elementCount) {
    this.count_00 = MathHelper.getUbyte(data, offset);
    this.patchVolume_01 = MathHelper.getUbyte(data, offset + 0x1);
    this.pan_02 = MathHelper.getUbyte(data, offset + 0x2);

    this.pitchBendMultiplier_04 = MathHelper.getUbyte(data, offset + 0x4);
    this._05 = MathHelper.getUbyte(data, offset + 0x5);
    this.startingKeyPosition_06 = MathHelper.getUbyte(data, offset + 0x6);

    this.entry_08 = new SshdStruct10[elementCount];

    for(int i = 0; i < this.entry_08.length; i++) {
      this.entry_08[i] = new SshdStruct10(data, offset + 0x8 + i * 0x10);
    }
  }
}
