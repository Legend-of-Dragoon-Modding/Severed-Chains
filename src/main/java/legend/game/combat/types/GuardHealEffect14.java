package legend.game.combat.types;

import legend.core.gte.TmdObjTable;
import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.UnsignedIntRef;
import legend.core.memory.types.UnsignedShortRef;
import legend.game.combat.deff.DeffPart;

public class GuardHealEffect14 implements BttlScriptData6cSubBase1, MemoryRef {
  private final Value ref;

  public final UnsignedIntRef _00;
  public final Pointer<DeffPart.TmdType> tmdType_04;
  public final Pointer<TmdObjTable> tmd_08;

  public final UnsignedShortRef _10;

  public GuardHealEffect14(final Value ref) {
    this.ref = ref;

    this._00 = ref.offset(4, 0x00L).cast(UnsignedIntRef::new);
    this.tmdType_04 = ref.offset(4, 0x04L).cast(Pointer.deferred(4, DeffPart.TmdType::new));
    this.tmd_08 = ref.offset(4, 0x08L).cast(Pointer.deferred(4, TmdObjTable::new));

    this._10 = ref.offset(2, 0x10L).cast(UnsignedShortRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
