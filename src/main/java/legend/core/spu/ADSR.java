package legend.core.spu;

import legend.core.IoHelper;

import java.nio.ByteBuffer;

public class ADSR {
  public int lo;               //8
  public int hi;               //A

  public boolean isAttackModeExponential() {
    return (this.lo >> 15 & 0x1) != 0;
  }

  public int attackShift() {
    return this.lo >> 10 & 0x1f;
  }

  public int attackStep() {
    return this.lo >> 8 & 0x3; //"+7,+6,+5,+4"
  }

  public int decayShift() {
    return this.lo >> 4 & 0xf;
  }

  public int sustainLevel() {
    return this.lo & 0xf; //Level=(N+1)*800h
  }

  public boolean isSustainModeExponential() {
    return (this.hi >> 15 & 0x1) != 0;
  }

  public boolean isSustainDirectionDecrease() {
    return (this.hi >> 14 & 0x1) != 0;
  }

  public int sustainShift() {
    return this.hi >> 8 & 0x1f;
  }

  public int sustainStep() {
    return this.hi >> 6 & 0x3;
  }

  public boolean isReleaseModeExponential() {
    return (this.hi >> 5 & 0x1) != 0;
  }

  public int releaseShift() {
    return this.hi & 0x1f;
  }

  public void dump(final ByteBuffer stream) {
    IoHelper.write(stream, this.lo);
    IoHelper.write(stream, this.hi);
  }

  public void load(final ByteBuffer stream) {
    this.lo = IoHelper.readInt(stream);
    this.hi = IoHelper.readInt(stream);
  }
}
