package legend.game.combat.types;

import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.ShortRef;
import legend.core.memory.types.UnsignedShortRef;

public class BttlScriptData6cSub50 implements BttlScriptData6cSubBase1, MemoryRef {
  private final Value ref;

  public final UnsignedShortRef _00;
  public final UnsignedShortRef _02;
  public final ArrayRef<UnsignedShortRef> u_04;
  public final ArrayRef<UnsignedShortRef> v_0e;
  public final ArrayRef<UnsignedShortRef> w_18;
  public final ArrayRef<UnsignedShortRef> h_22;
  public final ArrayRef<UnsignedShortRef> clut_2c;

  public final Pointer<ArrayRef<BttlScriptData6cSub50Sub3c>> _38;
  public final IntRef bobjIndex_3c;
  public final ShortRef _40;
  public final ShortRef _42;
  public final ShortRef _44;

  public final ShortRef _48;
  public final ShortRef _4a;

  public BttlScriptData6cSub50(final Value ref) {
    this.ref = ref;

    this._00 = ref.offset(2, 0x00L).cast(UnsignedShortRef::new);
    this._02 = ref.offset(2, 0x02L).cast(UnsignedShortRef::new);
    this.u_04 = ref.offset(2, 0x04L).cast(ArrayRef.of(UnsignedShortRef.class, 5, 2, UnsignedShortRef::new));
    this.v_0e = ref.offset(2, 0x0eL).cast(ArrayRef.of(UnsignedShortRef.class, 5, 2, UnsignedShortRef::new));
    this.w_18 = ref.offset(2, 0x18L).cast(ArrayRef.of(UnsignedShortRef.class, 5, 2, UnsignedShortRef::new));
    this.h_22 = ref.offset(2, 0x22L).cast(ArrayRef.of(UnsignedShortRef.class, 5, 2, UnsignedShortRef::new));
    this.clut_2c = ref.offset(2, 0x2cL).cast(ArrayRef.of(UnsignedShortRef.class, 5, 2, UnsignedShortRef::new));

    this._38 = ref.offset(4, 0x38L).cast(Pointer.deferred(4, ArrayRef.of(BttlScriptData6cSub50Sub3c.class, 5, 0x3c, BttlScriptData6cSub50Sub3c::new)));
    this.bobjIndex_3c = ref.offset(4, 0x3cL).cast(IntRef::new);
    this._40 = ref.offset(2, 0x40L).cast(ShortRef::new);
    this._42 = ref.offset(2, 0x42L).cast(ShortRef::new);
    this._44 = ref.offset(2, 0x44L).cast(ShortRef::new);

    this._48 = ref.offset(2, 0x048L).cast(ShortRef::new);
    this._4a = ref.offset(2, 0x04aL).cast(ShortRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
