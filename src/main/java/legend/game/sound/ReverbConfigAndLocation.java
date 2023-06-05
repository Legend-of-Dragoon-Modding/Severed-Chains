package legend.game.sound;

import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.UnsignedShortRef;
import legend.core.spu.ReverbConfig;

public class ReverbConfigAndLocation implements MemoryRef {
  private final Value ref;

  public final UnsignedShortRef address_00;
  public final ReverbConfig config_02;

  public ReverbConfigAndLocation(final Value ref) {
    this.ref = ref;

    this.address_00 = ref.offset(2, 0x00L).cast(UnsignedShortRef::new);
    this.config_02 = ref.offset(2, 0x02L).cast(ReverbConfig::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
