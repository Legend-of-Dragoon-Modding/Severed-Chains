package legend.game.submap;

import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.ShortRef;
import legend.core.memory.types.UnboundedArrayRef;
import legend.core.memory.types.UnsignedByteRef;

public class SubmapMusic08 implements MemoryRef {
  private final Value ref;

  public final UnsignedByteRef submapCut_00;

  public final ShortRef musicIndex_02;
  public final Pointer<UnboundedArrayRef<ShortRef>> submapCuts_04;

  public SubmapMusic08(final Value ref) {
    this.ref = ref;

    this.submapCut_00 = ref.offset(1, 0x00L).cast(UnsignedByteRef::new);

    this.musicIndex_02 = ref.offset(2, 0x02L).cast(ShortRef::new);
    this.submapCuts_04 = ref.offset(4, 0x04L).cast(Pointer.deferred(2, UnboundedArrayRef.of(2, ShortRef::new)));
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
