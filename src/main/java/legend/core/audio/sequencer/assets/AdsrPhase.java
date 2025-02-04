package legend.core.audio.sequencer.assets;

import legend.core.audio.SampleRate;

import static legend.core.audio.AudioThread.BASE_SAMPLE_RATE;

public final class AdsrPhase {
  private final int target;
  private final boolean isDecreasing;
  private final boolean isExponential;

  // Shift has to be stored because of sample rate change
  private final int shift;

  private int adsrCounter;
  private final int adsrStep;

  AdsrPhase(final int target, final int shift, final int step, final boolean isDecreasing, final boolean isExponential, final SampleRate sampleRate) {
    this.target = target;
    this.isDecreasing = isDecreasing;
    this.isExponential = isExponential;

    this.shift = shift;

    this.adsrStep = step << Math.max(0, 11 - shift);
    this.adsrCounter = calculateCounter(shift, sampleRate);
  }

  public void changeSampleRate(final SampleRate sampleRate) {
    this.adsrCounter = calculateCounter(this.shift, sampleRate);
  }

  private static int calculateCounter(final int shift, final SampleRate sampleRate) {
    return (int)Math.round((1 << Math.min(30, 41 - shift)) * (BASE_SAMPLE_RATE / (double)sampleRate.value));
  }

  public int getTarget() {
    return this.target;
  }

  public boolean isDecreasing() {
    return this.isDecreasing;
  }

  public boolean isExponential() {
    return this.isExponential;
  }

  public int getAdsrStep() {
    return this.adsrStep;
  }
  public int getAdsrCounter() {
    return this.adsrCounter;
  }

  static AdsrPhase[] getPhases(final int lo, final int hi, final SampleRate sampleRate) {
    final AdsrPhase[] phases = new AdsrPhase[4];

    //Attack
    phases[0] = new AdsrPhase(
      0x7fff,
      (lo >> 10) & 0x1f,
      7 - ((lo >> 8) & 0x03),
      false,
      ((lo >> 15) & 0x01) != 0,
      sampleRate
    );

    //Decay
    phases[1] = new AdsrPhase(
      Math.min(((lo & 0x0f) + 1) * 0x800, 0x7fff),
      (lo >> 4) & 0x0f,
      -8,
      true,
      true,
      sampleRate
    );

    //Sustain
    final boolean sustainDecreasing = ((hi >> 14) & 0x01) != 0;
    final int sustainStep = (hi >> 6) & 0x03;

    phases[2] = new AdsrPhase(
      sustainDecreasing ? 15 : 0x7FFF,
      (hi >> 8) & 0x1f,
      sustainDecreasing ? -8 + sustainStep : 7 - sustainStep,
      sustainDecreasing,
      ((hi >> 15) & 0x01) != 0,
      sampleRate
    );

    //Release
    phases[3] = new AdsrPhase(
      15,
      hi & 0x1f,
      -8,
      true,
      ((hi >> 5) & 0x01) != 0,
      sampleRate
    );


    return phases;
  }
}
