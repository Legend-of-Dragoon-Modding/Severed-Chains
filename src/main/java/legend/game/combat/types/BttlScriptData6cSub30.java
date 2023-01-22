package legend.game.combat.types;

import legend.core.gte.TmdObjTable;
import legend.core.gte.VECTOR;
import legend.core.memory.Value;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.UnboundedArrayRef;
import legend.core.memory.types.UnsignedShortRef;

public class BttlScriptData6cSub30 implements BttlScriptData6cSubBase1, MemoryRef {
  private final Value ref;

  public final IntRef _00;
  public final IntRef _04;
  public final IntRef _08;
  public final IntRef _0c;
  public final IntRef _10;
  public final IntRef _14;
  public final Pointer<UnboundedArrayRef<VECTOR>> _18;
  /** Overlapped */
  public final AttackHitFlashEffect0c _1c;
  public final Pointer<TmdObjTable> tmd_24;

  public final UnsignedShortRef tpage_2c;

  public BttlScriptData6cSub30(final Value ref) {
    this.ref = ref;

    this._00 = ref.offset(4, 0x00L).cast(IntRef::new);
    this._04 = ref.offset(4, 0x04L).cast(IntRef::new);
    this._08 = ref.offset(4, 0x08L).cast(IntRef::new);
    this._0c = ref.offset(4, 0x0cL).cast(IntRef::new);
    this._10 = ref.offset(4, 0x10L).cast(IntRef::new);
    this._14 = ref.offset(4, 0x14L).cast(IntRef::new);
    this._18 = ref.offset(4, 0x18L).cast(Pointer.deferred(4, UnboundedArrayRef.of(0x10, VECTOR::new)));
    this._1c = ref.offset(4, 0x1cL).cast(AttackHitFlashEffect0c::new);
    this.tmd_24 = ref.offset(4, 0x24L).cast(Pointer.deferred(4, TmdObjTable::new));

    this.tpage_2c = ref.offset(2, 0x2cL).cast(UnsignedShortRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
