package legend.core.audio;

final class Offsets {
  static final int[] sampleRates = new int[768 + 64];
  static double[] pan = new double[128];


  static void genOffsets() {
    for(int i = 0; i <= 60; i++) {
      pan[i] = i * 4 / 256.0;
    }

    for(int i = 61; i < 128; i++) {
      pan[i] = 1.0;
    }

    for(int i = 64; i < sampleRates.length; i++) {
      sampleRates[i] = (int)(0x1000 * Math.pow(2, (i - 64) / 768d));
    }

    System.arraycopy(sampleRates, sampleRates.length - 64, sampleRates, 0, 64);

    for(int i = 0; i < 64; i++) {
      sampleRates[i] = sampleRates[i] >> 1;
    }
  }
}
