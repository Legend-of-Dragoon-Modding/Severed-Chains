package legend.game.types;

import legend.core.gte.GsCOORDINATE2;
import legend.core.gte.GsDOBJ2;
import legend.core.gte.TmdWithId;
import legend.core.memory.Value;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.UnboundedArrayRef;

/** 0x10 bytes long */
public class TmdRenderingStruct implements MemoryRef {
  private final Value ref;

  public final Pointer<UnboundedArrayRef<GsDOBJ2>> dobj2s_00;
  public final Pointer<UnboundedArrayRef<GsCOORDINATE2>> coord2s_04;
  public final IntRef count_08;
  public final Pointer<TmdWithId> tmd_0c;

  public TmdRenderingStruct(final Value ref) {
    this.ref = ref;

    this.dobj2s_00 = ref.offset(4, 0x0L).cast(Pointer.deferred(4, UnboundedArrayRef.of(4, GsDOBJ2::new)));
    this.coord2s_04 = ref.offset(4, 0x4L).cast(Pointer.deferred(4, UnboundedArrayRef.of(4, GsCOORDINATE2::new)));
    this.count_08 = ref.offset(4, 0x8L).cast(IntRef::new);
    this.tmd_0c = ref.offset(4, 0xcL).cast(Pointer.deferred(4, TmdWithId::new));
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
