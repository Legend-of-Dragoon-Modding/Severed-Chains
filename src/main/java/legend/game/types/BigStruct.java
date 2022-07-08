package legend.game.types;

import legend.core.gte.GsCOORD2PARAM;
import legend.core.gte.GsCOORDINATE2;
import legend.core.gte.GsDOBJ2;
import legend.core.gte.GsOBJTABLE2;
import legend.core.gte.Tmd;
import legend.core.gte.VECTOR;
import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.BoolRef;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.ShortRef;
import legend.core.memory.types.UnboundedArrayRef;
import legend.core.memory.types.UnsignedByteRef;
import legend.core.memory.types.UnsignedIntRef;
import legend.core.memory.types.UnsignedShortRef;

/** Might be 0x210 bytes long */
public class BigStruct implements MemoryRef {
  private final Value ref;

  public final Pointer<UnboundedArrayRef<GsDOBJ2>> dobj2ArrPtr_00;
  public final Pointer<UnboundedArrayRef<GsCOORDINATE2>> coord2ArrPtr_04;
  public final Pointer<UnboundedArrayRef<GsCOORD2PARAM>> coord2ParamArrPtr_08;
  public final GsOBJTABLE2 ObjTable_0c; // GsOBJTABLE2 is 0xc bytes long... overlaps coord2's flag...?
  // 0x50 bytes
  public final GsCOORDINATE2 coord2_14;
  public final GsCOORD2PARAM coord2Param_64;
  public final Pointer<Tmd> tmd_8c;
  public final Pointer<UnboundedArrayRef<RotateTranslateStruct>> ptr_ui_90;
  /** One entry for each TMD object (tmdNobj_ca) */
  public final Pointer<UnboundedArrayRef<RotateTranslateStruct>> rotateTranslateArrPtr_94;
  public final UnsignedShortRef animCount_98;
  public final UnsignedShortRef us_9a;
  public final UnsignedByteRef ub_9c;
  public final UnsignedByteRef ub_9d;
  public final UnsignedShortRef us_9e;
  public final ShortRef us_a0;
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
  public final UnsignedByteRef ub_cc;
  public final UnsignedByteRef ub_cd;

  /** 7 ints long */
  public final ArrayRef<UnsignedIntRef> aui_d0;
  /** 7 bytes long */
  public final ArrayRef<UnsignedByteRef> aub_ec;

  public final UnsignedIntRef ui_f4;
  public final UnsignedIntRef ui_f8;
  public final VECTOR scaleVector_fc;
  public final UnsignedIntRef ui_108;
  public final VECTOR vector_10c;
  public final VECTOR vector_118;
  public final Pointer<MrgFile> mrg_124;
  public final UnsignedShortRef us_128;
  public final UnsignedShortRef us_12a;
  public final UnsignedShortRef us_12c;
  public final UnsignedShortRef mrgAnimGroup_12e;
  /** TODO possibly file index? */
  public final UnsignedShortRef scriptFileIndex_130;
  public final UnsignedShortRef mrgAnimGroupIndex_132;
  public final UnsignedShortRef us_134;

  public final VECTOR vec_138;
  public final UnsignedIntRef ui_144;
  public final VECTOR vec_148;
  public final VECTOR vec_154;
  public final VECTOR vec_160;
  public final IntRef ui_16c;
  public final UnsignedShortRef us_170;
  public final UnsignedShortRef us_172;

  public final UnsignedShortRef us_178;

  public final UnsignedShortRef us_17c;

  public final UnsignedShortRef us_180;

  public final UnsignedShortRef us_184;

  public final UnsignedIntRef ui_188;
  public final UnsignedIntRef ui_18c;
  public final UnsignedIntRef ui_190;
  public final UnsignedIntRef ui_194;
  public final UnsignedIntRef ui_198;
  public final UnsignedIntRef ui_19c;
  public final UnsignedIntRef ui_1a0;
  public final UnsignedIntRef ui_1a4;
  public final UnsignedIntRef ui_1a8;
  public final UnsignedIntRef ui_1ac;
  public final UnsignedIntRef ui_1b0;
  public final UnsignedIntRef ui_1b4;
  public final UnsignedIntRef ui_1b8;
  public final UnsignedIntRef ui_1bc;
  public final UnsignedIntRef ui_1c0;
  public final BoolRef flatLightingEnabled_1c4;
  public final UnsignedByteRef flatLightRed_1c5;
  public final UnsignedByteRef flatLightGreen_1c6;
  public final UnsignedByteRef flatLightBlue_1c7;
  public final BoolRef ambientColourEnabled_1c8;

  public final UnsignedShortRef ambientRed_1ca;
  public final UnsignedShortRef ambientGreen_1cc;
  public final UnsignedShortRef ambientBlue_1ce;
  public final BigSubStruct _1d0;

  public BigStruct(final Value ref) {
    this.ref = ref;

    this.dobj2ArrPtr_00 = ref.offset(4, 0x00L).cast(Pointer.deferred(4, UnboundedArrayRef.of(0x10, GsDOBJ2::new)));
    this.coord2ArrPtr_04 = ref.offset(4, 0x04L).cast(Pointer.deferred(4, UnboundedArrayRef.of(0x50, GsCOORDINATE2::new)));
    this.coord2ParamArrPtr_08 = ref.offset(4, 0x08L).cast(Pointer.deferred(4, UnboundedArrayRef.of(0x28, GsCOORD2PARAM::new)));
    this.ObjTable_0c = ref.offset(4, 0x0cL).cast(GsOBJTABLE2::new);
    this.coord2_14 = ref.offset(4, 0x14L).cast(GsCOORDINATE2::new);
    this.coord2Param_64 = ref.offset(4, 0x64L).cast(GsCOORD2PARAM::new);
    this.tmd_8c = ref.offset(4, 0x8cL).cast(Pointer.deferred(4, Tmd::new));
    this.ptr_ui_90 = ref.offset(4, 0x90L).cast(Pointer.deferred(4, UnboundedArrayRef.of(0xc, RotateTranslateStruct::new)));
    this.rotateTranslateArrPtr_94 = ref.offset(4, 0x94L).cast(Pointer.deferred(4, UnboundedArrayRef.of(0xc, RotateTranslateStruct::new)));
    this.animCount_98 = ref.offset(2, 0x98L).cast(UnsignedShortRef::new);
    this.us_9a = ref.offset(2, 0x9aL).cast(UnsignedShortRef::new);
    this.ub_9c = ref.offset(1, 0x9cL).cast(UnsignedByteRef::new);
    this.ub_9d = ref.offset(1, 0x9dL).cast(UnsignedByteRef::new);
    this.us_9e = ref.offset(2, 0x9eL).cast(UnsignedShortRef::new);
    this.us_a0 = ref.offset(2, 0xa0L).cast(ShortRef::new);
    this.ub_a2 = ref.offset(1, 0xa2L).cast(UnsignedByteRef::new);
    this.ub_a3 = ref.offset(1, 0xa3L).cast(UnsignedByteRef::new);
    this.smallerStructPtr_a4 = ref.offset(4, 0xa4L).cast(Pointer.deferred(4, SmallerStruct::new));
    this.ptr_a8 = ref.offset(4, 0xa8L).cast(UnsignedIntRef::new);
    this.usArr_ac = ref.offset(4, 0xacL).cast(ArrayRef.of(UnsignedShortRef.class, 7, 2, UnsignedShortRef::new));
    this.usArr_ba = ref.offset(4, 0xbaL).cast(ArrayRef.of(UnsignedShortRef.class, 7, 2, UnsignedShortRef::new));
    this.count_c8 = ref.offset(2, 0xc8L).cast(ShortRef::new);
    this.tmdNobj_ca = ref.offset(2, 0xcaL).cast(UnsignedShortRef::new);
    this.ub_cc = ref.offset(1, 0xccL).cast(UnsignedByteRef::new);
    this.ub_cd = ref.offset(1, 0xcdL).cast(UnsignedByteRef::new);

    this.aui_d0 = ref.offset(4, 0xd0L).cast(ArrayRef.of(UnsignedIntRef.class, 7, 4, UnsignedIntRef::new));
    this.aub_ec = ref.offset(1, 0xecL).cast(ArrayRef.of(UnsignedByteRef.class, 7, 1, UnsignedByteRef::new));
    this.ui_f4 = ref.offset(4, 0xf4L).cast(UnsignedIntRef::new);
    this.ui_f8 = ref.offset(4, 0xf8L).cast(UnsignedIntRef::new);
    this.scaleVector_fc = ref.offset(4, 0xfcL).cast(VECTOR::new);
    this.ui_108 = ref.offset(4, 0x108L).cast(UnsignedIntRef::new);
    this.vector_10c = ref.offset(4, 0x10cL).cast(VECTOR::new);
    this.vector_118 = ref.offset(4, 0x118L).cast(VECTOR::new);
    this.mrg_124 = ref.offset(4, 0x124L).cast(Pointer.deferred(4, MrgFile::new));
    this.us_128 = ref.offset(2, 0x128L).cast(UnsignedShortRef::new);
    this.us_12a = ref.offset(2, 0x12aL).cast(UnsignedShortRef::new);
    this.us_12c = ref.offset(2, 0x12cL).cast(UnsignedShortRef::new);
    this.mrgAnimGroup_12e = ref.offset(2, 0x12eL).cast(UnsignedShortRef::new);
    this.scriptFileIndex_130 = ref.offset(2, 0x130L).cast(UnsignedShortRef::new);
    this.mrgAnimGroupIndex_132 = ref.offset(2, 0x132L).cast(UnsignedShortRef::new);
    this.us_134 = ref.offset(2, 0x134L).cast(UnsignedShortRef::new);

    this.vec_138 = ref.offset(4, 0x138L).cast(VECTOR::new);
    this.ui_144 = ref.offset(4, 0x144L).cast(UnsignedIntRef::new);
    this.vec_148 = ref.offset(4, 0x148L).cast(VECTOR::new);
    this.vec_154 = ref.offset(4, 0x154L).cast(VECTOR::new);
    this.vec_160 = ref.offset(4, 0x160L).cast(VECTOR::new);
    this.ui_16c = ref.offset(4, 0x16cL).cast(IntRef::new);
    this.us_170 = ref.offset(2, 0x170L).cast(UnsignedShortRef::new);
    this.us_172 = ref.offset(2, 0x172L).cast(UnsignedShortRef::new);

    this.us_178 = ref.offset(2, 0x178L).cast(UnsignedShortRef::new);

    this.us_17c = ref.offset(2, 0x17cL).cast(UnsignedShortRef::new);

    this.us_180 = ref.offset(2, 0x180L).cast(UnsignedShortRef::new);

    this.us_184 = ref.offset(2, 0x184L).cast(UnsignedShortRef::new);

    this.ui_188 = ref.offset(4, 0x188L).cast(UnsignedIntRef::new);
    this.ui_18c = ref.offset(4, 0x18cL).cast(UnsignedIntRef::new);
    this.ui_190 = ref.offset(4, 0x190L).cast(UnsignedIntRef::new);
    this.ui_194 = ref.offset(4, 0x194L).cast(UnsignedIntRef::new);
    this.ui_198 = ref.offset(4, 0x198L).cast(UnsignedIntRef::new);
    this.ui_19c = ref.offset(4, 0x19cL).cast(UnsignedIntRef::new);
    this.ui_1a0 = ref.offset(4, 0x1a0L).cast(UnsignedIntRef::new);
    this.ui_1a4 = ref.offset(4, 0x1a4L).cast(UnsignedIntRef::new);
    this.ui_1a8 = ref.offset(4, 0x1a8L).cast(UnsignedIntRef::new);
    this.ui_1ac = ref.offset(4, 0x1acL).cast(UnsignedIntRef::new);
    this.ui_1b0 = ref.offset(4, 0x1b0L).cast(UnsignedIntRef::new);
    this.ui_1b4 = ref.offset(4, 0x1b4L).cast(UnsignedIntRef::new);
    this.ui_1b8 = ref.offset(4, 0x1b8L).cast(UnsignedIntRef::new);
    this.ui_1bc = ref.offset(4, 0x1bcL).cast(UnsignedIntRef::new);
    this.ui_1c0 = ref.offset(4, 0x1c0L).cast(UnsignedIntRef::new);
    this.flatLightingEnabled_1c4 = ref.offset(1, 0x1c4L).cast(BoolRef::new);
    this.flatLightRed_1c5 = ref.offset(1, 0x1c5L).cast(UnsignedByteRef::new);
    this.flatLightGreen_1c6 = ref.offset(1, 0x1c6L).cast(UnsignedByteRef::new);
    this.flatLightBlue_1c7 = ref.offset(1, 0x1c7L).cast(UnsignedByteRef::new);
    this.ambientColourEnabled_1c8 = ref.offset(1, 0x1c8L).cast(BoolRef::new);

    this.ambientRed_1ca = ref.offset(2, 0x1caL).cast(UnsignedShortRef::new);
    this.ambientGreen_1cc = ref.offset(2, 0x1ccL).cast(UnsignedShortRef::new);
    this.ambientBlue_1ce = ref.offset(2, 0x1ceL).cast(UnsignedShortRef::new);
    this._1d0 = ref.offset(4, 0x1d0L).cast(BigSubStruct::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
