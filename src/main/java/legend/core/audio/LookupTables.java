package legend.core.audio;

final class LookupTables {
  private static final int BASE_SAMPLE_RATE = 0x1000;
  private final int[] sampleRates;
  private final double[][] interpolationWeights;

  LookupTables(final int sampleRateStep, final int interpolationStep) {
    this.sampleRates = new int[sampleRateStep * 12];

    for(int i = 0; i < this.sampleRates.length; i++) {
      this.sampleRates[i] = (int)Math.round(BASE_SAMPLE_RATE * Math.pow(2, i / (double)this.sampleRates.length));
    }

    this.interpolationWeights = new double[interpolationStep][];

    for(int i = 0; i < this.interpolationWeights.length; i++) {
      final double pow1 = i / (double)this.interpolationWeights.length;
      final double pow2 = pow1 * pow1;
      final double pow3 = pow2 * pow1;

      //TODO this should be fine tuned. The algorithm would use 0.5, but the the original table doesn't add up to 0x8000h, so it would cause clipping. 0.45 is a safe value, but it might be possible increase this to 0.48
      this.interpolationWeights[i] = new double[] {
        0.45d * (-pow3 + 2 * pow2 - pow1),
        0.45d * (3 * pow3 - 5 * pow2 + 2),
        0.45d * (-3 * pow3 + 4 * pow2 + pow1),
        0.45d * (pow3 - pow2)
      };
    }
  }

  int getSampleRate(final int index) {
    return this.sampleRates[index];
  }

  double[] getInterpolationWeights(final int index) {
    return this.interpolationWeights[index];
  }
}
