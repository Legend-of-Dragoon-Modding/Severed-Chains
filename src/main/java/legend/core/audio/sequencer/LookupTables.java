package legend.core.audio.sequencer;

import it.unimi.dsi.fastutil.floats.FloatFloatImmutablePair;
import legend.core.audio.Interpolation;
import legend.core.audio.InterpolationPrecision;
import legend.core.audio.PitchResolution;

import static legend.core.audio.Constants.PITCH_BIT_SHIFT;
import static legend.core.audio.Constants.SAMPLE_RATE_RATIO;

final public class LookupTables {
  private static final double BASE_SAMPLE_RATE_VALUE = (1L << PITCH_BIT_SHIFT);
  private PitchResolution pitchResolution;
  private Interpolation interpolation;
  private int[] sampleRates;
  private int interpolationStep;
  private float[][] interpolationWeights;
  private final float[] pan = new float[0x80];

  LookupTables(final InterpolationPrecision bitDepth, final PitchResolution pitchResolution, final Interpolation interpolation) {
    this.pitchResolution = pitchResolution;
    this.sampleRates = new int[12 * pitchResolution.value];

    for(int i = 0; i < this.sampleRates.length; i++) {
      this.sampleRates[i] = (int)Math.round(BASE_SAMPLE_RATE_VALUE * SAMPLE_RATE_RATIO * Math.pow(2, i / (double)this.sampleRates.length));
    }

    this.calculateInterpolationWeights(bitDepth, interpolation);

    // Lerped original pan table. Probably could be made into a function, but there's a weird plato with the 0x78, and I was lazy.
    final int[] panTable = {
      0x00, 0x02, 0x04, 0x06, 0x08, 0x0A, 0x0C, 0x0E,
      0x10, 0x12, 0x14, 0x16, 0x18, 0x1A, 0x1C, 0x1E,
      0x20, 0x22, 0x24, 0x26, 0x28, 0x2A, 0x2C, 0x2E,
      0x30, 0x32, 0x34, 0x36, 0x38, 0x3A, 0x3C, 0x3E,
      0x40, 0x42, 0x44, 0x46, 0x48, 0x4A, 0x4C, 0x4E,
      0x50, 0x52, 0x54, 0x56, 0x58, 0x5A, 0x5C, 0x5E,
      0x60, 0x62, 0x64, 0x66, 0x68, 0x6A, 0x6C, 0x6E,
      0x70, 0x72, 0x74, 0x76, 0x78, 0x78, 0x78, 0x78,
      0x78, 0x7A, 0x7C, 0x7E, 0x80, 0x80, 0x80, 0x80,
      0x80, 0x80, 0x80, 0x80, 0x80, 0x80, 0x80, 0x80,
      0x80, 0x80, 0x80, 0x80, 0x80, 0x80, 0x80, 0x80,
      0x80, 0x80, 0x80, 0x80, 0x80, 0x80, 0x80, 0x80,
      0x80, 0x80, 0x80, 0x80, 0x80, 0x80, 0x80, 0x80,
      0x80, 0x80, 0x80, 0x80, 0x80, 0x80, 0x80, 0x80,
      0x80, 0x80, 0x80, 0x80, 0x80, 0x80, 0x80, 0x80,
      0x80, 0x80, 0x80, 0x80, 0x80, 0x80, 0x80, 0x80
    };
    for(int pan = 0; pan < panTable.length; pan++) {
      this.pan[pan] = panTable[pan] / 128.0f;
    }
  }

  long calculateSampleRate(final int rootKey, final int note, final int finePitch, final int pitchBend, final int pitchBendMultiplier) {
    final int offsetIn128ths = (note - rootKey) * 128 + finePitch + pitchBend * pitchBendMultiplier;
    final int scaledOffset = offsetIn128ths >> this.pitchResolution.sampleRateShift;

    if(scaledOffset >= 0) {
      final int octaveOffset = scaledOffset / this.sampleRates.length;
      final int sampleRateOffset = scaledOffset - octaveOffset * this.sampleRates.length;
      return (this.sampleRates[sampleRateOffset] & 0xFFFF_FFFFL) << octaveOffset;
    }

    final int octaveOffset = (scaledOffset + 1) / -this.sampleRates.length + 1;
    final int sampleRateOffset = scaledOffset + octaveOffset * this.sampleRates.length;
    return (this.sampleRates[sampleRateOffset] & 0xFFFF_FFFFL) >> octaveOffset;
  }

  /**
   * Uses 4 points to return a Catmull-Rom spline interpolated value, which is somewhere between p1 and p2.
   * @param array data to interpolate over
   * @param position index of p0. Make sure there are at least 4 points available
   * @param interpolationIndex position between p1 and p2. 0 will return p1
   */
  float interpolate(final short[] array, final int position, final int interpolationIndex) {
    return this.interpolationWeights[0][interpolationIndex] * array[position]
      + this.interpolationWeights[1][interpolationIndex] * array[position + 1]
      + this.interpolationWeights[1][this.interpolationStep - interpolationIndex] * array[position + 2]
      + this.interpolationWeights[0][this.interpolationStep - interpolationIndex] * array[position + 3];
  }

  public float interpolate(final float[] array, final int position, final int interpolationIndex) {
    float out = this.interpolationWeights[0][interpolationIndex] * array[position];

    final int halfTaps = this.interpolation.taps / 2;

    for(int i = 1; i < halfTaps; i++) {
      out += this.interpolationWeights[i][interpolationIndex] * array[position + i];
    }

    final int reverseIndex = this.interpolationStep - interpolationIndex;
    final int halfIndex = halfTaps - 1;

    for(int i = 0; i < halfTaps; i++) {
      out += this.interpolationWeights[halfIndex - i][reverseIndex] * array[position + halfTaps + i];
    }

    return out;
  }

  FloatFloatImmutablePair getPan(final int channelPan, final int instrumentPan, final int layerPan) {
    final int panIndex = mergePan(channelPan, mergePan(instrumentPan, layerPan));

    return new FloatFloatImmutablePair(this.pan[127 - panIndex], this.pan[panIndex]);
  }

  private static int mergePan(final int pan1, final int pan2) {
    return Math.clamp(pan1 + pan2 - 60, 0, 0x7f);
  }

  // This will get scaled down later down the line, so we can to keep it in 128ths
  int modulate(final int finePitch, final float interpolatedBreath, final int modulation) {
    return finePitch + Math.round(interpolatedBreath * modulation);
  }

  void changePitchResolution(final PitchResolution pitchResolution) {
    this.pitchResolution = pitchResolution;
    this.sampleRates = new int[12 * pitchResolution.value];

    for(int i = 0; i < this.sampleRates.length; i++) {
      this.sampleRates[i] = (int)Math.round(BASE_SAMPLE_RATE_VALUE * SAMPLE_RATE_RATIO * Math.pow(2, i / (double)this.sampleRates.length));
    }
  }

  void calculateInterpolationWeights(final InterpolationPrecision bitDepth, final Interpolation interpolation) {
    this.interpolationStep = 1 << bitDepth.value;
    this.interpolation = interpolation;

    this.interpolationWeights = interpolation.generateWeights(this.interpolationStep);
  }

  public static float[][] catmullRomSplines(final int interpolationStep) {
    final float[][] weights = new float[2][];

    weights[0] = new float[interpolationStep + 1];
    weights[1] = new float[interpolationStep + 1];

    for(int i = 0; i <= interpolationStep; i++) {
      final double pow1 = i / (double)interpolationStep;
      final double pow2 = pow1 * pow1;
      final double pow3 = pow2 * pow1;

      weights[0][i] = (float)(0.5d * (-pow3 + 2 * pow2 - pow1));
      weights[1][i] = (float)(0.5d * (3 * pow3 - 5 * pow2 + 2));
    }

    return weights;
  }

  public static float[][] lagrangeWeights(final int interpolationStep, final int taps) {
    final float[][] weights = new float[taps / 2][];

    for(int set = 0; set < weights.length; set++) {
      weights[set] = new float[interpolationStep + 1];

      for(int weight = 0; weight < weights[set].length; weight++) {
        final double fraction = (double)weight / interpolationStep;

        weights[set][weight] = lagrangeWeight(fraction, set, taps);
      }
    }

    return weights;
  }

  private static float lagrangeWeight(final double fraction, final int currentTap, final int totalTaps) {
    final int tapsBase = 1 - totalTaps / 2;

    double numerator = 1.0f;
    double denominator = 1.0f;

    for(int tap = 0; tap < totalTaps; tap++) {
      if(tap == currentTap) {
        continue;
      }

      numerator *= fraction - (tapsBase + tap);
      denominator *= currentTap - tap;
    }

    return (float)(numerator / denominator);
  }

  public int getInterpolationStep() {
    return this.interpolationStep;
  }
}
