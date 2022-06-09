package legend.game.combat.types;

import legend.core.memory.Value;
import legend.core.memory.types.UnsignedIntRef;
import legend.core.memory.types.UnsignedShortRef;

public class BttlScriptData6cSub14 extends BttlScriptData6cSubBase1 {
  public final UnsignedIntRef _08;

  /** TODO ptr */
  public final UnsignedShortRef _10;

  public BttlScriptData6cSub14(final Value ref) {
    super(ref);

    this._08 = ref.offset(4, 0x08L).cast(UnsignedIntRef::new);

    this._10 = ref.offset(2, 0x10L).cast(UnsignedShortRef::new);
  }
}
