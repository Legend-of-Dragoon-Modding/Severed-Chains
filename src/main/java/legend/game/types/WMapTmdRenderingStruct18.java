package legend.game.types;

import legend.core.gte.GsCOORDINATE2;
import legend.core.gte.GsDOBJ2;
import legend.core.gte.SVECTOR;
import legend.core.gte.TmdWithId;
import legend.core.memory.Value;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.UnboundedArrayRef;
import legend.core.memory.types.UnsignedIntRef;

public class WMapTmdRenderingStruct18 implements MemoryRef {
  private final Value ref;

  public final Pointer<UnboundedArrayRef<GsDOBJ2>> dobj2s_00;
  public final Pointer<UnboundedArrayRef<GsCOORDINATE2>> coord2s_04;
  public final Pointer<UnboundedArrayRef<SVECTOR>> rotations_08;
  public final IntRef count_0c;
  public final Pointer<UnboundedArrayRef<UnsignedIntRef>> _10;
  public final Pointer<TmdWithId> tmd_14;

  public WMapTmdRenderingStruct18(final Value ref) {
    this.ref = ref;

    this.dobj2s_00 = ref.offset(4, 0x00L).cast(Pointer.deferred(4, UnboundedArrayRef.of(4, GsDOBJ2::new)));
    this.coord2s_04 = ref.offset(4, 0x04L).cast(Pointer.deferred(4, UnboundedArrayRef.of(4, GsCOORDINATE2::new)));
    this.rotations_08 = ref.offset(4, 0x08L).cast(Pointer.deferred(4, UnboundedArrayRef.of(8, SVECTOR::new)));
    this.count_0c = ref.offset(4, 0x0cL).cast(IntRef::new);
    this._10 = ref.offset(4, 0x10L).cast(Pointer.deferred(4, UnboundedArrayRef.of(4, UnsignedIntRef::new)));
    this.tmd_14 = ref.offset(4, 0x14L).cast(Pointer.deferred(4, TmdWithId::new));
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
