package legend.game.combat.types;

import legend.core.gte.SVECTOR;
import legend.core.memory.Value;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.ShortRef;
import legend.core.memory.types.UnboundedArrayRef;
import legend.core.memory.types.UnsignedByteRef;

public class BttlScriptData6cSub38Sub14 implements MemoryRef {
  private final Value ref;

  public final UnsignedByteRef _00;

  public final ShortRef _02;
  public final SVECTOR _04;
  public final IntRef sz3_0c;
  public final Pointer<UnboundedArrayRef<BttlScriptData6cSub38Sub14Sub30>> ptr_10;

  public BttlScriptData6cSub38Sub14(final Value ref) {
    this.ref = ref;

    this._00 = ref.offset(1, 0x00L).cast(UnsignedByteRef::new);

    this._02 = ref.offset(2, 0x02L).cast(ShortRef::new);
    this._04 = ref.offset(2, 0x04L).cast(SVECTOR::new);
    this.sz3_0c = ref.offset(4, 0x0cL).cast(IntRef::new);
    this.ptr_10 = ref.offset(4, 0x10L).cast(Pointer.deferred(4, UnboundedArrayRef.of(0x30, BttlScriptData6cSub38Sub14Sub30::new)));
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
