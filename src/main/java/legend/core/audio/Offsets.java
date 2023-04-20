package legend.core.audio;

final class Offsets {
  static double[] semitone = new double[12];
  static double[] cent = new double[100];
  static double[] pan = new double[128];


  static void genOffsets() {
    for(int i = 0; i < semitone.length; i++) {
      semitone[i] = Math.pow(1.05946d, i);
    }

    for(int i = 0; i < cent.length; i++) {
      cent[i] = Math.pow(1.0005946d, i);
    }

    for(int i = 0; i <= 60; i++) {
      pan[i] = i * 4 / 256.0;
    }

    for(int i = 61; i < 128; i++) {
      pan[i] = 1.0;
    }
  }
}
