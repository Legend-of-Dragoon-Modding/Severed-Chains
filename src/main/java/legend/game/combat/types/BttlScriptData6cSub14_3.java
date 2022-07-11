package legend.game.combat.types;

import legend.core.memory.Value;
import legend.core.memory.types.UnsignedIntRef;
import legend.core.memory.types.UnsignedShortRef;

public class BttlScriptData6cSub14_3 extends BttlScriptData6cSubBase1 {
  public final UnsignedShortRef count_00;

  public final UnsignedIntRef _04;
  /** TODO array of 0x48-byte structs */
  public final UnsignedIntRef _08;

  public BttlScriptData6cSub14_3(final Value ref) {
    super(ref);

    this.count_00 = ref.offset(2, 0x00L).cast(UnsignedShortRef::new);

    this._04 = ref.offset(4, 0x04L).cast(UnsignedIntRef::new);
    this._08 = ref.offset(4, 0x08L).cast(UnsignedIntRef::new);
  }
}
