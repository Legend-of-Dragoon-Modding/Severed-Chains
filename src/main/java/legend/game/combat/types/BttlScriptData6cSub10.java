package legend.game.combat.types;

import legend.core.memory.Value;
import legend.core.memory.types.UnsignedIntRef;
import legend.core.memory.types.UnsignedShortRef;

public class BttlScriptData6cSub10 extends BttlScriptData6cSubBase1 {
  public final UnsignedShortRef count_04;

  public final UnsignedIntRef _08;
  /** TODO ptr */
  public final UnsignedIntRef _0c;

  public BttlScriptData6cSub10(final Value ref) {
    super(ref);

    this.count_04 = ref.offset(2, 0x04L).cast(UnsignedShortRef::new);
    this._08 = ref.offset(4, 0x08L).cast(UnsignedIntRef::new);
    this._0c = ref.offset(4, 0x0cL).cast(UnsignedIntRef::new);
  }
}
