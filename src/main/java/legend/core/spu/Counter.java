package legend.core.spu;

public class Counter {            //internal
  public int register;

  public int currentSampleIndex() {
    return this.register >> 12 & 0x1F;
  }

  public void currentSampleIndex(final int value) {
    this.register = (short)(this.register & 0xFFF);
    this.register |= value << 12;
  }

  public int interpolationIndex() {
    return this.register >> 3 & 0xFF;
  }
}
