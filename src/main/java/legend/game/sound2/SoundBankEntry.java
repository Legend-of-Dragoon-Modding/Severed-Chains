package legend.game.sound2;

import legend.core.MathHelper;

import java.nio.ByteBuffer;
import java.nio.ShortBuffer;

final class SoundBankEntry {
  private static final int[] POSITIVE_SPU_ADPCM_TABLE = {0, 60, 115, 98, 122};
  private static final int[] NEGATIVE_SPU_ADPCM_TABLE = {0, 0, -52, -55, -60};

  private final boolean loops;
  private final short[] pcm;

  SoundBankEntry(final ByteBuffer data) {
    this.loops = (data.get(0x11) & 0x02) == 2;

    assert !this.loops || ((data.get(data.limit() - 0x0f) & 0x03) == 3) : "SoundBank partial loop!";

    final int blockCount = data.limit() / 16;
    this.pcm = new short[blockCount * 24];

    int old = 0;
    int older = 0;
    for(int block = 1; block < blockCount; block++) {
      final int shift = 12 - (data.get(block * 16) & 0x0f);
      int filter = (data.get(block * 16) & 0x70) >> 4; //filter on SPU adpcm is 0-4 vs XA which is 0-3
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

        final int t = signed4bit((byte)(data.get(block * 16 + position) >> nibble * 4 & 0x0f));
        final int s = (t << shift) + (old * f0 + older * f1 + 32) / 64;
        final short sample = (short)MathHelper.clamp(s, -0x8000, 0x7fff);

        this.pcm[(block - 1) * 24 + i] = sample;

        older = old;
        old = sample;

        position += nibble;
      }
    }
  }

  SoundBankBuffer getBuffer() {
    if(this.loops) {
      return new LoopingSoundBankBuffer(ShortBuffer.wrap(this.pcm));
    }

    return new NonLoopingSoundBankBuffer(ShortBuffer.wrap(this.pcm));
  }

  private static int signed4bit(final byte value) {
    return value << 28 >> 28;
  }
}
