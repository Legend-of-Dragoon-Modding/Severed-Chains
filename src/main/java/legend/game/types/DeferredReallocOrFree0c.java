package legend.game.types;

import legend.core.memory.Value;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.UnsignedIntRef;
import legend.core.memory.types.UnsignedShortRef;

public class DeferredReallocOrFree0c implements MemoryRef {
  private final Value ref;

  public final UnsignedIntRef ptr_00;
  public final IntRef size_04;

  public final UnsignedShortRef frames_0a;

  public DeferredReallocOrFree0c(final Value ref) {
    this.ref = ref;

    this.ptr_00 = ref.offset(4, 0x00L).cast(UnsignedIntRef::new);
    this.size_04 = ref.offset(4, 0x04L).cast(IntRef::new);

    this.frames_0a = ref.offset(2, 0x0aL).cast(UnsignedShortRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
