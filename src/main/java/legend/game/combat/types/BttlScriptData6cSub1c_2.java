package legend.game.combat.types;

import legend.core.memory.Value;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.UnboundedArrayRef;
import legend.core.memory.types.UnsignedByteRef;
import legend.core.memory.types.UnsignedIntRef;
import legend.core.memory.types.UnsignedShortRef;

public class BttlScriptData6cSub1c_2 implements MemoryRef {
  private final Value ref;

  public final UnsignedShortRef count_00;

  public final IntRef _04;
  public final IntRef _08;
  public final UnsignedByteRef count_0c;

  public final UnsignedIntRef _10;
  public final IntRef z_14;
  public final Pointer<UnboundedArrayRef<Pointer<UnboundedArrayRef<BttlScriptData6cSub1c_2Sub1e>>>> _18;

  public BttlScriptData6cSub1c_2(final Value ref) {
    this.ref = ref;

    this.count_00 = ref.offset(2, 0x00L).cast(UnsignedShortRef::new);

    this._04 = ref.offset(4, 0x04L).cast(IntRef::new);
    this._08 = ref.offset(4, 0x08L).cast(IntRef::new);
    this.count_0c = ref.offset(1, 0x0cL).cast(UnsignedByteRef::new);

    this._10 = ref.offset(4, 0x10L).cast(UnsignedIntRef::new);
    this.z_14 = ref.offset(4, 0x14L).cast(IntRef::new);
    this._18 = ref.offset(4, 0x18L).cast(Pointer.deferred(4, UnboundedArrayRef.of(4, Pointer.deferred(2, UnboundedArrayRef.of(0x1e, BttlScriptData6cSub1c_2Sub1e::new)))));
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
