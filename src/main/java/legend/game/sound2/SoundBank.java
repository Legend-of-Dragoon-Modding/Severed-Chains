package legend.game.sound2;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.longs.Long2ObjectArrayMap;
import legend.game.unpacker.FileData;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

final class SoundBank {
  private final Long2ObjectArrayMap<SoundBankEntry> entries;

  SoundBank (final FileData soundBank) {
    this.entries = new Long2ObjectArrayMap<>();

    final byte[] data = soundBank.data();
    final ByteBuffer dataBuff = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN);

    final IntArrayList offsets = findSounds(data);

    for(int i = 0; i < offsets.size() - 1; i++) {
      final int offset = offsets.getInt(i);
      final int size = offsets.getInt(i + 1) - offset;
      this.entries.put(offset, new SoundBankEntry(dataBuff.slice(offset, size)));
    }

    final int offset = offsets.getInt(offsets.size() - 1);
    final int size = data.length - offset;
    this.entries.put(offset, new SoundBankEntry(dataBuff.slice(offset, size)));
  }

  public SoundBankBuffer getSoundBankBuffer(final int offset) {
    return this.entries.get(offset).getBuffer();
  }

  private static IntArrayList findSounds(final byte[] haystack) {
    final IntArrayList offsets = new IntArrayList();

    int index = 0;
    while(index <= haystack.length - 16) {
      int matchIndex = 15;

      while(true) {
        if(haystack[index + matchIndex] != 0) {
          index += 16;
          break;
        }

        if (matchIndex <= 0) {
          offsets.add(index);
          index += 16;
          break;
        }

        matchIndex--;
      }
    }
    return offsets;
  }
}
