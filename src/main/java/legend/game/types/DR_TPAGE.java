package legend.game.types;

import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.UnsignedIntRef;

public class DR_TPAGE implements MemoryRef {
  private final Value ref;

  public final UnsignedIntRef tag;
  public final ArrayRef<UnsignedIntRef> code;

  public DR_TPAGE(final Value ref) {
    this.ref = ref;

    this.tag = ref.offset(4, 0x0L).cast(UnsignedIntRef::new);
    this.code = ref.offset(4, 0x4L).cast(ArrayRef.of(UnsignedIntRef.class, 1, 4, UnsignedIntRef::new));
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
