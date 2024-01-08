package legend.core.audio.sequencer;

import legend.core.MathHelper;
import legend.core.audio.sequencer.assets.AdsrPhase;

final class AdsrEnvelope {
  private AdsrPhase[] phases;
  private Phase phase;
  private int currentLevel;
  private int counter;

  void tick() {
    if(this.counter > 0) {
      this.counter--;
      return;
    }

    if(this.phase == Phase.Off) {
      return;
    }

    final AdsrPhase phase = this.phases[this.phase.value];
    final int step = phase.getStep();
    final int shift = phase.getShift();
    final int target = phase.getTarget();
    final boolean isDecreasing = phase.isDecreasing();
    final boolean isExponential = phase.isExponential();

    int adsrCycles = 1 << Math.max(0, shift - 11);
    int adsrStep = step << Math.max(0, 11 - shift);

    if(isExponential && !isDecreasing && this.currentLevel > 0x6000) {
      adsrCycles *= 4;
    }

    if(isExponential && isDecreasing) {
      adsrStep = (adsrStep * this.currentLevel) >> 15;
    }

    this.currentLevel = MathHelper.clamp(this.currentLevel + adsrStep, 0, 0x7fff);

    this.counter = adsrCycles;

    final boolean nextPhase = isDecreasing ? this.currentLevel <= target : this.currentLevel >= target;

    if(nextPhase) {
      this.phase = this.phase.next(isDecreasing);
      this.counter = 0;
    }
  }

  void load(final AdsrPhase[] phases) {
    this.counter = 0;
    this.currentLevel = 0;
    this.phase = Phase.Attack;
    this.phases = phases;
  }

  int getCurrentLevel() {
    return this.currentLevel;
  }

  void keyOff() {
    this.phase = Phase.Release;
    this.counter = 0;
  }

  boolean isFinished() {
    return this.phase == Phase.Off;
  }

  private enum Phase {
    Attack(0),
    Decay(1),
    Sustain(2),
    Release(3),
    Off(-1);

    private final int value;

    Phase(final int value) {
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
