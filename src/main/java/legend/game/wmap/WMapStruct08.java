package legend.game.wmap;

import legend.core.memory.Value;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;

public class WMapStruct08 implements MemoryRef {
  private final Value ref;

  public final IntRef locationIndex_00;
  public final IntRef _04;

  public WMapStruct08(final Value ref) {
    this.ref = ref;

    this.locationIndex_00 = ref.offset(4, 0x00L).cast(IntRef::new);
    this._04 = ref.offset(4, 0x04L).cast(IntRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
