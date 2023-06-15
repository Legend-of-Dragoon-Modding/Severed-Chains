package legend.game.wmap;

import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.ShortRef;
import legend.core.memory.types.UnsignedShortRef;

public class WMapStruct2c implements MemoryRef {
  private final Value ref;

  public final ShortRef packedFlag_00;

  public final ArrayRef<IntRef> _04;
  public final ShortRef x_24;
  public final ShortRef y_26;
  public final UnsignedShortRef placeIndex_28;

  public WMapStruct2c(final Value ref) {
    this.ref = ref;

    this.packedFlag_00 = ref.offset(2, 0x00L).cast(ShortRef::new);

    this._04 = ref.offset(4, 0x04L).cast(ArrayRef.of(IntRef.class, 8, 4, IntRef::new));
    this.x_24 = ref.offset(2, 0x24L).cast(ShortRef::new);
    this.y_26 = ref.offset(2, 0x26L).cast(ShortRef::new);
    this.placeIndex_28 = ref.offset(2, 0x28L).cast(UnsignedShortRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
