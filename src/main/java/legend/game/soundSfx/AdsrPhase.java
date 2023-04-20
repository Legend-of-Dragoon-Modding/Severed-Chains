package legend.game.soundSfx;

final class AdsrPhase {
  private final int target;
  private final int shift;
  private final int step;
  private final boolean isDecreasing;
  private final boolean isExponential;

  AdsrPhase(final int target, final int shift, final int step, final boolean isDecreasing, final boolean isExponential) {
    this.target = target;
    this.shift = shift;
    this.step = step;
    this.isDecreasing = isDecreasing;
    this.isExponential = isExponential;
  }

  int getTarget() {
    return this.target;
  }

  int getShift() {
    return this.shift;
  }

  int getStep() {
    return this.step;
  }

  boolean isDecreasing() {
    return this.isDecreasing;
  }

  boolean isExponential() {
    return this.isExponential;
  }

  static AdsrPhase[] getPhases(final int lo, final int hi) {
    final AdsrPhase[] phases = new AdsrPhase[4];

    //Attack
    phases[0] = new AdsrPhase(
      0x7fff,
      (lo >> 10) & 0x1f,
      7 - ((lo >> 8)  & 0x3),
      false,
      ((lo >> 15) & 0x1) != 0
    );

    //Decay
    phases[1] = new AdsrPhase(
      (lo & 0xf) * 0x800,
      (lo >> 4) & 0xf,
      -8,
      true,
      true
    );

    //Sustain
    final boolean sustainDecreasing = (hi >> 14 & 0x1) != 0;
    final int sustainStep = (hi >> 6) & 0x3;

    phases[2] = new AdsrPhase(
      sustainDecreasing ? 0 : 0x7fff,
      (hi >> 8) & 0x1f,
      sustainDecreasing ? -8 + sustainStep : 7 - sustainStep,
      sustainDecreasing,
      (hi >> 15 & 0x1) != 0
    );

    //Release
    // target is actually 15, because anything below 16 gets cleared
    phases[3] = new AdsrPhase(
      15,
      hi & 0x1f,
      -8,
      true,
      (hi >> 5 & 0x1) != 0
    );

    return phases;
  }
}
