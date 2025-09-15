package legend.core.audio.sequencer;

import legend.core.audio.EffectsOverTimeGranularity;
import legend.core.audio.InterpolationPrecision;

import static legend.core.audio.Constants.BREATH_BIT_SHIFT;
import static legend.core.audio.Constants.BREATH_MAX_VALUE;
import static legend.core.audio.Constants.PITCH_BIT_SHIFT;

final class VoiceCounter {
  private static InterpolationPrecision interpolationPrecision;
  private static EffectsOverTimeGranularity effectsOverTimeGranularity;


  /* TODO verify this is actually correct for other values in case we want to change
  *   the window size. This should be a generic solution but it wasn't verified.
  */
  private final static int START_OFFSET = ((Voice.EMPTY.length) / 2 + 1) << PITCH_BIT_SHIFT;
  private final static int CLEAR_AND = (1 << PITCH_BIT_SHIFT) - 1;
  private int sampleCounter = START_OFFSET;
  private int breathCounter;

  int getCurrentSampleIndex() {
    return (this.sampleCounter >>> PITCH_BIT_SHIFT) & 0x1f;
  }

  int getSampleInterpolationIndex() {
    return (this.sampleCounter >>> interpolationPrecision.sampleShift) & interpolationPrecision.interpolationAnd;
  }

  /** Adds value to the counter, returns true if the end of block was reached */
  boolean add(final int value) {
    this.sampleCounter += value;

    final int sampleIndex = this.getCurrentSampleIndex();
    if(sampleIndex >= 28) {
      this.sampleCounter = ((sampleIndex - 28) << PITCH_BIT_SHIFT) + (this.sampleCounter & CLEAR_AND);
      return true;
    }

    return false;
  }

  int getCurrentBreathIndex() {
    return this.breathCounter >>> BREATH_BIT_SHIFT;
  }

  int getBreathInterpolationIndex() {
    return (this.breathCounter >>> interpolationPrecision.breathShift) & interpolationPrecision.interpolationAnd;
  }

  void addBreath(final int breath) {
    // We're losing some precision here, but the values are already so precise, that it should not matter.
    // At the x16 setting, we should still be only losing about 0.0000477% of precision.
    this.breathCounter += breath >>> effectsOverTimeGranularity.shift;

    if(this.breathCounter >= BREATH_MAX_VALUE) {
      this.breathCounter -= BREATH_MAX_VALUE;
    }
  }

  void reset() {
    this.sampleCounter = START_OFFSET;
    this.resetBreath();
  }

  void resetBreath() {
    this.breathCounter = 0;
  }

  static void changeInterpolationPrecision (final InterpolationPrecision precision) {
    interpolationPrecision = precision;
  }

  static void changeEffectsOverTimeGranularity(final EffectsOverTimeGranularity granularity) {
    effectsOverTimeGranularity = granularity;
  }
}
