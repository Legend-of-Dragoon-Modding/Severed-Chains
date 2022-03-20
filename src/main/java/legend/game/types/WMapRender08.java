package legend.game.types;

import legend.core.gte.SVECTOR;
import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;

public class WMapRender08 implements MemoryRef {
  private final Value ref;

  public final SVECTOR svec_00;

  public WMapRender08(final Value ref) {
    this.ref = ref;

    this.svec_00 = ref.offset(2, 0x00L).cast(SVECTOR::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
