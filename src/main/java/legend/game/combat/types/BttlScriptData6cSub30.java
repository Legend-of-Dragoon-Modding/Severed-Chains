package legend.game.combat.types;

import legend.core.gte.VECTOR;
import legend.core.memory.Value;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.UnboundedArrayRef;
import legend.core.memory.types.UnsignedIntRef;
import legend.core.memory.types.UnsignedShortRef;

public class BttlScriptData6cSub30 extends BttlScriptData6cSubBase1 {
  public final UnsignedIntRef _08;
  public final UnsignedIntRef _0c;
  public final UnsignedIntRef _10;
  public final UnsignedIntRef _14;
  public final Pointer<UnboundedArrayRef<VECTOR>> _18;

  public final UnsignedIntRef _24;

  public final UnsignedShortRef _2c;

  public BttlScriptData6cSub30(final Value ref) {
    super(ref);

    this._08 = ref.offset(4, 0x08L).cast(UnsignedIntRef::new);
    this._0c = ref.offset(4, 0x0cL).cast(UnsignedIntRef::new);
    this._10 = ref.offset(4, 0x10L).cast(UnsignedIntRef::new);
    this._14 = ref.offset(4, 0x14L).cast(UnsignedIntRef::new);
    this._18 = ref.offset(4, 0x18L).cast(Pointer.deferred(4, UnboundedArrayRef.of(0x10, VECTOR::new)));

    this._24 = ref.offset(4, 0x24L).cast(UnsignedIntRef::new);

    this._2c = ref.offset(2, 0x2cL).cast(UnsignedShortRef::new);
  }
}
