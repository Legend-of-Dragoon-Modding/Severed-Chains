package legend.core.gte;

import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.UnsignedIntRef;

public class TmdWithId implements MemoryRef {
  private final Value ref;

  public final UnsignedIntRef id;
  public final Tmd tmd;

  public TmdWithId(final Value ref) {
    this.ref = ref;

    this.id = ref.offset(4, 0x0L).cast(UnsignedIntRef::new);
    this.tmd = ref.offset(4, 0x4L).cast(Tmd::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
