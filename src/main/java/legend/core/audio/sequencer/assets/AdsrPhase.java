package legend.core.audio.sequencer.assets;

public final class AdsrPhase {
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

  public int getTarget() {
    return this.target;
  }

  public int getShift() {
    return this.shift;
  }

  public int getStep() {
    return this.step;
  }

  public boolean isDecreasing() {
    return this.isDecreasing;
  }

  public boolean isExponential() {
    return this.isExponential;
  }

  static AdsrPhase[] getPhases(final int lo, final int hi) {
    final AdsrPhase[] phases = new AdsrPhase[4];

    //Attack
    phases[0] = new AdsrPhase(
      0x7fff,
      (lo >> 10) & 0x1f,
      7 - ((lo >> 8) & 0x03),
      false,
      ((lo >> 15) & 0x01) != 0
    );

    //Decay
    phases[1] = new AdsrPhase(
      Math.min(((lo & 0x0f) + 1) * 0x800, 0x7fff),
      (lo >> 4) & 0x0f,
      -8,
      true,
      true
    );

    //Sustain
    final boolean sustainDecreasing = ((hi >> 14) & 0x01) != 0;
    final int sustainStep = (hi >> 6) & 0x03;

    phases[2] = new AdsrPhase(
      sustainDecreasing ? 15 : 0x7FFF,
      (hi >> 8) & 0x1f,
      sustainDecreasing ? -8 + sustainStep : 7 - sustainStep,
      sustainDecreasing,
      ((hi >> 15) & 0x01) != 0
    );

    //Release
    phases[3] = new AdsrPhase(
      15,
      hi & 0x1f,
      -8,
      true,
      ((hi >> 5) & 0x01) != 0
    );


    return phases;
  }
}
