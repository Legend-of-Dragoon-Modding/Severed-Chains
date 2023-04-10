package legend.game.soundFinal;

import it.unimi.dsi.fastutil.shorts.ShortArrayList;
import legend.core.IoHelper;
import legend.core.MathHelper;

final class AdsrEnvelope {
  private final AdsrPhase[] phases;
  private Phase phase;
  private short currentLevel;
  private int counter;

  AdsrEnvelope(final int lo, final int hi) {
    this.phases = AdsrPhase.getPhases(lo, hi);
    this.phase = Phase.Attack;
  }

  short get() {
    if(this.counter > 0) {
      this.counter--;
      return this.currentLevel;
    }

    int envelopeCycles = 1 << Math.max(0, this.phases[this.phase.value].getShift() - 11);
    int envelopeStep = this.phases[this.phase.value].getStep() << Math.max(0, 11 - this.phases[this.phase.value].getShift());

    if(this.phases[this.phase.value].isExponential && !this.phases[this.phase.value].isDecreasing && this.currentLevel > 0x6000) {
      envelopeCycles += 4;
    }

    if(this.phases[this.phase.value].isExponential && this.phases[this.phase.value].isDecreasing) {
      envelopeStep = envelopeCycles * this.currentLevel >> 15;
    }

    this.currentLevel = (short) MathHelper.clamp(this.currentLevel + envelopeStep, 0, 0x7fff);
    this.counter += envelopeCycles;

    final boolean nextPhase = this.phases[this.phase.value].isDecreasing ? this.currentLevel <= this.phases[this.phase.value].getTarget() : this.currentLevel >= this.phases[this.phase.value].getTarget();

    if(nextPhase) {
      this.phase = this.phase.next(this.phases[this.phase.value].isDecreasing);
      this.counter = 0;
    }

    return this.currentLevel;
  }

  void reset() {
    this.phase = Phase.Attack;
    this.currentLevel = 0;
  }

  void KeyOff() {
    this.phase = Phase.Release;
  }


  Phase getState() {
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

    private Phase next(final boolean isDecreasing) {
      return switch(this.value) {
        case 0 -> Decay;
        case 1 -> Sustain;
        case 2 -> isDecreasing ? Off : Sustain;
        default -> Off;
      };
    }
  }
}
