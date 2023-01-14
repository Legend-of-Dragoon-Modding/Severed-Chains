package legend.game.combat.types;

import legend.core.memory.Value;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.UnsignedIntRef;

public class BttlScriptData6cSub10_2 extends BttlScriptData6cSubBase1 {
  public final IntRef count_00;
  public final SpriteMetrics08 metrics_04;
  /** TODO */
  public final UnsignedIntRef ptr_0c;

  public BttlScriptData6cSub10_2(final Value ref) {
    super(ref);

    this.count_00 = ref.offset(4, 0x00L).cast(IntRef::new);
    this.metrics_04 = ref.offset(4, 0x04L).cast(SpriteMetrics08::new);
    this.ptr_0c = ref.offset(4, 0x0cL).cast(UnsignedIntRef::new);
  }
}
