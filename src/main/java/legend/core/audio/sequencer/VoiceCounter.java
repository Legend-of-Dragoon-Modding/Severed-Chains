package legend.core.audio.sequencer;

final class VoiceCounter {
  //TODO verify this is actually correct for other values
  private final static int START_OFFSET = ((Voice.EMPTY.length) / 2 + 1) << 12;
  private int counter = START_OFFSET;

  private final int interpolationShift;
  private final int interpolationAnd;

  VoiceCounter(final int bitDepth) {
    this.interpolationShift = 12 - bitDepth;
    this.interpolationAnd = (1 << bitDepth) - 1;
  }

  int getCurrentSampleIndex() {
    return (this.counter >> 12) & 0x1f;
  }

  int getInterpolationIndex() {
    return this.counter >> this.interpolationShift & this.interpolationAnd;
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
    this.counter = START_OFFSET;
  }
}
