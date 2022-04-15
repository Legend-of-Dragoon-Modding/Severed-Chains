package legend.game.types;

import legend.core.gte.COLOUR;
import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;

public class WMapRender28 implements MemoryRef {
  private final Value ref;

  public final COLOUR _04;

  public WMapRender28(final Value ref) {
    this.ref = ref;

    this._04 = ref.offset(4, 0x04L).cast(COLOUR::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
