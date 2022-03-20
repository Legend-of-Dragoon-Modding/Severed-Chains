package legend.game.types;

import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;

public class WMapRender28 implements MemoryRef {
  private final Value ref;

  public WMapRender28(final Value ref) {
    this.ref = ref;
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
