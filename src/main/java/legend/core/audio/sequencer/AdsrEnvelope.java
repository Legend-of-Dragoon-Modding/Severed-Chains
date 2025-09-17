package legend.core.audio.sequencer;

import legend.core.audio.sequencer.assets.AdsrPhase;

final class AdsrEnvelope {
  private AdsrPhase[] phases;
  private Phase phase;
  private int currentLevel;
  private float currentLevelF;
  private int counter;
  private int cycleAmount;

  void tick() {
    if(this.phase == Phase.OFF) {
      return;
    }

    if((this.counter >> 30) == 0) {
      this.counter += this.cycleAmount;
      return;
    }

    final AdsrPhase phase = this.phases[this.phase.value];
    int adsrStep = phase.getAdsrStep();
    final int target = phase.getTarget();
    final boolean isDecreasing = phase.isDecreasing();
    final boolean isExponential = phase.isExponential();


    if(isExponential && !isDecreasing && this.currentLevel > 0x6000) {
      this.cycleAmount = phase.getAdsrCounter() >> 2;
    }

    if(isExponential && isDecreasing) {
      adsrStep = (adsrStep * this.currentLevel) >> 15;
    }

    this.currentLevel = Math.clamp(this.currentLevel + adsrStep, isDecreasing ? target : 0, isDecreasing ? 0x7fff : target);
    this.currentLevelF = this.currentLevel / 32768.0f;

    final boolean nextPhase = this.currentLevel == target;

    if(nextPhase) {
      this.phase = this.phase.next(isDecreasing);
      if(this.phase != Phase.OFF) {
        final AdsrPhase newPhase = this.phases[this.phase.value];
        this.cycleAmount = newPhase.getAdsrCounter();
      }

      return;
    }

    this.counter -= 1 << 30;
  }

  void load(final AdsrPhase[] phases) {
    this.counter = 1 << 30;
    this.cycleAmount = phases[0].getAdsrCounter();
    this.currentLevel = 0;
    this.currentLevelF = 0.0f;
    this.phase = Phase.ATTACK;
    this.phases = phases;
  }

  float getCurrentLevel() {
    return this.currentLevelF;
  }

  void keyOff() {
    this.phase = Phase.RELEASE;
    this.counter = 1 << 30;
    this.cycleAmount = this.phases[3].getAdsrCounter();
  }

  boolean isFinished() {
    return this.phase == Phase.OFF;
  }

  private enum Phase {
    ATTACK(0),
    DECAY(1),
    SUSTAIN(2),
    RELEASE(3),
    OFF(-1);

    private final int value;

    Phase(final int value) {
      this.value = value;
    }

    private Phase next(final boolean isDecreasing) {
      return switch(this.value) {
        case 0 -> DECAY;
        case 1 -> SUSTAIN;
        case 2 -> isDecreasing ? OFF : SUSTAIN;
        default -> OFF;
      };
    }
  }
}
