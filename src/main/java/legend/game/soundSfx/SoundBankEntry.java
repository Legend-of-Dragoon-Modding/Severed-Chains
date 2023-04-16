package legend.game.soundSfx;

import legend.core.audio.MidiSoundFontEntry;

final class SoundBankEntry implements MidiSoundFontEntry {
  private final short[][] pcm;
  private final int[] flags;
  private int index;
  private int repeatIndex;


  SoundBankEntry(final short[][] pcm, final int[] flags) {
    this.pcm = pcm;
    this.flags = flags;
  }

  @Override
  public boolean get(final short[] samples) {
    boolean isEnd = false;
    switch(this.flags[this.index]) {
      case 0, 2 -> this.index++;
      case 1 -> {
        this.index = 0;
        isEnd = true;
      }
      case 3 -> this.index = this.repeatIndex;
      case 4, 6 -> {
        this.repeatIndex = this.index;
        this.index++;
      }
    }

    System.arraycopy(this.pcm[this.index], 0, samples, 3, 28);

    return isEnd;
  }
}
