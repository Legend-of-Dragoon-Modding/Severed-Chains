package legend.game.combat.effects;

import legend.core.memory.Value;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;

/** Center-line origin point of a lightning bolt segment */
public class LightningBoltEffectSegmentOrigin08 implements MemoryRef {
  private final Value ref;

  public final IntRef x_00;
  public final IntRef y_04;

  public LightningBoltEffectSegmentOrigin08(final Value ref) {
    this.ref = ref;

    this.x_00 = ref.offset(4, 0x00).cast(IntRef::new);
    this.y_04 = ref.offset(4, 0x04).cast(IntRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
