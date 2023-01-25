package legend.game.sound;

import legend.core.MathHelper;

public class Subfile0 implements Sshd.Subfile {
  public final int count_00;
  public final SubList[] entries_02;

  public Subfile0(final byte[] data, final int offset) {
    this.count_00 = MathHelper.getUshort(data, offset);
    this.entries_02 = new SubList[this.count_00 + 1];

    for(int i = 0; i < this.entries_02.length; i++) {
      final int entryOffset = MathHelper.getShort(data, offset + 2 + i * 2);

      if(entryOffset != -1) {
        this.entries_02[i] = new SubList(data, offset + entryOffset, (MathHelper.getUbyte(data, offset + entryOffset) & 0x7f) + 1);
      }
    }
  }
}
