package legend.game.sound;

import legend.game.unpacker.FileData;

public class Instrument {
  public final String name;

  /**
   * <ul>
   *   <li>When this is a subfile of a {@link Sssqish, it's always 0xff}.</li>
   *   <li>If flag 0x80 is set on the count, then all instruments in the preset are played at once.</li>
   * </ul>
   */
  public final int count_00;
  /** If this value is 0xff, {@link Sequencer#instrumentCanPlayNote} always returns true */
  public final int patchVolume_01;
  public final int pan_02;

  public final int pitchBendMultiplier_04;
  public final int _05;
  /** The starting key from which each instruments' min and max key range is relative */
  public final int startingKeyPosition_06;

  /** One instrument can be made up from multiple samples with different configurations */
  public final InstrumentLayer10[] layers_08;

  public Instrument(final String name, final FileData data, final int offset, final int elementCount) {
    this.name = name;
    this.count_00 = data.readUByte(offset);
    this.patchVolume_01 = data.readUByte(offset + 0x1);
    this.pan_02 = data.readUByte(offset + 0x2);

    this.pitchBendMultiplier_04 = data.readUByte(offset + 0x4);
    this._05 = data.readUByte(offset + 0x5);
    this.startingKeyPosition_06 = data.readUByte(offset + 0x6);

    this.layers_08 = new InstrumentLayer10[elementCount];

    for(int i = 0; i < this.layers_08.length; i++) {
      this.layers_08[i] = new InstrumentLayer10(name + " layer " + i, data, offset + 0x8 + i * 0x10);
    }
  }
}
