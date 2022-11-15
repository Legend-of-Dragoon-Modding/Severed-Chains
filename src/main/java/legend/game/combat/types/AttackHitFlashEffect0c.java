package legend.game.combat.types;

import legend.core.memory.Value;
import legend.core.memory.types.UnsignedIntRef;

public class AttackHitFlashEffect0c extends BttlScriptData6cSubBase1 {
  public final UnsignedIntRef _00;
  public final SpriteMetrics08 metrics_04;

  public AttackHitFlashEffect0c(final Value ref) {
    super(ref);

    this._00 = ref.offset(4, 0x00L).cast(UnsignedIntRef::new);
    this.metrics_04 = ref.offset(4, 0x04L).cast(SpriteMetrics08::new);
  }
}
