package legend.game.wmap;

import legend.core.memory.Value;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;

public class TeleportationEndpoints08 implements MemoryRef {
  private final Value ref;

  public final IntRef originLocationIndex_00;
  public final IntRef targetLocationIndex_04;

  public TeleportationEndpoints08(final Value ref) {
    this.ref = ref;

    this.originLocationIndex_00 = ref.offset(4, 0x00L).cast(IntRef::new);
    this.targetLocationIndex_04 = ref.offset(4, 0x04L).cast(IntRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
