package legend.game.soundFinal;

final class SoundBankEntry {
  private final short[][] pcm;
  private final int[] next;
  private int index;


  SoundBankEntry(final short[][] pcm, final int[] next) {
    this.pcm = pcm;
    this.next = next;
  }

  short[] get() {
    this.index = this.next[this.index];
    return this.pcm[this.index];
  }

  void reset() {
    this.index = 0;
  }
}