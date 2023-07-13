package legend.game.combat.effects;

import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.ShortRef;

public class GuardEffectMetrics04 implements MemoryRef {
  public final Value ref;

  public final ShortRef z_00;
  public final ShortRef y_02;

  public GuardEffectMetrics04(final Value ref) {
    this.ref = ref;

    this.z_00 = ref.offset(2, 0x00).cast(ShortRef::new);
    this.y_02 = ref.offset(2, 0x02).cast(ShortRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
