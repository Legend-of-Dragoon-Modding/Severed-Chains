package legend.core.spu;

import legend.core.IoHelper;

import java.nio.ByteBuffer;

class Status {
  public short register;

  public boolean isSecondHalfCaptureBuffer() {
    return (this.register >> 11 & 0x1) != 0;
  }

  public boolean dataTransferBusyFlag() {
    return (this.register >> 10 & 0x1) != 0;
  }

  public boolean dataTransferDmaReadRequest() {
    return (this.register >> 9 & 0x1) != 0;
  }

  public boolean dataTransferDmaWriteRequest() {
    return (this.register >> 8 & 0x1) != 0;
  }

  //  7     Data Transfer DMA Read/Write Request ;seems to be same as SPUCNT.Bit5 todo
  public boolean irq9Flag() {
    return (this.register >> 6 & 0x1) != 0;
  }

  public void irq9Flag(final boolean value) {
    this.register = (short)(value ? this.register | 1 << 6 : this.register & ~(1 << 6));
  }

  public void dump(final ByteBuffer stream) {
    IoHelper.write(stream, this.register);
  }

  public void load(final ByteBuffer stream) {
    this.register = IoHelper.readShort(stream);
  }
}
