package legend.game.sound;

import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;

public class Sequence implements MemoryRef {
  private final Value ref;

  public Sequence(final Value ref) {
    this.ref = ref;
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
