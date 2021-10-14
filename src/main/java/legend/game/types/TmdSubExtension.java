package legend.game.types;

import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.UnsignedShortRef;

public class TmdSubExtension implements MemoryRef {
  private final Value ref;

  public final UnsignedShortRef us_02;

  public TmdSubExtension(final Value ref) {
    this.ref = ref;

    this.us_02 = ref.offset(2, 0x02L).cast(UnsignedShortRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
