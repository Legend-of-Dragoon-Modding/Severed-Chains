package legend.core.gte;

import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.UnsignedIntRef;

/** 0x8 bytes long */
public class TmdHeader implements MemoryRef {
  private final Value ref;

  public final UnsignedIntRef flags;
  public final UnsignedIntRef nobj;

  public TmdHeader(final Value ref) {
    this.ref = ref;

    this.flags = ref.offset(4, 0x0L).cast(UnsignedIntRef::new);
    this.nobj = ref.offset(4, 0x4L).cast(UnsignedIntRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
