package legend.game.types;

import legend.core.gte.GsCOORDINATE2;
import legend.core.gte.SVECTOR;
import legend.core.gte.VECTOR;
import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.ByteRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.ShortRef;
import legend.core.memory.types.UnboundedArrayRef;
import legend.core.memory.types.UnsignedByteRef;
import legend.core.memory.types.UnsignedIntRef;
import legend.core.memory.types.UnsignedShortRef;

public class WMapStruct258 implements MemoryRef {
  private final Value ref;

  public final UnsignedIntRef _00;
  public final UnsignedByteRef _04;
  public final UnsignedByteRef _05;

  public final Pointer<WMapTmdRenderingStruct18> tmdRendering_08;
  public final ArrayRef<Pointer<BigStruct>> bigStructs_0c; //TODO doesn't use the whole BigStruct? Only 0x124 bytes? Is BigStruct too big?
  public final UnsignedIntRef _1c; //TODO pointer to a struct
  public final ShortRef _20;

  public final UnsignedIntRef _28;
  public final UnsignedIntRef _2c;
  public final UnsignedIntRef _30;
  public final GsCOORDINATE2 coord2_34;
  public final VECTOR vec_84;
  public final VECTOR vec_94;
  public final SVECTOR rotation_a4;

  public final ArrayRef<WMapStruct258Sub40> _b4;

  public final UnsignedIntRef _1e4;
  public final SVECTOR svec_1e8;
  public final SVECTOR svec_1f0;
  public final UnsignedByteRef _1f8;
  public final UnsignedByteRef _1f9;

  public final Pointer<WMapRender40> _1fc;
  public final SVECTOR svec_200;
  public final SVECTOR svec_208;

  public final UnsignedIntRef _218;
  public final UnsignedShortRef _21c;
  public final UnsignedShortRef _21e;
  public final ByteRef _220;
  public final UnsignedByteRef coolonWarpIndex_221;
  public final UnsignedByteRef coolonWarpIndex_222;
  public final UnsignedByteRef _223;
  public final Pointer<UnboundedArrayRef<VECTOR>> vecs_224;
  public final Pointer<UnboundedArrayRef<VECTOR>> vecs_228;
  /** TODO pointer */
  public final UnsignedIntRef ptr_22c;
  public final UnsignedIntRef _230;
  public final UnsignedIntRef _234;
  public final UnsignedIntRef _238;
  public final UnsignedIntRef _23c;

  public final UnsignedByteRef _244;

  public final UnsignedIntRef _248;

  public final UnsignedIntRef _250;
  public final UnsignedIntRef _254;

  public WMapStruct258(final Value ref) {
    this.ref = ref;

    this._00 = ref.offset(4, 0x00L).cast(UnsignedIntRef::new);
    this._04 = ref.offset(1, 0x04L).cast(UnsignedByteRef::new);
    this._05 = ref.offset(1, 0x05L).cast(UnsignedByteRef::new);

    this.tmdRendering_08 = ref.offset(4, 0x08L).cast(Pointer.deferred(4, WMapTmdRenderingStruct18::new));
    this.bigStructs_0c = ref.offset(4, 0x0cL).cast(ArrayRef.of(Pointer.classFor(BigStruct.class), 4, 4, Pointer.deferred(4, BigStruct::new)));
    this._1c = ref.offset(4, 0x1cL).cast(UnsignedIntRef::new);
    this._20 = ref.offset(2, 0x20L).cast(ShortRef::new);

    this._28 = ref.offset(4, 0x28L).cast(UnsignedIntRef::new);
    this._2c = ref.offset(4, 0x2cL).cast(UnsignedIntRef::new);
    this._30 = ref.offset(4, 0x30L).cast(UnsignedIntRef::new);
    this.coord2_34 = ref.offset(4, 0x34L).cast(GsCOORDINATE2::new);
    this.vec_84 = ref.offset(4, 0x84L).cast(VECTOR::new);
    this.vec_94 = ref.offset(4, 0x94L).cast(VECTOR::new);
    this.rotation_a4 = ref.offset(2, 0xa4L).cast(SVECTOR::new);

    this._b4 = ref.offset(4, 0xb4L).cast(ArrayRef.of(WMapStruct258Sub40.class, 4, 0x40, WMapStruct258Sub40::new));

    this._1e4 = ref.offset(4, 0x1e4L).cast(UnsignedIntRef::new);
    this.svec_1e8 = ref.offset(2, 0x1e8L).cast(SVECTOR::new);
    this.svec_1f0 = ref.offset(2, 0x1f0L).cast(SVECTOR::new);
    this._1f8 = ref.offset(1, 0x1f8L).cast(UnsignedByteRef::new);
    this._1f9 = ref.offset(1, 0x1f9L).cast(UnsignedByteRef::new);

    this._1fc = ref.offset(4, 0x1fcL).cast(Pointer.deferred(4, WMapRender40::new));
    this.svec_200 = ref.offset(2, 0x200L).cast(SVECTOR::new);
    this.svec_208 = ref.offset(2, 0x208L).cast(SVECTOR::new);

    this._218 = ref.offset(4, 0x218L).cast(UnsignedIntRef::new);
    this._21c = ref.offset(2, 0x21cL).cast(UnsignedShortRef::new);
    this._21e = ref.offset(2, 0x21eL).cast(UnsignedShortRef::new);
    this._220 = ref.offset(1, 0x220L).cast(ByteRef::new);
    this.coolonWarpIndex_221 = ref.offset(1, 0x221L).cast(UnsignedByteRef::new);
    this.coolonWarpIndex_222 = ref.offset(1, 0x222L).cast(UnsignedByteRef::new);
    this._223 = ref.offset(1, 0x223L).cast(UnsignedByteRef::new);
    this.vecs_224 = ref.offset(4, 0x224L).cast(Pointer.deferred(4, UnboundedArrayRef.of(0x10, VECTOR::new)));
    this.vecs_228 = ref.offset(4, 0x228L).cast(Pointer.deferred(4, UnboundedArrayRef.of(0x10, VECTOR::new)));
    this.ptr_22c = ref.offset(4, 0x22cL).cast(UnsignedIntRef::new);
    this._230 = ref.offset(4, 0x230L).cast(UnsignedIntRef::new);
    this._234 = ref.offset(4, 0x234L).cast(UnsignedIntRef::new);
    this._238 = ref.offset(4, 0x238L).cast(UnsignedIntRef::new);
    this._23c = ref.offset(4, 0x23cL).cast(UnsignedIntRef::new);

    this._244 = ref.offset(1, 0x244L).cast(UnsignedByteRef::new);

    this._248 = ref.offset(4, 0x248L).cast(UnsignedIntRef::new);

    this._250 = ref.offset(4, 0x250L).cast(UnsignedIntRef::new);
    this._254 = ref.offset(4, 0x254L).cast(UnsignedIntRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
