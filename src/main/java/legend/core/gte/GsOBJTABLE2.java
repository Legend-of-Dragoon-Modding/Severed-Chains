package legend.core.gte;

import legend.core.memory.Value;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.UnboundedArrayRef;

public class GsOBJTABLE2 implements MemoryRef {
  private final Value ref;

  public final Pointer<UnboundedArrayRef<GsDOBJ2>> top;
  public final IntRef nobj;
  public final IntRef maxobj;

  public GsOBJTABLE2(final Value ref) {
    this.ref = ref;

    this.nobj = ref.offset(4, 0x4L).cast(IntRef::new);
    this.maxobj = ref.offset(4, 0x8L).cast(IntRef::new);

    this.top = ref.offset(4, 0x0L).cast(Pointer.deferred(4, UnboundedArrayRef.of(0x10, GsDOBJ2::new, this.nobj::get)));
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
