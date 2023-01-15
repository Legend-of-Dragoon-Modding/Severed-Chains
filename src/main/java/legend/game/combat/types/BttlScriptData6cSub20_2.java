package legend.game.combat.types;

import legend.core.memory.Value;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.UnsignedIntRef;

public class BttlScriptData6cSub20_2 implements BttlScriptData6cSubBase1, MemoryRef {
  private final Value ref;

  public final IntRef count_00;
  public final UnsignedIntRef _04;
  /** TODO */
  public final UnsignedIntRef ptr_08;

  public BttlScriptData6cSub20_2(final Value ref) {
    this.ref = ref;

    this.count_00 = ref.offset(4, 0x00L).cast(IntRef::new);
    this._04 = ref.offset(4, 0x04L).cast(UnsignedIntRef::new);
    this.ptr_08 = ref.offset(4, 0x08L).cast(UnsignedIntRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
