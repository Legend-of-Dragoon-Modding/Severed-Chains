package legend.game.combat.types;

import legend.core.gte.TmdObjTable;
import legend.core.memory.Value;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.UnsignedIntRef;
import legend.core.memory.types.UnsignedShortRef;

public class BttlScriptData6cSub14_2 extends BttlScriptData6cSubBase1 {
  public final UnsignedIntRef _00;
  public final UnsignedIntRef _04;
  public final Pointer<TmdObjTable> tmd_08;

  public final UnsignedShortRef _10;

  public BttlScriptData6cSub14_2(final Value ref) {
    super(ref);

    this._00 = ref.offset(4, 0x00L).cast(UnsignedIntRef::new);
    this._04 = ref.offset(4, 0x04L).cast(UnsignedIntRef::new);
    this.tmd_08 = ref.offset(4, 0x08L).cast(Pointer.deferred(4, TmdObjTable::new));

    this._10 = ref.offset(2, 0x10L).cast(UnsignedShortRef::new);
  }
}
