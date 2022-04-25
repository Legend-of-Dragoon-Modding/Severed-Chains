package legend.game.types;

import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.UnsignedShortRef;

public class Drgn0_6666Struct implements MemoryRef {
  private final Value ref;

  public UnsignedShortRef _06;

  public UnsignedShortRef _0a;

  public Drgn0_6666Struct(final Value ref) {
    this.ref = ref;

    this._06 = ref.offset(2, 0x06L).cast(UnsignedShortRef::new);

    this._0a = ref.offset(2, 0x0aL).cast(UnsignedShortRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
