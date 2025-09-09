package legend.game.sound;

import legend.game.unpacker.FileData;

import java.util.Arrays;

/** Files embedded in SSHDs that start like SSSQs but have 24 entries instead of 16 and some other data instead of sequence data */
public class Sssqish implements Sshd.Subfile {
  public final String name;

  public int volume_00;

  public final Sssq.ChannelInfo[] entries_10;
  public final Entry2List entries2_190;

  public Sssqish(final String name, final FileData data, final int offset, final int size) {
    this.name = name;
    this.volume_00 = data.readUByte(offset);

    this.entries_10 = new Sssq.ChannelInfo[24];
    Arrays.setAll(this.entries_10, i -> new Sssq.ChannelInfo(data, offset + 0x10 + i * 0x10));

    this.entries2_190 = new Entry2List(name, data, offset + 0x190, size);
  }

  public static class Entry2List {
    public final String name;
    public final int count_00;
    public final Instrument[] entries_02;

    public Entry2List(final String name, final FileData data, final int offset, final int size) {
      this.name = name;
      this.count_00 = data.readUShort(offset);
      this.entries_02 = new Instrument[this.count_00 + 1];

      final int[] entryOffsets = new int[this.count_00 + 2];
      for(int i = 0; i < entryOffsets.length - 1; i++) {
        entryOffsets[i] = data.readShort(offset + 2 + i * 2);
      }

      entryOffsets[entryOffsets.length - 1] = size - 0x190;
      Arrays.sort(entryOffsets);

      for(int i = 0; i < this.entries_02.length; i++) {
        final int entryOffset = data.readShort(offset + 2 + i * 2);

        if(entryOffset != -1) {
          int entrySize = 0;
          for(int n = 0; n < entryOffsets.length - 1; n++) {
            if(entryOffsets[n] == entryOffset) {
              entrySize = entryOffsets[n + 1] - entryOffset;
              break;
            }
          }

          this.entries_02[i] = new Instrument(name + " instrument " + i, data, offset + entryOffset, (entrySize - 0x8) / 0x10);
        }
      }
    }
  }
}
