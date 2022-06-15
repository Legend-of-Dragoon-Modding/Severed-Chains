package legend.game.combat.types;

import legend.core.gte.VECTOR;
import legend.core.memory.Value;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.UnsignedIntRef;

public class BttlLightStruct84Sub3c implements MemoryRef {
  private final Value ref;

  public final UnsignedIntRef _00;
  public final VECTOR vec_04;
  public final VECTOR vec_10;
  public final VECTOR vec_1c;
  public final VECTOR vec_28;
  public final IntRef _34;
  public final IntRef scriptIndex_38;

  public BttlLightStruct84Sub3c(final Value ref) {
    this.ref = ref;

    this._00 = ref.offset(4, 0x00L).cast(UnsignedIntRef::new);
    this.vec_04 = ref.offset(4, 0x04L).cast(VECTOR::new);
    this.vec_10 = ref.offset(4, 0x10L).cast(VECTOR::new);
    this.vec_1c = ref.offset(4, 0x1cL).cast(VECTOR::new);
    this.vec_28 = ref.offset(4, 0x28L).cast(VECTOR::new);
    this._34 = ref.offset(4, 0x34L).cast(IntRef::new);
    this.scriptIndex_38 = ref.offset(4, 0x38L).cast(IntRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
