package legend.game.submap;

import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.UnsignedShortRef;

public class Struct14_2 implements MemoryRef {
  private final Value ref;

  public final UnsignedShortRef _04;
  public final UnsignedShortRef _06;

  public Struct14_2(final Value ref) {
    this.ref = ref;

    this._04 = ref.offset(2, 0x04L).cast(UnsignedShortRef::new);
    this._06 = ref.offset(2, 0x06L).cast(UnsignedShortRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
