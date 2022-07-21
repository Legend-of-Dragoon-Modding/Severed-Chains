package legend.game.combat.types;

import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;

public class DeffPart implements MemoryRef {
  private final Value ref;

  public DeffPart(final Value ref) {
    this.ref = ref;
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
