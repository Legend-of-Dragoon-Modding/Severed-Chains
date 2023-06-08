package legend.core.audio.assets;

import legend.core.MathHelper;

public final class AdsrPhase {
  private final int target;
  private final int shift;
  private final int step;
  private final boolean isDecreasing;
  private final boolean isExponential;

  AdsrPhase(final int target, final int shift, final int step, final boolean isDecreasing, final boolean isExponential) {
    this.target = MathHelper.clamp(target, 0, 0x7FFF);
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


    return phases;
  }
}
