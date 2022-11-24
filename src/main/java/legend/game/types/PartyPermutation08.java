package legend.game.types;

import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.UnsignedShortRef;

public class PartyPermutation08 implements MemoryRef {
  private final Value ref;

  public final UnsignedShortRef drgn0File_00;
  public final ArrayRef<UnsignedShortRef> charIndices_02;

  public PartyPermutation08(final Value ref) {
    this.ref = ref;

    this.drgn0File_00 = ref.offset(2, 0x00L).cast(UnsignedShortRef::new);
    this.charIndices_02 = ref.offset(2, 0x02L).cast(ArrayRef.of(UnsignedShortRef.class, 3, 2, UnsignedShortRef::new));
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
