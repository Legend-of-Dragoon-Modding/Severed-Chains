package legend.game.types;

import legend.core.gte.VECTOR;
import legend.core.memory.Value;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.UnsignedIntRef;

public class WMapSubStruct18 implements MemoryRef {
  private final Value ref;

  public final IntRef index_00;
  public final IntRef vecLength_04;
  public final VECTOR vec_08;

  public WMapSubStruct18(final Value ref) {
    this.ref = ref;

    this.index_00 = ref.offset(4, 0x00L).cast(IntRef::new);
    this.vecLength_04 = ref.offset(4, 0x04L).cast(IntRef::new);
    this.vec_08 = ref.offset(4, 0x08L).cast(VECTOR::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
