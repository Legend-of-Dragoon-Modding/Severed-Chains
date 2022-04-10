package legend.game.types;

import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.UnsignedIntRef;

public class Struct84 implements MemoryRef {
  private final Value ref;

  public final UnsignedIntRef _00;

  public final UnsignedIntRef ptr_58;

  public Struct84(final Value ref) {
    this.ref = ref;

    this._00 = ref.offset(4, 0x00L).cast(UnsignedIntRef::new);

    this.ptr_58 = ref.offset(4, 0x58L).cast(UnsignedIntRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
