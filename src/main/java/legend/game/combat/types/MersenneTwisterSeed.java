package legend.game.combat.types;

import legend.core.memory.Value;
import legend.core.memory.types.UnsignedIntRef;

public class MersenneTwisterSeed extends UnsignedIntRef {
  public MersenneTwisterSeed(final Value ref) {
    super(ref);
  }

  public MersenneTwisterSeed advance() {
    return (MersenneTwisterSeed)this.incr().mulOverflow(0x10dcdL);
  }
}
