package legend.game.types;

import legend.core.memory.Value;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;

public class MoonMusic08 implements MemoryRef {
  private final Value ref;

  public final IntRef submapCut_00;
  public final IntRef musicIndex_04;

  public MoonMusic08(final Value ref) {
    this.ref = ref;

    this.submapCut_00 = ref.offset(4, 0x00L).cast(IntRef::new);
    this.musicIndex_04 = ref.offset(4, 0x04L).cast(IntRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
