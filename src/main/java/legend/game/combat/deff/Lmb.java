package legend.game.combat.deff;

import legend.core.memory.Value;
import legend.core.memory.types.IntRef;

public abstract class Lmb extends Anim {
  public static final int MAGIC = 0x42_4d4c;

  public final IntRef count_04;

  public Lmb(final Value ref) {
    super(ref);

    this.count_04 = ref.offset(4, 0x04L).cast(IntRef::new);
  }
}
