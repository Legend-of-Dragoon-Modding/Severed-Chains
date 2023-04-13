package legend.game.soundFinal;

import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.longs.Long2ObjectArrayMap;
import legend.core.MathHelper;
import legend.game.unpacker.FileData;

final class SoundBank {
  private static final int[] POSITIVE_SPU_ADPCM_TABLE = {0, 60, 115, 98, 122};
  private static final int[] NEGATIVE_SPU_ADPCM_TABLE = {0, 0, -52, -55, -60};
  private final Long2ObjectArrayMap<Pair<short[][], int[]>> entries;


  SoundBank(final FileData soundBank) {
    this.entries = new Long2ObjectArrayMap<>();

    final byte[] data = soundBank.data();

    final IntArrayList offsets = findSounds(data);

    for(int i = 0; i < offsets.size() - 1; i++) {
      final int offset = offsets.getInt(i);
      final int size = offsets.getInt(i + 1) - offset;
      final byte[] entry = new byte[size];
      System.arraycopy(data, offset, entry, 0, size);
      this.entries.put(offset, generateEntry(entry));
    }

    final int offset = offsets.getInt(offsets.size() - 1);
    final int size = data.length - offset;
    final byte[] entry = new byte[size];
    System.arraycopy(data, offset, entry, 0, size);
    this.entries.put(offset, generateEntry(entry));
  }


  SoundBankEntry getEntry(final int offset) {
    final Pair<short[][], int[]> entry = this.entries.get(offset);
    return new SoundBankEntry(entry.left(), entry.right());
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

  private static Pair<short[][], int[]> generateEntry(final byte[] data) {
    final boolean loops = (data[0x11] & 2) == 2;

    assert !loops || (data[data.length - 0xf] & 0x3) == 3 : "SoundBank partial loop!";

    final int blockCount = data.length / 16;

    final short[][] pcm = new short[blockCount + (loops ? 0 : 1)][];
    for(int i = 0; i < pcm.length; i++) {
      pcm[i] = new short[28];
    }

    final int[] next = new int[blockCount + (loops ? 0 : 1)];
    for(int i = 0; i < next.length - 1; i++) {
      next[i] = i + 1;
    }
    next[next.length - 1] = loops ? 1 : (next.length - 1);

    int old = 0;
    int older = 0;

    for(int block = 1; block < blockCount; block++) {
      final int shift = 12 - (data[block * 16] & 0xf);
      int filter = (data[block * 16] & 0x70) >> 4; //filter on SPU adpcm is 0-4 vs XA which is 0-3
      if(filter > 4) {
        filter = 4; //Crash Bandicoot sets this to 7 at the end of the first level and overflows the filter
      }

      final int f0 = POSITIVE_SPU_ADPCM_TABLE[filter];
      final int f1 = NEGATIVE_SPU_ADPCM_TABLE[filter];

      //Actual ADPCM decoding is the same as on XA but the layout here is sequential by nibble where on XA in grouped by nibble line
      int position = 2; //skip shift and flags
      int nibble = 1;
      for(int i = 0; i < 28; i++) {
        nibble = nibble + 1 & 0x1;

        final int t = signed4bit((byte)(data[block * 16 + position] >> nibble * 4 & 0x0f));
        final int s = (t << shift) + (old * f0 + older * f1 + 32) / 64;
        final short sample = (short)MathHelper.clamp(s, -0x8000, 0x7fff);

        pcm[block][i] = sample;

        older = old;
        old = sample;

        position += nibble;
      }
    }

    return new Pair<>() {
      @Override
      public short[][] left() {
        return pcm;
      }

      @Override
      public int[] right() {
        return next;
      }
    };
  }

  private static int signed4bit(final byte value) {
    return value << 28 >> 28;
  }
}
