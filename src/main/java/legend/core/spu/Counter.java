package legend.core.spu;

import legend.core.IoHelper;

import java.nio.ByteBuffer;

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

  public void dump(final ByteBuffer stream) {
    IoHelper.write(stream, this.register);
  }

  public void load(final ByteBuffer stream) {
    this.register = IoHelper.readInt(stream);
  }
}
