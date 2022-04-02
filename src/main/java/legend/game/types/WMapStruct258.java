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
import legend.core.memory.types.UnsignedByteRef;
import legend.core.memory.types.UnsignedIntRef;

public class WMapStruct258 implements MemoryRef {
  private final Value ref;

  public final UnsignedIntRef _00;
  public final UnsignedByteRef _04;
  public final UnsignedByteRef _05;

  public final Pointer<WMapTmdRenderingStruct18> tmdRendering_08;
  public final ArrayRef<Pointer<BigStruct>> bigStructs_0c; //TODO doesn't use the whole BigStruct? Only 0x124 bytes? Is BigStruct too big?
  public final UnsignedIntRef _1c;
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

  public final UnsignedByteRef _1f8;
  public final UnsignedByteRef _1f9;

  public final Pointer<WMapRender40> _1fc;

  public final ByteRef _220;

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

    this._1f8 = ref.offset(1, 0x1f8L).cast(UnsignedByteRef::new);
    this._1f9 = ref.offset(1, 0x1f9L).cast(UnsignedByteRef::new);

    this._1fc = ref.offset(4, 0x1fcL).cast(Pointer.deferred(4, WMapRender40::new));

    this._220 = ref.offset(1, 0x220L).cast(ByteRef::new);

    this._248 = ref.offset(4, 0x248L).cast(UnsignedIntRef::new);

    this._250 = ref.offset(4, 0x250L).cast(UnsignedIntRef::new);
    this._254 = ref.offset(4, 0x254L).cast(UnsignedIntRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
