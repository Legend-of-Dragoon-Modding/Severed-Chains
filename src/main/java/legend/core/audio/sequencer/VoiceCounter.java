package legend.core.audio.sequencer;

import legend.core.audio.InterpolationPrecision;

import static legend.core.audio.sequencer.LookupTables.BREATH_BASE_SHIFT;
import static legend.core.audio.sequencer.LookupTables.BREATH_BASE_VALUE;
import static legend.core.audio.sequencer.LookupTables.VOICE_COUNTER_BIT_PRECISION;

final class VoiceCounter {
  //TODO verify this is actually correct for other values in case we want to change
  //     the window size. This should be a generic solution but it wasn't verified.
  private final static int START_OFFSET = ((Voice.EMPTY.length) / 2 + 1) << VOICE_COUNTER_BIT_PRECISION;
  private final static int CLEAR_AND = (1 << VOICE_COUNTER_BIT_PRECISION) - 1;
  private int sampleCounter = START_OFFSET;
  private int breathCounter = 0;

  private int sampleInterpolationShift;
  private int breathInterpolationShift;
  private int interpolationAnd;

  VoiceCounter(final InterpolationPrecision bitDepth) {
    this.sampleInterpolationShift = VOICE_COUNTER_BIT_PRECISION - bitDepth.value;
    this.breathInterpolationShift = BREATH_BASE_SHIFT - bitDepth.value - 2;
    this.interpolationAnd = (1 << bitDepth.value) - 1;
  }

  int getCurrentSampleIndex() {
    return (this.sampleCounter >>> VOICE_COUNTER_BIT_PRECISION) & 0x1f;
  }

  int getSampleInterpolationIndex() {
    return this.sampleCounter >>> this.sampleInterpolationShift & this.interpolationAnd;
  }

  /** Adds value to the counter, returns true if the end of block was reached */
  boolean add(final int value) {
    this.sampleCounter += value;

    final int sampleIndex = this.getCurrentSampleIndex();
    if(sampleIndex >= 28) {
      this.sampleCounter = ((sampleIndex - 28) << VOICE_COUNTER_BIT_PRECISION) + (this.sampleCounter & CLEAR_AND);
      return true;
    }

    return false;
  }

  int getCurrentBreathIndex() {
    return this.breathCounter >>> (BREATH_BASE_SHIFT - 2);
  }

  int getBreathInterpolationIndex() {
    return (this.breathCounter >>> this.breathInterpolationShift) & this.interpolationAnd;
  }

  void addBreath(final int breath) {
    this.breathCounter += breath;

    if(this.breathCounter >= BREATH_BASE_VALUE) {
      this.breathCounter -= BREATH_BASE_VALUE;
    }
  }

  void reset() {
    this.sampleCounter = START_OFFSET;
    this.breathCounter = 0;
  }

  void changeInterpolationBitDepth(final InterpolationPrecision bitDepth) {
    this.sampleInterpolationShift = VOICE_COUNTER_BIT_PRECISION - bitDepth.value;
    this.breathInterpolationShift = BREATH_BASE_SHIFT - bitDepth.value - 2;
    this.interpolationAnd = (1 << bitDepth.value) - 1;
  }
}
