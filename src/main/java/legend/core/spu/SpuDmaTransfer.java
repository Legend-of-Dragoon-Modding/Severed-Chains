package legend.core.spu;

import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;

public class SpuDmaTransfer implements MemoryRef {
  private final Value ref;

  public final Value ramAddress;
  public final Value size;
  public final Value soundBufferAddress;

  public SpuDmaTransfer(final Value ref) {
    this.ref = ref;

    this.ramAddress = ref.offset(4, 0x0L);
    this.size = ref.offset(4, 0x4L);
    this.soundBufferAddress = ref.offset(4, 0x8L);
  }

  public void set(final SpuDmaTransfer other) {
    this.ramAddress.setu(other.ramAddress);
    this.size.setu(other.size);
    this.soundBufferAddress.setu(other.soundBufferAddress);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
