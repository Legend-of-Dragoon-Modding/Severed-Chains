package legend.game.soundFinal;

final class Offsets {
  static double[] semitone = new double[12];
  static double[] cent = new double[100];

  static void genOffsets() {
    for(int i = 0; i < semitone.length; i++) {
      semitone[i] = Math.pow(1.05946d, i);
    }

    for(int i = 0; i < cent.length; i++) {
      cent[i] = Math.pow(1.0005946d, i);
    }
  }
}
