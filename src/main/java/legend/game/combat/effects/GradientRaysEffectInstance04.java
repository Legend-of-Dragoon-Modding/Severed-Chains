package legend.game.combat.effects;

import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.ShortRef;

public class GradientRaysEffectInstance04 implements MemoryRef {
  private final Value ref;

  public final ShortRef _00;
  public final ShortRef _02;

  public GradientRaysEffectInstance04(final Value ref) {
    this.ref = ref;

    this._00 = ref.offset(2, 0x00).cast(ShortRef::new);
    this._02 = ref.offset(2, 0x02).cast(ShortRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
