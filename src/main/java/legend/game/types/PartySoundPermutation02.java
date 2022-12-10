package legend.game.types;

import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.UnsignedByteRef;

public class PartySoundPermutation02 implements MemoryRef {
  private final Value ref;

  public final UnsignedByteRef characterId0_00;
  public final UnsignedByteRef characterId1_01;

  public PartySoundPermutation02(final Value ref) {
    this.ref = ref;

    this.characterId0_00 = ref.offset(1, 0x00L).cast(UnsignedByteRef::new);
    this.characterId1_01 = ref.offset(1, 0x01L).cast(UnsignedByteRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
