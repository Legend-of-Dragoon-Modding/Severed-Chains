package legend.core.audio.sequencer;

import legend.core.audio.EffectsOverTimeGranularity;
import legend.core.audio.InterpolationPrecision;

import static legend.core.audio.Constants.BREATH_BIT_SHIFT;
import static legend.core.audio.Constants.BREATH_COUNT;
import static legend.core.audio.Constants.BREATH_MAX_VALUE;
import static legend.core.audio.Constants.PITCH_BIT_SHIFT;
import static legend.core.audio.Constants.PITCH_MAX_VALUE;

final class VoiceCounter {
  private static InterpolationPrecision interpolationPrecision;
  private static EffectsOverTimeGranularity effectsOverTimeGranularity;


  /* TODO verify this is actually correct for other values in case we want to change
  *   the window size. This should be a generic solution but it wasn't verified.
  */
  private final static long START_OFFSET = ((Voice.EMPTY.length) / 2 + 1L) << PITCH_BIT_SHIFT;
  private long sampleCounter = START_OFFSET;
  private int breathCounter;

  int getCurrentSampleIndex() {
    return (int)((this.sampleCounter >>> PITCH_BIT_SHIFT) & 0x1f);
  }

  int getSampleInterpolationIndex() {
    return (int)((this.sampleCounter >>> interpolationPrecision.sampleShift) & interpolationPrecision.interpolationAnd);
  }

  /** Adds value to the counter, returns true if the end of block was reached */
  boolean add(final long value) {
    this.sampleCounter += value;

    if(this.sampleCounter >= PITCH_MAX_VALUE) {
      this.sampleCounter -= PITCH_MAX_VALUE;
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

    if(this.getCurrentBreathIndex() >= BREATH_COUNT) {
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
