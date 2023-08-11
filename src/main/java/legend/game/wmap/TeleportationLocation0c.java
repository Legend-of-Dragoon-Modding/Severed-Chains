package legend.game.wmap;

import legend.core.gte.SVECTOR;
import legend.core.memory.Value;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;

public class TeleportationLocation0c implements MemoryRef {
  private final Value ref;

  public final IntRef locationIndex_00;
  public final SVECTOR translation_04;

  public TeleportationLocation0c(final Value ref) {
    this.ref = ref;

    this.locationIndex_00 = ref.offset(4, 0x00L).cast(IntRef::new);
    this.translation_04 = ref.offset(2, 0x04L).cast(SVECTOR::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
