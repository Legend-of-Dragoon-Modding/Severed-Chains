package legend.core.spu;

class Control {
  public short register;

  public boolean spuUnmuted() {
    return (this.register >> 14 & 0x1) != 0;
  }

  public int noiseFrequencyShift() {
    return this.register >> 10 & 0xF;
  }

  public int noiseFrequencyStep() {
    return this.register >> 8 & 0x3;
  }

  public boolean cdAudioEnabled() {
    return (this.register & 0x1) != 0;
  }
}
