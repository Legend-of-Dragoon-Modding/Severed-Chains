package legend.game.types;

import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.UnsignedByteRef;
import legend.core.memory.types.UnsignedShortRef;

public class UiPart implements MemoryRef {
  private final Value ref;

  public final UnsignedShortRef metricsIndicesIndex_00;
  public final UnsignedByteRef _02;

  public UiPart(final Value ref) {
    this.ref = ref;

    this.metricsIndicesIndex_00 = ref.offset(2, 0x00L).cast(UnsignedShortRef::new);
    this._02 = ref.offset(1, 0x02L).cast(UnsignedByteRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
