package legend.game.combat.types;

import legend.core.gte.TmdObjTable1c;
import legend.core.memory.Value;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.UnsignedShortRef;
import legend.game.combat.deff.DeffPart;

public class BttlScriptData6cSub30Sub10 implements MemoryRef {
  private final Value ref;

  public final IntRef flags_00;
  public final Pointer<DeffPart.TmdType> tmdType_04;
  public final Pointer<TmdObjTable1c> tmd_08;

  public final UnsignedShortRef tpage_10;

  public BttlScriptData6cSub30Sub10(final Value ref) {
    this.ref = ref;

    this.flags_00 = ref.offset(4, 0x00L).cast(IntRef::new);
    this.tmdType_04 = ref.offset(4, 0x04L).cast(Pointer.deferred(4, DeffPart.TmdType::new));
    this.tmd_08 = ref.offset(4, 0x08L).cast(Pointer.deferred(4, TmdObjTable1c::new));

    this.tpage_10 = ref.offset(2, 0x10L).cast(UnsignedShortRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
