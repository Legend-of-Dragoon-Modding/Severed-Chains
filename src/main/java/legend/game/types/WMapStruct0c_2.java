package legend.game.types;

import legend.core.gte.DVECTOR;
import legend.core.memory.Value;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;

public class WMapStruct0c_2 implements MemoryRef {
  private final Value ref;

  public final IntRef z_00;
  public final IntRef _04;
  public final DVECTOR xy_08;

  public WMapStruct0c_2(final Value ref) {
    this.ref = ref;

    this.z_00 = ref.offset(4, 0x00L).cast(IntRef::new);
    this._04 = ref.offset(4, 0x04L).cast(IntRef::new);
    this.xy_08 = ref.offset(4, 0x08L).cast(DVECTOR::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
