package legend.core.audio.sequencer;

import it.unimi.dsi.fastutil.floats.FloatFloatImmutablePair;
import legend.core.MathHelper;

import static legend.core.audio.AudioThread.SAMPLE_RATE_RATIO;

final class LookupTables {
  public static final int VOICE_COUNTER_BIT_PRECISION = 24;
  private static final double BASE_SAMPLE_RATE_VALUE = (1 << VOICE_COUNTER_BIT_PRECISION) * SAMPLE_RATE_RATIO;
  private final int[] sampleRates = new int[128 * 12];
  private final int interpolationStep;
  private final float[][] interpolationWeights = new float[2][];
  private final float[] pan = new float[0x80];

  LookupTables(final int interpolationBitDepth) {
    for(int i = 0; i < this.sampleRates.length; i++) {
      this.sampleRates[i] = (int)Math.round(BASE_SAMPLE_RATE_VALUE * Math.pow(2, i / (double)this.sampleRates.length));
    }

    this.interpolationStep = 1 << interpolationBitDepth;

    // The weights for Catmull-Rom splines are symmetrical, hence we can just store both of the unique sets and use a reverse index for half of them
    this.interpolationWeights[0] = new float[this.interpolationStep + 1];
    this.interpolationWeights[1] = new float[this.interpolationStep + 1];

    for(int i = 0; i <= this.interpolationStep; i++) {
      final double pow1 = i / (double)this.interpolationStep;
      final double pow2 = pow1 * pow1;
      final double pow3 = pow2 * pow1;

      this.interpolationWeights[0][i] = (float)(0.5d * (-pow3 + 2 * pow2 - pow1));
      this.interpolationWeights[1][i] = (float)(0.5d * (3 * pow3 - 5 * pow2 + 2));
    }

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

  int getSampleRate(final int index) {
    return this.sampleRates[index];
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

  FloatFloatImmutablePair getPan(final int channelPan, final int instrumentPan, final int layerPan) {
    final int panIndex = mergePan(channelPan, mergePan(instrumentPan, layerPan));

    return new FloatFloatImmutablePair(this.pan[127 - panIndex], this.pan[panIndex]);
  }

  private static int mergePan(final int pan1, final int pan2) {
    return MathHelper.clamp(pan1 + pan2 - 60, 0, 0x7f);
  }
}
