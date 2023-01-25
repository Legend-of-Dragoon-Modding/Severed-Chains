package legend.game.sound;

import legend.core.MathHelper;

import java.util.Arrays;

/** Files embedded in SSHDs that start like SSSQs but have 24 entries instead of 16 and some other data instead of sequence data */
public class Sssqish implements Sshd.Subfile {
  public int volume_00;

  public final Sssq.Entry[] entries_10;
  public final Entry2List entries2_190;

  public Sssqish(final byte[] data, final int offset, final int size) {
    this.volume_00 = MathHelper.getUbyte(data, offset);

    this.entries_10 = new Sssq.Entry[24];
    Arrays.setAll(this.entries_10, i -> new Sssq.Entry(data, offset + 0x10 + i * 0x10));

    this.entries2_190 = new Entry2List(data, offset + 0x190, size);
  }

  public static class Entry2List {
    public final int count_00;
    public final SubList[] entries_02;

    public Entry2List(final byte[] data, final int offset, final int size) {
      this.count_00 = MathHelper.getUshort(data, offset);
      this.entries_02 = new SubList[this.count_00 + 1];

      final int[] entryOffsets = new int[this.count_00 + 2];
      for(int i = 0; i < entryOffsets.length - 1; i++) {
        entryOffsets[i] = MathHelper.getShort(data, offset + 2 + i * 2);
      }

      entryOffsets[entryOffsets.length - 1] = size - 0x190;
      Arrays.sort(entryOffsets);

      for(int i = 0; i < this.entries_02.length; i++) {
        final int entryOffset = MathHelper.getShort(data, offset + 2 + i * 2);

        if(entryOffset != -1) {
          int entrySize = 0;
          for(int n = 0; n < entryOffsets.length - 1; n++) {
            if(entryOffsets[n] == entryOffset) {
              entrySize = entryOffsets[n + 1] - entryOffset;
              break;
            }
          }

          this.entries_02[i] = new SubList(data, offset + entryOffset, (entrySize - 0x8) / 0x10);
        }
      }
    }
  }
}
