package legend.core.spu;

import static legend.core.audio.Constants.SAMPLE_RATE_RATIO;

final class NoiseGenerator {
  private static final long RETAIL_NOISE_BASE = 0x2_0000;
  private static final int SHIFT = 42;
  private static final long NOISE_BASE = RETAIL_NOISE_BASE << SHIFT;

  private long step;
  private int shift;

  private long counter;
  public int volume;
  public float volumeF;

  public void load(final int packed) {
    this.shift = packed >> 2 & 0xF;
    this.step = Math.round((((packed & 0x3) + 0x4L) << SHIFT) * SAMPLE_RATE_RATIO);
  }

  //Wait(1 cycle); at 44.1kHz clock
  //Timer=Timer-NoiseStep  ;subtract Step(4..7)
  //ParityBit = NoiseLevel.Bit15 xor Bit12 xor Bit11 xor Bit10 xor 1
  //IF Timer<0 then NoiseLevel = NoiseLevel * 2 + ParityBit
  //IF Timer<0 then Timer = Timer + (20000h SHR NoiseShift); reload timer once
  //IF Timer<0 then Timer = Timer + (20000h SHR NoiseShift); reload again if needed
  public void tick() {
    this.counter -= this.step;

    if(this.counter < 0) {
      final int parityBit = this.volume >> 15 & 0x1 ^ this.volume >> 12 & 0x1 ^ this.volume >> 11 & 0x1 ^ this.volume >> 10 & 0x1 ^ 1;
      this.volume = this.volume * 2 + parityBit;
      this.volumeF = (short)this.volume / (float)0x8000;

      this.counter += NOISE_BASE >> this.shift;
    }

    if(this.counter < 0) {
      this.counter += NOISE_BASE >> this.shift;
    }
  }
}
