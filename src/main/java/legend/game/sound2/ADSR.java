package legend.game.sound2;

import it.unimi.dsi.fastutil.floats.FloatArrayList;
import it.unimi.dsi.fastutil.shorts.ShortArrayList;

class ADSR {
  private final short[] ad;

  private int index = 0;
  private int currentLevel = 0;
  private State state = State.AttackDecay;

  private final boolean attackExponential;
  private final int attackShift;
  private final int attackStep;

  private final int decayShift;
  private final int decayStep = -8;

  private final boolean releaseExponential;
  private final int releaseShift;
  private final int releaseStep = -8;

  private final int sustainLevel;

  ADSR(final int lo, final int hi) {
    this.attackExponential = ((lo >> 15) & 0x1) != 0;
    this.attackShift = lo >> 10 & 0x1f;
    this.attackStep = lo >> 8 & 0x3; //"+7,+6,+5,+4"

    this.decayShift = lo >> 4 & 0xf;
    this.sustainLevel = (lo & 0xf) * 0x800;

    this.releaseExponential = (hi >> 5 & 0x1) != 0;
    this.releaseShift = hi & 0x1f;


    this.ad = this.calculateAd();
  }

  boolean applyAdsr(final short[] pcm) {
    int pcmIndex = 0;

    if(this.state != State.Release) {
      for (int i = this.index; i < this.ad.length && i < pcm.length; i++) {
        pcm[pcmIndex] = (short) (pcm[pcmIndex] * (this.ad[this.index] / 32768f));
        pcmIndex++;
        this.index++;
      }

      if(this.index >= this.ad.length) {
        for(int i = pcmIndex; i < pcm.length; i++) {
          pcm[pcmIndex] = (short) (pcm[pcmIndex] * (this.sustainLevel / 32768f));
          pcmIndex++;
        }
        this.state = State.Sustain;

        this.currentLevel = this.sustainLevel;

        return false;
      }

      this.currentLevel = this.ad[this.index];

      return false;
    }

    final ShortArrayList decay = this.loop(this.currentLevel, 0, this.releaseShift, this.releaseStep, this.releaseExponential, false);

    for(int i = 0; i < decay.size() && i < pcm.length; i++) {
      this.currentLevel = decay.getShort(i);
      pcm[i] = (short) (pcm[i] * (this.currentLevel / 32768f));
      pcmIndex++;
    }

    if(pcmIndex < pcm.length) {
      for(int i = pcmIndex; i < pcm.length; i++) {
        pcm[i] = 0;
      }

      return true;
    }

    return false;
  }

  void reset() {
    this.index = 0;
    this.state = State.AttackDecay;
  }

  void release() {
    this.state = State.Release;
  }


  private short[] calculateAd() {
    final ShortArrayList ad = this.loop(0, 0x7FFF, this.attackShift, this.attackStep, this.attackExponential, true);
    ad.addAll(0, this.loop(0x7FFF, this.sustainLevel, this.decayShift, this.decayStep, true, false));

    return ad.toShortArray();
  }

  private ShortArrayList loop(final int start, final int target, final int shift, final int step, final boolean exponential, final boolean increase) {
    final ShortArrayList shorts = new ShortArrayList();

    int adsrLevel = start;

    int adsrCycles = 1 << Math.max(0, shift - 11);
    int adsrStep = step << Math.max(0, 11 - shift);

    while(adsrLevel != target) {
      if(exponential && increase && adsrCycles > 0x6000) {
        adsrCycles *= 4;
      }

      if(exponential && !increase) {
        adsrStep = adsrStep * adsrLevel / 0x8000;
      }

      adsrLevel += adsrStep;

      if(increase) {
        adsrLevel = Math.max(adsrLevel, target);
      } else {
        adsrLevel = Math.min(adsrLevel, target);
      }

      for(int i = 0; i < adsrCycles; i++) {
        shorts.add((short) adsrLevel);
      }
    }

    return shorts;
  }

  enum State {
    AttackDecay,
    Sustain,
    Release
  }
}
