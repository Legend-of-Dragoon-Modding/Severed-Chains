package legend.game.combat.types;

import legend.core.memory.Value;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.UnsignedIntRef;

public class EffeScriptData18 implements MemoryRef {
  private final Value ref;

  public final IntRef ticksRemaining_00;
  public final IntRef _04;
  public final UnsignedIntRef count_08;
  public final UnsignedIntRef _0c;
  public final UnsignedIntRef ptr_10;
  public final UnsignedIntRef ptr_14;

  public EffeScriptData18(final Value ref) {
    this.ref = ref;

    this.ticksRemaining_00 = ref.offset(4, 0x00L).cast(IntRef::new);
    this._04 = ref.offset(4, 0x04L).cast(IntRef::new);
    this.count_08 = ref.offset(4, 0x08L).cast(UnsignedIntRef::new);
    this._0c = ref.offset(4, 0x0cL).cast(UnsignedIntRef::new);
    this.ptr_10 = ref.offset(4, 0x10L).cast(UnsignedIntRef::new);
    this.ptr_14 = ref.offset(4, 0x14L).cast(UnsignedIntRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
