package legend.core.spu;

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
}
