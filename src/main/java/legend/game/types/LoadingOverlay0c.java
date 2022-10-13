package legend.game.types;

import legend.core.memory.Value;
import legend.core.memory.types.ConsumerRef;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.Pointer;

public class LoadingOverlay0c implements MemoryRef {
  private final Value ref;

  public final IntRef overlayIndex_00;
  public final Pointer<ConsumerRef<Integer>> loadedCallback_04;
  public final IntRef callbackParam_08;

  public LoadingOverlay0c(final Value ref) {
    this.ref = ref;

    this.overlayIndex_00 = ref.offset(4, 0x00L).cast(IntRef::new);
    this.loadedCallback_04 = ref.offset(4, 0x04L).cast(Pointer.deferred(4, ConsumerRef::new));
    this.callbackParam_08 = ref.offset(4, 0x08L).cast(IntRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
