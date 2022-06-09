package legend.game.combat.types;

import legend.core.gte.VECTOR;
import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.ShortRef;
import legend.core.memory.types.UnsignedByteRef;
import legend.core.memory.types.UnsignedIntRef;
import legend.core.memory.types.UnsignedShortRef;
import legend.game.types.BigStruct;

public class BttlScriptData6cSub3c extends BttlScriptData6cSubBase1 {
  public final IntRef _00;
  public final UnsignedIntRef _04;
  public final ShortRef _08;
  public final UnsignedShortRef _0a;
  public final UnsignedShortRef _0c;
  public final UnsignedByteRef _0e;
  public final VECTOR _10;
  public final VECTOR _20;
  public final Pointer<BigStruct> _30;
  public final Pointer<ArrayRef<BttlScriptData6cSub3cSub2c>> _34;
  public final Pointer<BttlScriptData6cSub3cSub2c> _38;

  public BttlScriptData6cSub3c(final Value ref) {
    super(ref);

    this._00 = ref.offset(4, 0x00L).cast(IntRef::new);
    this._04 = ref.offset(4, 0x04L).cast(UnsignedIntRef::new);
    this._08 = ref.offset(2, 0x08L).cast(ShortRef::new);
    this._0a = ref.offset(2, 0x0aL).cast(UnsignedShortRef::new);
    this._0c = ref.offset(2, 0x0cL).cast(UnsignedShortRef::new);
    this._0e = ref.offset(1, 0x0eL).cast(UnsignedByteRef::new);
    this._10 = ref.offset(4, 0x10L).cast(VECTOR::new);
    this._20 = ref.offset(4, 0x20L).cast(VECTOR::new);
    this._30 = ref.offset(4, 0x30L).cast(Pointer.deferred(4, BigStruct::new));
    this._34 = ref.offset(4, 0x34L).cast(Pointer.deferred(4, ArrayRef.of(BttlScriptData6cSub3cSub2c.class, 65, 0x2c, BttlScriptData6cSub3cSub2c::new)));
    this._38 = ref.offset(4, 0x38L).cast(Pointer.deferred(4, BttlScriptData6cSub3cSub2c::new));
  }
}
