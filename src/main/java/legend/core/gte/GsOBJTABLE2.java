package legend.core.gte;

import legend.core.gte.GsDOBJ2;
import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.UnboundedArrayRef;
import legend.core.memory.types.UnsignedIntRef;

public class GsOBJTABLE2 implements MemoryRef {
  private final Value ref;

  public final Pointer<UnboundedArrayRef<GsDOBJ2>> top;
  public final UnsignedIntRef nobj;
  public final UnsignedIntRef maxobj;

  public GsOBJTABLE2(final Value ref) {
    this.ref = ref;

    this.top = ref.offset(4, 0x0L).cast(Pointer.deferred(4, UnboundedArrayRef.of(0x10, GsDOBJ2::new, this::getLength)));
    this.nobj = ref.offset(4, 0x4L).cast(UnsignedIntRef::new);
    this.maxobj = ref.offset(4, 0x8L).cast(UnsignedIntRef::new);
  }

  private int getLength() {
    return (int)this.nobj.get();
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
