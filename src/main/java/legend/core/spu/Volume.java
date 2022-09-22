package legend.core.spu;

import legend.core.IoHelper;

import java.nio.ByteBuffer;

public class Volume {
  private short register;

  public long get() {
    return this.register;
  }

  public void set(final long val) {
    this.register = (short)val;
  }

  public boolean isSweepMode() {
    return (this.register >> 15 & 0x1) != 0;
  }

  public short fixedVolume() {
    return (short)(this.register << 1);
  }

  public boolean isSweepExponential() {
    return (this.register >> 14 & 0x1) != 0;
  }

  public boolean isSweepDirectionDecrease() {
    return (this.register >> 13 & 0x1) != 0;
  }

  public boolean isSweepPhaseNegative() {
    return (this.register >> 12 & 0x1) != 0;
  }

  public int sweepShift() {
    return this.register >> 2 & 0x1F;
  }

  public int sweepStep() {
    return this.register & 0x3;
  }

  public void dump(final ByteBuffer stream) {
    IoHelper.write(stream, this.register);
  }

  public void load(final ByteBuffer stream) {
    this.register = IoHelper.readShort(stream);
  }
}
