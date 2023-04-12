package legend.game.soundFinal;

final class VoiceCounter {
  private int counter;

  int getCurrentSampleIndex() {
    return this.counter >> 12 & 0x1f;
  }

  void setCurrentSampleIndex(final int value) {
    this.counter = (short)(this.counter & 0xfff);
    this.counter |= value << 12;
  }

  int getInterpolationIndex() {
    return this.counter >> 3 & 0xff;
  }

  /**
   * Adds value to counter returns true if block is to be advanced
   */
  boolean add(final int value) {
    this.counter += value;

    if(this.getCurrentSampleIndex() >= 28) {
      this.setCurrentSampleIndex(this.getCurrentSampleIndex() - 28);
      return true;
    }

    return false;
  }
}
