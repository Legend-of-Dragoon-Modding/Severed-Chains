package legend.game.combat.types;

import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.UnsignedByteRef;
import legend.core.memory.types.UnsignedIntRef;
import legend.core.memory.types.UnsignedShortRef;

public class BttlScriptData6cSub3c extends BttlScriptData6cSubBase1 {
  public final IntRef _00;
  public final UnsignedIntRef _04;
  public final UnsignedShortRef _08;

  public final UnsignedByteRef _0e;

  public final UnsignedIntRef _30;
  public final Pointer<ArrayRef<BttlScriptData6cSub3cSub2c>> _34;
  public final UnsignedIntRef _38;

  public BttlScriptData6cSub3c(final Value ref) {
    super(ref);

    this._00 = ref.offset(4, 0x00L).cast(IntRef::new);
    this._04 = ref.offset(4, 0x04L).cast(UnsignedIntRef::new);
    this._08 = ref.offset(2, 0x08L).cast(UnsignedShortRef::new);

    this._0e = ref.offset(1, 0x0eL).cast(UnsignedByteRef::new);

    this._30 = ref.offset(4, 0x30L).cast(UnsignedIntRef::new);
    this._34 = ref.offset(4, 0x34L).cast(Pointer.deferred(4, ArrayRef.of(BttlScriptData6cSub3cSub2c.class, 65, 0x2c, BttlScriptData6cSub3cSub2c::new)));
    this._38 = ref.offset(4, 0x38L).cast(UnsignedIntRef::new);
  }
}
