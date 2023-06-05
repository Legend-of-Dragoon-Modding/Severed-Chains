package legend.game.sound;

import legend.game.unpacker.FileData;

public class InstrumentsSubfile implements Sshd.Subfile {
  public final int count_00;
  public final Instrument[] instruments_02;

  public InstrumentsSubfile(final FileData data, final int offset) {
    this.count_00 = data.readUShort(offset);
    this.instruments_02 = new Instrument[this.count_00 + 1];

    for(int i = 0; i < this.instruments_02.length; i++) {
      final int entryOffset = data.readShort(offset + 2 + i * 2);

      if(entryOffset != -1) {
        this.instruments_02[i] = new Instrument(data, offset + entryOffset, (data.readUByte(offset + entryOffset) & 0x7f) + 1);
      }
    }
  }
}
