package legend.core.spu;

import legend.core.audio.InterpolationPrecision;

import static legend.core.audio.Constants.PITCH_BIT_SHIFT;
import static legend.core.audio.Constants.PITCH_MAX_VALUE;

public class Counter {            //internal
  private static final InterpolationPrecision interpolationPrecision = InterpolationPrecision.Double;
  private static final long START_OFFSET = 2L << PITCH_BIT_SHIFT;

  private long sampleCounter = START_OFFSET;

  public int getCurrentSampleIndex() {
    return (int)((this.sampleCounter >>> PITCH_BIT_SHIFT) & 0x1f);
  }

  public int getSampleInterpolationIndex() {
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

  void reset() {
    this.sampleCounter = START_OFFSET;
  }
}
