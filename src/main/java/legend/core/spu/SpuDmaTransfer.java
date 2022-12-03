package legend.core.spu;

import legend.core.memory.Value;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.UnsignedIntRef;

public class SpuDmaTransfer implements MemoryRef {
  private final Value ref;

  public final UnsignedIntRef ramAddress;
  public final IntRef size;
  public final IntRef soundBufferAddress;

  public SpuDmaTransfer(final Value ref) {
    this.ref = ref;

    this.ramAddress = ref.offset(4, 0x0L).cast(UnsignedIntRef::new);
    this.size = ref.offset(4, 0x4L).cast(IntRef::new);
    this.soundBufferAddress = ref.offset(4, 0x8L).cast(IntRef::new);
  }

  public void set(final SpuDmaTransfer other) {
    this.ramAddress.set(other.ramAddress);
    this.size.set(other.size);
    this.soundBufferAddress.set(other.soundBufferAddress);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
