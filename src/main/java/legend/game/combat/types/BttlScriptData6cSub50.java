package legend.game.combat.types;

import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.ShortRef;
import legend.core.memory.types.UnsignedShortRef;

public class BttlScriptData6cSub50 extends BttlScriptData6cSubBase1 {
  public final UnsignedShortRef _00;
  public final UnsignedShortRef _02;
  public final ArrayRef<UnsignedShortRef> _04;
  public final ArrayRef<UnsignedShortRef> _0e;
  public final ArrayRef<UnsignedShortRef> _18;
  public final ArrayRef<UnsignedShortRef> _22;
  public final ArrayRef<UnsignedShortRef> _2c;

  public final Pointer<ArrayRef<BttlScriptData6cSub50Sub3c>> _38;
  public final IntRef bobjIndex_3c;
  public final ShortRef _40;
  public final ShortRef _42;
  public final ShortRef _44;

  public final ShortRef _48;
  public final ShortRef _4a;

  public BttlScriptData6cSub50(final Value ref) {
    super(ref);

    this._00 = ref.offset(2, 0x00L).cast(UnsignedShortRef::new);
    this._02 = ref.offset(2, 0x02L).cast(UnsignedShortRef::new);
    this._04 = ref.offset(2, 0x04L).cast(ArrayRef.of(UnsignedShortRef.class, 5, 2, UnsignedShortRef::new));
    this._0e = ref.offset(2, 0x0eL).cast(ArrayRef.of(UnsignedShortRef.class, 5, 2, UnsignedShortRef::new));
    this._18 = ref.offset(2, 0x18L).cast(ArrayRef.of(UnsignedShortRef.class, 5, 2, UnsignedShortRef::new));
    this._22 = ref.offset(2, 0x22L).cast(ArrayRef.of(UnsignedShortRef.class, 5, 2, UnsignedShortRef::new));
    this._2c = ref.offset(2, 0x2cL).cast(ArrayRef.of(UnsignedShortRef.class, 5, 2, UnsignedShortRef::new));

    this._38 = ref.offset(4, 0x38L).cast(Pointer.deferred(4, ArrayRef.of(BttlScriptData6cSub50Sub3c.class, 5, 0x3c, BttlScriptData6cSub50Sub3c::new)));
    this.bobjIndex_3c = ref.offset(4, 0x3cL).cast(IntRef::new);
    this._40 = ref.offset(2, 0x40L).cast(ShortRef::new);
    this._42 = ref.offset(2, 0x42L).cast(ShortRef::new);
    this._44 = ref.offset(2, 0x44L).cast(ShortRef::new);

    this._48 = ref.offset(2, 0x048L).cast(ShortRef::new);
    this._4a = ref.offset(2, 0x04aL).cast(ShortRef::new);
  }
}
