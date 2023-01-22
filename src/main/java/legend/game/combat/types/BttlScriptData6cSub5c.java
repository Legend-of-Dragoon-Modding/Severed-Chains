package legend.game.combat.types;

import legend.core.gte.TmdObjTable;
import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.UnboundedArrayRef;
import legend.game.combat.deff.Lmb;
import legend.game.combat.deff.LmbTransforms14;

public class BttlScriptData6cSub5c implements BttlScriptData6cSubBase1, MemoryRef {
  private final Value ref;

  public final IntRef lmbType_00;
  public final IntRef _04;
  public final IntRef _08;
  public final Pointer<Lmb> lmb_0c;
  /** May have two copies of the LMB table for some reason */
  public final Pointer<UnboundedArrayRef<LmbTransforms14>> ptr_10;
  public final ArrayRef<IntRef> _14;
  public final IntRef _34;
  public final IntRef _38;
  public final IntRef _3c;
  public final IntRef _40;

  public final IntRef deffTmdFlags_48;
  public final Pointer<TmdObjTable> deffTmdObjTable_4c;
  public final IntRef deffSpriteFlags_50;
  public final SpriteMetrics08 metrics_54;

  public BttlScriptData6cSub5c(final Value ref) {
    this.ref = ref;

    this.lmbType_00 = ref.offset(4, 0x00L).cast(IntRef::new);
    this._04 = ref.offset(4, 0x04L).cast(IntRef::new);
    this._08 = ref.offset(4, 0x08L).cast(IntRef::new);
    this.lmb_0c = ref.offset(4, 0x0cL).cast(Pointer.deferred(4, value -> { throw new RuntimeException("Can't be instantiated"); }));
    this.ptr_10 = ref.offset(4, 0x10L).cast(Pointer.deferred(4, UnboundedArrayRef.of(0x14, LmbTransforms14::new)));
    this._14 = ref.offset(4, 0x14L).cast(ArrayRef.of(IntRef.class, 8, 4, IntRef::new));
    this._34 = ref.offset(4, 0x34L).cast(IntRef::new);
    this._38 = ref.offset(4, 0x38L).cast(IntRef::new);
    this._3c = ref.offset(4, 0x3cL).cast(IntRef::new);
    this._40 = ref.offset(4, 0x40L).cast(IntRef::new);

    this.deffTmdFlags_48 = ref.offset(4, 0x48L).cast(IntRef::new);
    this.deffTmdObjTable_4c = ref.offset(4, 0x4cL).cast(Pointer.deferred(4, TmdObjTable::new));
    this.deffSpriteFlags_50 = ref.offset(4, 0x50L).cast(IntRef::new);
    this.metrics_54 = ref.offset(4, 0x54L).cast(SpriteMetrics08::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
