package legend.game.types;

import legend.core.gte.GsCOORDINATE2;
import legend.core.gte.GsDOBJ2;
import legend.core.gte.TmdObjTable;
import legend.core.gte.TmdWithId;
import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.UnboundedArrayRef;
import legend.core.memory.types.UnsignedIntRef;

public class SomethingStruct implements MemoryRef {
  private final Value ref;

  public final Pointer<UnboundedArrayRef<TmdObjTable>> objTableArrPtr_00;
  public final UnsignedIntRef verts_04;
  public final UnsignedIntRef normals_08;
  public final UnsignedIntRef count_0c;
  public final UnsignedIntRef primitives_10;
  /** 0xc bytes each */
  public final Pointer<UnboundedArrayRef<SomethingStruct2>> ptr_14;
  public final UnsignedIntRef ptr_18;
  public final Pointer<TmdWithId> tmdPtr_1c;
  public final Pointer<GsDOBJ2> dobj2Ptr_20;
  public final Pointer<GsCOORDINATE2> coord2Ptr_24;

  public SomethingStruct(final Value ref) {
    this.ref = ref;

    this.objTableArrPtr_00 = ref.offset(4, 0x00L).cast(Pointer.deferred(4, UnboundedArrayRef.of(0x1c, TmdObjTable::new)));
    this.verts_04 = ref.offset(4, 0x04L).cast(UnsignedIntRef::new);
    this.normals_08 = ref.offset(4, 0x08L).cast(UnsignedIntRef::new);
    this.count_0c = ref.offset(4, 0x0cL).cast(UnsignedIntRef::new);
    this.primitives_10 = ref.offset(4, 0x10L).cast(UnsignedIntRef::new);
    this.ptr_14 = ref.offset(4, 0x14L).cast(Pointer.deferred(4, UnboundedArrayRef.of(0xc, SomethingStruct2::new)));
    this.ptr_18 = ref.offset(4, 0x18L).cast(UnsignedIntRef::new);
    this.tmdPtr_1c = ref.offset(4, 0x1cL).cast(Pointer.deferred(4, TmdWithId::new));
    this.dobj2Ptr_20 = ref.offset(4, 0x20L).cast(Pointer.deferred(4, GsDOBJ2::new));
    this.coord2Ptr_24 = ref.offset(4, 0x24L).cast(Pointer.deferred(4, GsCOORDINATE2::new));
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
