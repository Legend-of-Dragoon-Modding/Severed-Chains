package legend.core.spu;

class Control {
  public short register;

  public int noiseFrequencyShift() {
    return this.register >> 10 & 0xF;
  }

  public int noiseFrequencyStep() {
    return this.register >> 8 & 0x3;
  }
}
