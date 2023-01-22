package legend.game.combat.types;

import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.UnsignedIntRef;

public class AttackHitFlashEffect0c implements BttlScriptData6cSubBase1, MemoryRef {
  private final Value ref;

  public final UnsignedIntRef _00;
  public final SpriteMetrics08 metrics_04;

  public AttackHitFlashEffect0c(final Value ref) {
    this.ref = ref;

    this._00 = ref.offset(4, 0x00L).cast(UnsignedIntRef::new);
    this.metrics_04 = ref.offset(4, 0x04L).cast(SpriteMetrics08::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
