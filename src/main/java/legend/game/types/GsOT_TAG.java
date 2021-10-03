package legend.game.types;

import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.UnsignedByteRef;
import legend.core.memory.types.UnsignedInt24Ref;
import legend.core.memory.types.UnsignedIntRef;

public class GsOT_TAG implements MemoryRef {
  private final Value ref;

  private final UnsignedIntRef tag;

  public final UnsignedInt24Ref p;
  public final UnsignedByteRef num;

  public GsOT_TAG(final Value ref) {
    this.ref = ref;

    this.tag = ref.offset(4, 0x0L).cast(UnsignedIntRef::new);

    this.p = ref.offset(3, 0x0L).cast(UnsignedInt24Ref::new);
    this.num = ref.offset(1, 0x3L).cast(UnsignedByteRef::new);
  }

  public long get() {
    return this.tag.get();
  }

  public void set(final long val) {
    this.tag.set(val);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
