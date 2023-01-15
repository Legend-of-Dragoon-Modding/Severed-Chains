package legend.game.combat.types;

import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;

public abstract class BttlScriptData6cSubBase1 implements MemoryRef {
  private final Value ref;

  public BttlScriptData6cSubBase1(final Value ref) {
    this.ref = ref;
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
