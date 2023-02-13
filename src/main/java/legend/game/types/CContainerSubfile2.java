package legend.game.types;

import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.RelativePointer;
import legend.core.memory.types.ShortRef;
import legend.core.memory.types.UnboundedArrayRef;

public class CContainerSubfile2 implements MemoryRef {
  private final Value ref;

  /** Sometimes 7, sometimes 10 elements */
  public final ArrayRef<RelativePointer<UnboundedArrayRef<ShortRef>>> _00;

  public CContainerSubfile2(final Value ref) {
    this.ref = ref;

    this._00 = ref.offset(4, 0x00L).cast(ArrayRef.of(RelativePointer.classFor(UnboundedArrayRef.classFor(ShortRef.class)), 10, 4, RelativePointer.deferred(2, ref.getAddress(), UnboundedArrayRef.of(2, ShortRef::new))));
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
