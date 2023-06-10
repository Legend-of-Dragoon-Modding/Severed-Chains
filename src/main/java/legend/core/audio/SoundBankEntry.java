package legend.core.audio;

import it.unimi.dsi.fastutil.Pair;

final class SoundBankEntry {
  private Pair<short[][], int[]> pcm;
  private int index;
  private int repeatIndex;
  private boolean end;

  void load(final Pair<short[][], int[]> pcm) {
    this.pcm = pcm;
    this.index = 0;
    this.repeatIndex = 0;
    this.end = false;
  }

  void loadSamples(final short[] samples) {
    switch(this.pcm.right()[this.index]) {
      case 0, 2 -> this.index++;
      case 1 -> {
        this.index = 0;
        this.end = true;
      }
      case 3 -> this.index = this.repeatIndex;
      case 4, 6 -> {
        this.repeatIndex = this.index;
        this.index++;
      }
      default -> throw new RuntimeException("Unknown Sound Bank Flag!");
    }

    System.arraycopy(this.pcm.left()[this.index], 0, samples, 3, 28);
  }

  boolean isEnd() {
    return this.end;
  }
}
