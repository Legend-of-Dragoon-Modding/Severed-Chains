package legend.game.types;

import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.ShortRef;
import legend.core.memory.types.UnsignedByteRef;

public class SubmapEncounterData_04 implements MemoryRef {
  private final Value ref;

  public final ShortRef scene_00;
  public final UnsignedByteRef rate_02;
  public final UnsignedByteRef stage_03;

  public SubmapEncounterData_04(final Value ref) {
    this.ref = ref;

    this.scene_00 = ref.offset(2, 0x00L).cast(ShortRef::new);
    this.rate_02 = ref.offset(1, 0x02L).cast(UnsignedByteRef::new);
    this.stage_03 = ref.offset(1, 0x03L).cast(UnsignedByteRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
