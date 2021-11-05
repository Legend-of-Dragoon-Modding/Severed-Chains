package legend.game.types;

import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;

public class SshdFile implements MemoryRef {
  private final Value ref;

  public SshdFile(final Value ref) {
    this.ref = ref;
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
