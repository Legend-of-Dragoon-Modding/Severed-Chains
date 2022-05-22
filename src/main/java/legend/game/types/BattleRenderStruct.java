package legend.game.types;

import legend.core.gte.GsCOORD2PARAM;
import legend.core.gte.GsCOORDINATE2;
import legend.core.gte.GsDOBJ2;
import legend.core.gte.GsOBJTABLE2;
import legend.core.gte.Tmd;
import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.ShortRef;
import legend.core.memory.types.UnboundedArrayRef;
import legend.core.memory.types.UnsignedByteRef;
import legend.core.memory.types.UnsignedIntRef;
import legend.core.memory.types.UnsignedShortRef;

public class BattleRenderStruct implements MemoryRef {
  private final Value ref;

  public final UnboundedArrayRef<GsDOBJ2> dobj2s_00;
  public final UnboundedArrayRef<GsCOORDINATE2> coord2s_a0;
  public final UnboundedArrayRef<GsCOORD2PARAM> params_3c0;
  public final GsOBJTABLE2 objtable2_550;
  /** Overlaps last maxobj of object table */
  public final GsCOORDINATE2 coord2_558;
  public final GsCOORD2PARAM param_5a8;
  public final Pointer<Tmd> tmd_5d0;
  public final Pointer<UnboundedArrayRef<RotateTranslateStruct>> rotTrans_5d4;
  public final Pointer<UnboundedArrayRef<RotateTranslateStruct>> rotTrans_5d8;
  public final ShortRef rotTransCount_5dc;
  public final UnsignedShortRef _5de;
  public final UnsignedShortRef _5e0;
  public final UnsignedShortRef _5e2;
  public final UnsignedIntRef _5e4;
  public final ShortRef _5e8;

  /** TODO pointer to something? */
  public final UnsignedIntRef _5ec;
  public final ArrayRef<UnsignedIntRef> _5f0;
  public final ArrayRef<UnsignedByteRef> _618;
  public final ArrayRef<UnsignedShortRef> _622;
  public final ArrayRef<UnsignedShortRef> _636;
  public final ArrayRef<ShortRef> _64a;
  public final ArrayRef<ShortRef> _65e;

  public BattleRenderStruct(final Value ref) {
    this.ref = ref;

    this.dobj2s_00 = ref.offset(4, 0x00L).cast(UnboundedArrayRef.of(0x10, GsDOBJ2::new));
    this.coord2s_a0 = ref.offset(4, 0xa0L).cast(UnboundedArrayRef.of(0x50, GsCOORDINATE2::new));
    this.params_3c0 = ref.offset(4, 0x3c0L).cast(UnboundedArrayRef.of(0x28, GsCOORD2PARAM::new));
    this.objtable2_550 = ref.offset(4, 0x550L).cast(GsOBJTABLE2::new);
    this.coord2_558 = ref.offset(4, 0x558L).cast(GsCOORDINATE2::new);
    this.param_5a8 = ref.offset(4, 0x5a8L).cast(GsCOORD2PARAM::new);
    this.tmd_5d0 = ref.offset(4, 0x5d0L).cast(Pointer.deferred(4, Tmd::new));
    this.rotTrans_5d4 = ref.offset(4, 0x5d4L).cast(Pointer.deferred(4, UnboundedArrayRef.of(0xc, RotateTranslateStruct::new)));
    this.rotTrans_5d8 = ref.offset(4, 0x5d8L).cast(Pointer.deferred(4, UnboundedArrayRef.of(0xc, RotateTranslateStruct::new)));
    this.rotTransCount_5dc = ref.offset(2, 0x5dcL).cast(ShortRef::new);
    this._5de = ref.offset(2, 0x5deL).cast(UnsignedShortRef::new);
    this._5e0 = ref.offset(2, 0x5e0L).cast(UnsignedShortRef::new);
    this._5e2 = ref.offset(2, 0x5e2L).cast(UnsignedShortRef::new);
    this._5e4 = ref.offset(4, 0x5e4L).cast(UnsignedIntRef::new);
    this._5e8 = ref.offset(2, 0x5e8L).cast(ShortRef::new);

    this._5ec = ref.offset(4, 0x5ecL).cast(UnsignedIntRef::new);
    this._5f0 = ref.offset(4, 0x5f0L).cast(ArrayRef.of(UnsignedIntRef.class, 10, 4, UnsignedIntRef::new));
    this._618 = ref.offset(1, 0x618L).cast(ArrayRef.of(UnsignedByteRef.class, 10, 1, UnsignedByteRef::new));
    this._622 = ref.offset(2, 0x622L).cast(ArrayRef.of(UnsignedShortRef.class, 10, 2, UnsignedShortRef::new));
    this._636 = ref.offset(2, 0x636L).cast(ArrayRef.of(UnsignedShortRef.class, 10, 2, UnsignedShortRef::new));
    this._64a = ref.offset(2, 0x64aL).cast(ArrayRef.of(ShortRef.class, 10, 2, ShortRef::new));
    this._65e = ref.offset(2, 0x65eL).cast(ArrayRef.of(ShortRef.class, 10, 2, ShortRef::new));
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
