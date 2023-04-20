package legend.game.soundSfx;

final class AdsrEnvelope implements legend.core.audio.AdsrEnvelope {
  private final AdsrPhase[] phases;
  private Phase phase;
  private int currentLevel;
  private int counter;

  AdsrEnvelope(final AdsrPhase[] phases) {
    this.phases = phases;
    this.phase = Phase.Attack;
    this.counter = 1 << Math.max(0, phases[0].getShift() - 11);
  }

  @Override
  public short tick() {
    if(this.counter > 0) {
      this.counter--;
      return (short)this.currentLevel;
    }

    if(this.phase == Phase.Off) {
      return 0;
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

    this.currentLevel += adsrStep;
    this.currentLevel = isDecreasing ? (short)Math.max(this.currentLevel, target) : (short)Math.min(this.currentLevel, target);

    this.counter += adsrCycles;

    final boolean nextPhase = isDecreasing ? this.currentLevel <= target : this.currentLevel >= target;

    if(nextPhase) {
      this.phase = this.phase.next(isDecreasing);
      this.counter = 0;
    }

    return (short)this.currentLevel;
  }

  @Override
  public void keyOff() {
    this.phase = Phase.Release;
    this.counter = 0;
  }

  @Override
  public void mute() {
    this.phase = Phase.Off;
    this.currentLevel = 0;
    this.counter = 0;
  }

  @Override
  public boolean isFinished() {
    return this.phase == Phase.Off;
  }

  int getCurrentLevel() {
    return this.currentLevel;
  }

  enum Phase {
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
