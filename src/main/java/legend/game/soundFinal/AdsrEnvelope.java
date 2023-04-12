package legend.game.soundFinal;

import legend.core.MathHelper;

final class AdsrEnvelope {
  private final AdsrPhase[] phases;
  private Phase phase;
  private short currentLevel;
  private int counter;

  AdsrEnvelope(final AdsrPhase[] phases) {
    this.phases = phases;
    this.phase = AdsrEnvelope.Phase.Attack;
  }

  short get() {
    if(this.counter > 0) {
      this.counter--;
      return this.currentLevel;
    }

    final AdsrPhase phase = this.phases[this.phase.value];

    int envelopeCycles = 1 << Math.max(0, phase.getShift() - 11);
    int envelopeStep = phase.getStep() << Math.max(0, 11 - phase.getShift());

    if(phase.isExponential && !phase.isDecreasing && this.currentLevel > 0x6000) {
      envelopeCycles += 4;
    }

    if(phase.isExponential && phase.isDecreasing) {
      envelopeStep = envelopeCycles * this.currentLevel >> 15;
    }

    this.currentLevel = (short) MathHelper.clamp(this.currentLevel + envelopeStep, 0, 0x7fff);
    this.counter += envelopeCycles;

    final boolean nextPhase = phase.isDecreasing ? this.currentLevel <= phase.getTarget() : this.currentLevel >= phase.getTarget();

    if(nextPhase) {
      this.phase = this.phase.next(phase.isDecreasing);
      this.counter = 0;
    }

    return this.currentLevel;
  }

  void KeyOff() {
    this.phase = Phase.Release;
  }


  AdsrEnvelope.Phase getState() {
    return this.phase;
  }

  enum Phase {
    Attack(0),
    Decay(1),
    Sustain(2),
    Release(3),
    Off(-1);

    private final int value;

    private Phase(final int value) {
      this.value = value;
    }

    private AdsrEnvelope.Phase next(final boolean isDecreasing) {
      return switch(this.value) {
        case 0 -> Decay;
        case 1 -> Sustain;
        case 2 -> isDecreasing ? Off : Sustain;
        default -> Off;
      };
    }
  }
}
