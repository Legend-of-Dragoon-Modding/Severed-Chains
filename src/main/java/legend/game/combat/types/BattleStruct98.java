package legend.game.combat.types;

import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;

public class BattleStruct98 implements MemoryRef {
  private final Value ref;

  public BattleStruct98(final Value ref) {
    this.ref = ref;
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
