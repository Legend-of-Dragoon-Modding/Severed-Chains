package legend.game.types;

import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.UnsignedIntRef;

public class UnknownStruct2 implements MemoryRef {
  private final Value ref;

  public final UnsignedIntRef _00;
  public final UnsignedIntRef _04;

  public UnknownStruct2(final Value ref) {
    this.ref = ref;

    this._00 = ref.offset(4, 0x0L).cast(UnsignedIntRef::new);
    this._04 = ref.offset(4, 0x4L).cast(UnsignedIntRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
