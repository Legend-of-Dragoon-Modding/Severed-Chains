package legend.game.types;

import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.UnsignedIntRef;

public class MrgEntry implements MemoryRef {
  private final Value ref;

  public final UnsignedIntRef offset;
  public final UnsignedIntRef size;

  public MrgEntry(final Value ref) {
    this.ref = ref;

    this.offset = ref.offset(4, 0x0L).cast(UnsignedIntRef::new);
    this.size = ref.offset(4, 0x4L).cast(UnsignedIntRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
