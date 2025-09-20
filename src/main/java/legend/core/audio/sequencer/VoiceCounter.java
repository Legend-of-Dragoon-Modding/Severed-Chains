package legend.core.audio.sequencer;

import legend.core.audio.EffectsOverTimeGranularity;
import legend.core.audio.Interpolation;
import legend.core.audio.InterpolationPrecision;

import static legend.core.audio.Constants.BREATH_BIT_SHIFT;
import static legend.core.audio.Constants.BREATH_COUNT;
import static legend.core.audio.Constants.BREATH_MAX_VALUE;
import static legend.core.audio.Constants.PITCH_BIT_SHIFT;
import static legend.core.audio.Constants.PITCH_MAX_VALUE;

final class VoiceCounter {
  private static InterpolationPrecision interpolationPrecision;
  private static EffectsOverTimeGranularity effectsOverTimeGranularity;

  private static long START_OFFSET;
  private long sampleCounter;
  private long breathCounter;

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
    return (int)(this.breathCounter >>> BREATH_BIT_SHIFT);
  }

  int getBreathInterpolationIndex() {
    return (int)((this.breathCounter >>> interpolationPrecision.breathShift) & interpolationPrecision.interpolationAnd);
  }

  void addBreath(final long breath) {
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

  static void changeInterpolation(final Interpolation interpolation) {
    START_OFFSET = ((long)interpolation.taps / 2) << PITCH_BIT_SHIFT;
  }

  static void changeInterpolationPrecision (final InterpolationPrecision precision) {
    interpolationPrecision = precision;
  }

  static void changeEffectsOverTimeGranularity(final EffectsOverTimeGranularity granularity) {
    effectsOverTimeGranularity = granularity;
  }
}
