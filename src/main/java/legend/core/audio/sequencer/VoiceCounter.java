package legend.core.audio.sequencer;

import static legend.core.audio.sequencer.LookupTables.VOICE_COUNTER_BIT_PRECISION;

final class VoiceCounter {
  //TODO verify this is actually correct for other values
  private final static int START_OFFSET = ((Voice.EMPTY.length) / 2 + 1) << VOICE_COUNTER_BIT_PRECISION;
  private final static int CLEAR_AND = (1 << VOICE_COUNTER_BIT_PRECISION) - 1;
  private int counter = START_OFFSET;

  private final int interpolationShift;
  private final int interpolationAnd;

  VoiceCounter(final int bitDepth) {
    this.interpolationShift = VOICE_COUNTER_BIT_PRECISION - bitDepth;
    this.interpolationAnd = (1 << bitDepth) - 1;
  }

  int getCurrentSampleIndex() {
    return (this.counter >> VOICE_COUNTER_BIT_PRECISION) & 0x1f;
  }

  int getInterpolationIndex() {
    return this.counter >> this.interpolationShift & this.interpolationAnd;
  }

  /** Adds value to the counter, returns true if the end of block was reached */
  boolean add(final int value) {
    this.counter += value;

    final int sampleIndex = this.getCurrentSampleIndex();
    if(sampleIndex >= 28) {
      this.counter = ((sampleIndex - 28) << VOICE_COUNTER_BIT_PRECISION) + (this.counter & CLEAR_AND);
      return true;
    }

    return false;
  }

  void reset() {
    this.counter = START_OFFSET;
  }
}
