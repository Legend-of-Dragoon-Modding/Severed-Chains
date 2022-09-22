package legend.core.spu;

import legend.core.IoHelper;

import java.nio.ByteBuffer;

class Control {
  public short register;

  public boolean spuEnabled() {
    return (this.register >> 15 & 0x1) != 0;
  }

  public boolean spuUnmuted() {
    return (this.register >> 14 & 0x1) != 0;
  }

  public int noiseFrequencyShift() {
    return this.register >> 10 & 0xF;
  }

  public int noiseFrequencyStep() {
    return this.register >> 8 & 0x3;
  }

  public boolean reverbMasterEnabled() {
    return (this.register >> 7 & 0x1) != 0;
  }

  public boolean irq9Enabled() {
    return (this.register >> 6 & 0x1) != 0;
  }

  public int soundRamTransferMode() {
    return this.register >> 4 & 0x3;
  }

  public boolean externalAudioReverb() {
    return (this.register >> 3 & 0x1) != 0;
  }

  public boolean cdAudioReverb() {
    return (this.register >> 2 & 0x1) != 0;
  }

  public boolean externalAudioEnabled() {
    return (this.register >> 1 & 0x1) != 0;
  }

  public boolean cdAudioEnabled() {
    return (this.register & 0x1) != 0;
  }

  public void dump(final ByteBuffer stream) {
    IoHelper.write(stream, this.register);
  }

  public void load(final ByteBuffer stream) {
    this.register = IoHelper.readShort(stream);
  }
}
