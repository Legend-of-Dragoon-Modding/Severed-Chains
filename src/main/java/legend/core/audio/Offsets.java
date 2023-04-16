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

    for(int i = 0; i < 65; i++) {
      pan[i] = i * Math.PI / 256;
    }

    for(int i = 65; i < pan.length; i++) {
      pan[i] = i * Math.PI / 254;
    }
  }
}
