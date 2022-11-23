package legend.game.types;

import legend.core.gte.VECTOR;
import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.ShortRef;
import legend.core.memory.types.UnsignedByteRef;
import legend.core.memory.types.UnsignedIntRef;

public class CoolonWarpDestination20 implements MemoryRef {
  private final Value ref;

  public final VECTOR vec_00;
  public final UnsignedIntRef _10;
  public final UnsignedByteRef _14;

  public final ShortRef x_18;
  public final ShortRef y_1a;
  public final Pointer<LodString> placeName_1c;

  public CoolonWarpDestination20(final Value ref) {
    this.ref = ref;

    this.vec_00 = ref.offset(4, 0x00L).cast(VECTOR::new);
    this._10 = ref.offset(4, 0x10L).cast(UnsignedIntRef::new);
    this._14 = ref.offset(1, 0x14L).cast(UnsignedByteRef::new);

    this.x_18 = ref.offset(2, 0x18L).cast(ShortRef::new);
    this.y_1a = ref.offset(2, 0x1aL).cast(ShortRef::new);

    this.placeName_1c = ref.offset(4, 0x1cL).cast(Pointer.deferred(4, LodString::new));
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
