package legend.core.audio;

final class VoiceCounter {
  private int counter = 0x2000;

  int getCurrentSampleIndex() {
    return (this.counter >> 12) & 0x1f;
  }

  int getInterpolationIndex() {
    return this.counter >> 3 & 0x1ff;
  }

  /** Adds value to the counter, returns true if the end of block was reached */
  boolean add(final int value) {
    this.counter += value;

    final int sampleIndex = this.getCurrentSampleIndex();
    if(sampleIndex >= 28) {
      this.counter = ((sampleIndex - 28) << 12) + (this.counter & 0xFFF);
      return true;
    }

    return false;
  }

  void reset() {
    this.counter = 0x2000;
  }
}
