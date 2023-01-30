package legend.game.sound;

import legend.core.MathHelper;

public class InstrumentsSubfile implements Sshd.Subfile {
  public final int count_00;
  public final Instrument[] instruments_02;

  public InstrumentsSubfile(final byte[] data, final int offset) {
    this.count_00 = MathHelper.getUshort(data, offset);
    this.instruments_02 = new Instrument[this.count_00 + 1];

    for(int i = 0; i < this.instruments_02.length; i++) {
      final int entryOffset = MathHelper.getShort(data, offset + 2 + i * 2);

      if(entryOffset != -1) {
        this.instruments_02[i] = new Instrument(data, offset + entryOffset, (MathHelper.getUbyte(data, offset + entryOffset) & 0x7f) + 1);
      }
    }
  }
}
