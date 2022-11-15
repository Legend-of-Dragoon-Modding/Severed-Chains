package legend.game.types;

import legend.core.gte.GsCOORD2PARAM;
import legend.core.gte.GsCOORDINATE2;
import legend.core.gte.GsDOBJ2;
import legend.core.gte.GsOBJTABLE2;
import legend.core.gte.Tmd;
import legend.core.gte.VECTOR;
import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.ByteRef;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.ShortRef;
import legend.core.memory.types.UnboundedArrayRef;
import legend.core.memory.types.UnsignedByteRef;
import legend.core.memory.types.UnsignedIntRef;
import legend.core.memory.types.UnsignedShortRef;

public class Model124 implements MemoryRef {
  private final Value ref;

  public final Pointer<UnboundedArrayRef<GsDOBJ2>> dobj2ArrPtr_00;
  public final Pointer<UnboundedArrayRef<GsCOORDINATE2>> coord2ArrPtr_04;
  public final Pointer<UnboundedArrayRef<GsCOORD2PARAM>> coord2ParamArrPtr_08;
  public final GsOBJTABLE2 ObjTable_0c; // GsOBJTABLE2 is 0xc bytes long... overlaps coord2's flag...?
  // 0x50 bytes
  public final GsCOORDINATE2 coord2_14;
  public final GsCOORD2PARAM coord2Param_64;
  public final Pointer<Tmd> tmd_8c;
  public final Pointer<UnboundedArrayRef<ModelPartTransforms>> partTransforms_90;
  /** One entry for each TMD object (tmdNobj_ca) */
  public final Pointer<UnboundedArrayRef<ModelPartTransforms>> partTransforms_94;
  public final UnsignedShortRef animCount_98;
  public final ShortRef s_9a;
  public final UnsignedByteRef ub_9c;
  public final UnsignedByteRef ub_9d;
  public final ShortRef s_9e;
  public final ShortRef zOffset_a0;
  public final UnsignedByteRef ub_a2;
  public final UnsignedByteRef ub_a3;
  /** Pointer to an address on the linked list, 0x30 bytes long, contains data copied from {@link ExtendedTmd#ext_04} */
  public final Pointer<SmallerStruct> smallerStructPtr_a4;
  /** TODO Pointer to whatever is pointed to by {@link ExtendedTmd#ptr_08} */
  public final UnsignedIntRef ptr_a8;
  public final ArrayRef<UnsignedShortRef> usArr_ac;
  public final ArrayRef<UnsignedShortRef> usArr_ba;
  public final ShortRef count_c8;
  public final UnsignedShortRef tmdNobj_ca;
  public final ByteRef b_cc;
  public final ByteRef b_cd;

  /** 7 ints long */
  public final ArrayRef<UnsignedIntRef> aui_d0;
  /** 7 bytes long */
  public final ArrayRef<UnsignedByteRef> aub_ec;

  public final UnsignedIntRef ui_f4;
  public final UnsignedIntRef ui_f8;
  public final VECTOR scaleVector_fc;
  /** Pretty sure this doesn't include VRAM X/Y */
  public final IntRef tpage_108;
  public final VECTOR vector_10c;
  public final VECTOR vector_118;

  public Model124(final Value ref) {
    this.ref = ref;

    this.dobj2ArrPtr_00 = ref.offset(4, 0x00L).cast(Pointer.deferred(4, UnboundedArrayRef.of(0x10, GsDOBJ2::new)));
    this.coord2ArrPtr_04 = ref.offset(4, 0x04L).cast(Pointer.deferred(4, UnboundedArrayRef.of(0x50, GsCOORDINATE2::new)));
    this.coord2ParamArrPtr_08 = ref.offset(4, 0x08L).cast(Pointer.deferred(4, UnboundedArrayRef.of(0x28, GsCOORD2PARAM::new)));
    this.ObjTable_0c = ref.offset(4, 0x0cL).cast(GsOBJTABLE2::new);
    this.coord2_14 = ref.offset(4, 0x14L).cast(GsCOORDINATE2::new);
    this.coord2Param_64 = ref.offset(4, 0x64L).cast(GsCOORD2PARAM::new);
    this.tmd_8c = ref.offset(4, 0x8cL).cast(Pointer.deferred(4, Tmd::new));
    this.partTransforms_90 = ref.offset(4, 0x90L).cast(Pointer.deferred(4, UnboundedArrayRef.of(0xc, ModelPartTransforms::new)));
    this.partTransforms_94 = ref.offset(4, 0x94L).cast(Pointer.deferred(4, UnboundedArrayRef.of(0xc, ModelPartTransforms::new)));
    this.animCount_98 = ref.offset(2, 0x98L).cast(UnsignedShortRef::new);
    this.s_9a = ref.offset(2, 0x9aL).cast(ShortRef::new);
    this.ub_9c = ref.offset(1, 0x9cL).cast(UnsignedByteRef::new);
    this.ub_9d = ref.offset(1, 0x9dL).cast(UnsignedByteRef::new);
    this.s_9e = ref.offset(2, 0x9eL).cast(ShortRef::new);
    this.zOffset_a0 = ref.offset(2, 0xa0L).cast(ShortRef::new);
    this.ub_a2 = ref.offset(1, 0xa2L).cast(UnsignedByteRef::new);
    this.ub_a3 = ref.offset(1, 0xa3L).cast(UnsignedByteRef::new);
    this.smallerStructPtr_a4 = ref.offset(4, 0xa4L).cast(Pointer.deferred(4, SmallerStruct::new));
    this.ptr_a8 = ref.offset(4, 0xa8L).cast(UnsignedIntRef::new);
    this.usArr_ac = ref.offset(2, 0xacL).cast(ArrayRef.of(UnsignedShortRef.class, 7, 2, UnsignedShortRef::new));
    this.usArr_ba = ref.offset(2, 0xbaL).cast(ArrayRef.of(UnsignedShortRef.class, 7, 2, UnsignedShortRef::new));
    this.count_c8 = ref.offset(2, 0xc8L).cast(ShortRef::new);
    this.tmdNobj_ca = ref.offset(2, 0xcaL).cast(UnsignedShortRef::new);
    this.b_cc = ref.offset(1, 0xccL).cast(ByteRef::new);
    this.b_cd = ref.offset(1, 0xcdL).cast(ByteRef::new);

    this.aui_d0 = ref.offset(4, 0xd0L).cast(ArrayRef.of(UnsignedIntRef.class, 7, 4, UnsignedIntRef::new));
    this.aub_ec = ref.offset(1, 0xecL).cast(ArrayRef.of(UnsignedByteRef.class, 7, 1, UnsignedByteRef::new));
    this.ui_f4 = ref.offset(4, 0xf4L).cast(UnsignedIntRef::new);
    this.ui_f8 = ref.offset(4, 0xf8L).cast(UnsignedIntRef::new);
    this.scaleVector_fc = ref.offset(4, 0xfcL).cast(VECTOR::new);
    this.tpage_108 = ref.offset(4, 0x108L).cast(IntRef::new);
    this.vector_10c = ref.offset(4, 0x10cL).cast(VECTOR::new);
    this.vector_118 = ref.offset(4, 0x118L).cast(VECTOR::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
