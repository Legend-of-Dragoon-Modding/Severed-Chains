package legend.game.combat.types;

import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.UnsignedIntRef;

public class BttlScriptData6cSub08_3 implements BttlScriptData6cSubBase1, MemoryRef {
  private final Value ref;

  public final UnsignedIntRef count_00;
  /** TODO ptr to array of 0xc-byte structs */
  public final UnsignedIntRef ptr_04;

  public BttlScriptData6cSub08_3(final Value ref) {
    this.ref = ref;

    this.count_00 = ref.offset(4, 0x00L).cast(UnsignedIntRef::new);
    this.ptr_04 = ref.offset(4, 0x04L).cast(UnsignedIntRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
