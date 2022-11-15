package legend.game.combat.types;

import legend.core.gte.TmdObjTable;
import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.UnsignedIntRef;

public class BttlScriptData6cSub5c extends BttlScriptData6cSubBase1 {
  public final UnsignedIntRef _00;
  public final UnsignedIntRef _04;
  public final IntRef _08;
  /** TODO ptr */
  public final UnsignedIntRef ptr_0c;
  /** TODO ptr */
  public final UnsignedIntRef ptr_10;
  public final ArrayRef<IntRef> _14;
  public final IntRef _34;
  public final IntRef _38;
  public final IntRef _3c;
  public final IntRef _40;

  public final IntRef _48;
  public final Pointer<TmdObjTable> _4c;
  public final IntRef _50;
  public final SpriteMetrics08 metrics_54;

  public BttlScriptData6cSub5c(final Value ref) {
    super(ref);

    this._00 = ref.offset(4, 0x00L).cast(UnsignedIntRef::new);
    this._04 = ref.offset(4, 0x04L).cast(UnsignedIntRef::new);
    this._08 = ref.offset(4, 0x08L).cast(IntRef::new);
    this.ptr_0c = ref.offset(4, 0x0cL).cast(UnsignedIntRef::new);
    this.ptr_10 = ref.offset(4, 0x10L).cast(UnsignedIntRef::new);
    this._14 = ref.offset(4, 0x14L).cast(ArrayRef.of(IntRef.class, 8, 4, IntRef::new));
    this._34 = ref.offset(4, 0x34L).cast(IntRef::new);
    this._38 = ref.offset(4, 0x38L).cast(IntRef::new);
    this._3c = ref.offset(4, 0x3cL).cast(IntRef::new);
    this._40 = ref.offset(4, 0x40L).cast(IntRef::new);

    this._48 = ref.offset(4, 0x48L).cast(IntRef::new);
    this._4c = ref.offset(4, 0x4cL).cast(Pointer.deferred(4, TmdObjTable::new));
    this._50 = ref.offset(4, 0x50L).cast(IntRef::new);
    this.metrics_54 = ref.offset(4, 0x54L).cast(SpriteMetrics08::new);
  }
}
