package legend.core.audio.sequencer;

import legend.core.MathHelper;

public final class SoundBankEntry {
  private static final int[] POSITIVE_SPU_ADPCM_TABLE = {0, 60, 115, 98, 122};
  private static final int[] NEGATIVE_SPU_ADPCM_TABLE = {0, 0, -52, -55, -60};

  private byte[] data;
  private int index;
  private int repeatIndex;
  private boolean end;

  private int old;
  private int older;

  public void load(final byte[] data) {
    this.data = data;
    this.index = 0;
    this.repeatIndex = 0;
    this.end = false;
    this.old = 0;
    this.older = 0;
  }
  public void loadSamples(final short[] samples) {
    switch(this.data[this.index + 1]) {
      case 0, 2 -> this.index += 16;
      case 1 -> {
        this.index = 0;
        this.end = true;
      }
      case 3 -> this.index = this.repeatIndex;
      case 4, 6 -> {
        this.repeatIndex = this.index;
        this.index += 16;
      }
      case 7 -> {
      }
      default -> throw new RuntimeException("Unknown Sound Bank Flag " + this.data[this.index + 1] + " !");
    }

    System.arraycopy(samples, 28, samples, 0, Voice.EMPTY.length);

    final int shift = 12 - (this.data[this.index] & 0x0f);
    final int filter = Math.min((this.data[this.index] & 0x70) >> 4, 4); //Crash Bandicoot sets this to 7 at the end of the first level and overflows the filter

    int position = 2; //Skip shift and flags
    int nibble = 1;
    for(int i = 0; i < 28; i++) {
      nibble = nibble + 1 & 0x1;

      final int t = signed4bit((byte)(this.data[this.index + position] >> nibble * 4 & 0x0f));
      final int s = (t << shift) + (this.old * POSITIVE_SPU_ADPCM_TABLE[filter] + this.older * NEGATIVE_SPU_ADPCM_TABLE[filter] + 32) / 64;

      final short sample = (short)MathHelper.clamp(s, -0x8000, 0x7fff);

      samples[Voice.EMPTY.length + i] = sample;

      this.older = this.old;
      this.old = sample;

      position += nibble;
    }
  }

  private static int signed4bit(final byte value) {
    return value << 28 >> 28;
  }

  public boolean isEnd() {
    return this.end;
  }
}
