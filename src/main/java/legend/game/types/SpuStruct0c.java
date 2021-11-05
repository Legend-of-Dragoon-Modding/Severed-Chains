package legend.game.types;

import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.UnsignedIntRef;

public class SpuStruct0c implements MemoryRef {
  private final Value ref;

  public final UnsignedIntRef used_00;
  public final UnsignedIntRef sshdPtr_04;
  public final UnsignedIntRef soundBufferPtr_08;

  public SpuStruct0c(final Value ref) {
    this.ref = ref;

    this.used_00 = ref.offset(4, 0x00L).cast(UnsignedIntRef::new);
    this.sshdPtr_04 = ref.offset(4, 0x04L).cast(UnsignedIntRef::new);
    this.soundBufferPtr_08 = ref.offset(4, 0x08L).cast(UnsignedIntRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
