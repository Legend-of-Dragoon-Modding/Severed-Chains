package legend.game.types;

import legend.core.gte.COLOUR;
import legend.core.gte.GsCOORDINATE2;
import legend.core.gte.SVECTOR;
import legend.core.gte.VECTOR;
import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.ByteRef;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.ShortRef;
import legend.core.memory.types.UnboundedArrayRef;
import legend.core.memory.types.UnsignedByteRef;
import legend.core.memory.types.UnsignedIntRef;
import legend.core.memory.types.UnsignedShortRef;

public class WMapStruct19c0 implements MemoryRef {
  private final Value ref;

  public final RenderStruct20 _00;
  public final GsCOORDINATE2 coord2_20;
  public final SVECTOR mapRotation_70;
  public final ShortRef mapRotationStartAngle_78;
  public final UnsignedShortRef mapRotationEndAngle_7a;
  public final ShortRef mapRotationStep_7c;
  public final ShortRef mapRotationCounter_7e;
  public final UnsignedByteRef mapRotating_80;

  public final UnsignedIntRef _84;
  public final UnsignedIntRef _88;
  public final ArrayRef<COLOUR> colour_8c;

  public final UnsignedShortRef _98;
  public final UnsignedShortRef _9a;
  public final UnsignedShortRef _9c;
  public final ShortRef _9e;
  public final UnsignedShortRef _a0;
  public final VECTOR vec_a4;
  public final VECTOR vec_b4;

  public final ByteRef _c4;
  public final UnsignedByteRef _c5;

  public final RenderStruct20 _c8;

  public final IntRef _ec;
  public final IntRef _f0;

  public final IntRef _f8;
  public final IntRef _fc;

  public final ShortRef _108;
  public final ShortRef _10a;
  public final ShortRef _10c;
  public final UnsignedShortRef _10e;
  public final UnsignedByteRef _110;

  public final UnsignedIntRef _114;
  public final ShortRef _118;
  public final UnsignedByteRef _11a;

  public final ArrayRef<GsF_LIGHT> lights_11c;

  public final SVECTOR ambientLight_14c;
  public final UnboundedArrayRef<WMapSubStruct18> _154; //TODO unknown length

  public final IntRef _196c;
  public final IntRef _1970;
  public final IntRef _1974;

  public final ArrayRef<ShortRef> _19a8;
  public final ArrayRef<ShortRef> _19ae;

  public WMapStruct19c0(final Value ref) {
    this.ref = ref;

    this._00 = ref.offset(4, 0x00L).cast(RenderStruct20::new);
    this.coord2_20 = ref.offset(4, 0x20L).cast(GsCOORDINATE2::new);
    this.mapRotation_70 = ref.offset(2, 0x70L).cast(SVECTOR::new);
    this.mapRotationStartAngle_78 = ref.offset(2, 0x78L).cast(ShortRef::new);
    this.mapRotationEndAngle_7a = ref.offset(2, 0x7aL).cast(UnsignedShortRef::new);
    this.mapRotationStep_7c = ref.offset(2, 0x7cL).cast(ShortRef::new);
    this.mapRotationCounter_7e = ref.offset(2, 0x7eL).cast(ShortRef::new);
    this.mapRotating_80 = ref.offset(1, 0x80L).cast(UnsignedByteRef::new);

    this._84 = ref.offset(4, 0x84L).cast(UnsignedIntRef::new);
    this._88 = ref.offset(4, 0x88L).cast(UnsignedIntRef::new);
    this.colour_8c = ref.offset(4, 0x8cL).cast(ArrayRef.of(COLOUR.class, 3, 4, COLOUR::new));

    this._98 = ref.offset(2, 0x98L).cast(UnsignedShortRef::new);
    this._9a = ref.offset(2, 0x9aL).cast(UnsignedShortRef::new);
    this._9c = ref.offset(2, 0x9cL).cast(UnsignedShortRef::new);
    this._9e = ref.offset(2, 0x9eL).cast(ShortRef::new);
    this._a0 = ref.offset(2, 0xa0L).cast(UnsignedShortRef::new);
    this.vec_a4 = ref.offset(4, 0xa4L).cast(VECTOR::new);
    this.vec_b4 = ref.offset(4, 0xb4L).cast(VECTOR::new);

    this._c4 = ref.offset(1, 0xc4L).cast(ByteRef::new);
    this._c5 = ref.offset(1, 0xc5L).cast(UnsignedByteRef::new);

    this._c8 = ref.offset(4, 0xc8L).cast(RenderStruct20::new);

    this._ec = ref.offset(4, 0xecL).cast(IntRef::new);
    this._f0 = ref.offset(4, 0xf0L).cast(IntRef::new);

    this._f8 = ref.offset(4, 0xf8L).cast(IntRef::new);
    this._fc = ref.offset(4, 0xfcL).cast(IntRef::new);

    this._108 = ref.offset(2, 0x108L).cast(ShortRef::new);
    this._10a = ref.offset(2, 0x10aL).cast(ShortRef::new);
    this._10c = ref.offset(2, 0x10cL).cast(ShortRef::new);
    this._10e = ref.offset(2, 0x10eL).cast(UnsignedShortRef::new);
    this._110 = ref.offset(1, 0x110L).cast(UnsignedByteRef::new);

    this._114 = ref.offset(4, 0x114L).cast(UnsignedIntRef::new);
    this._118 = ref.offset(2, 0x118L).cast(ShortRef::new);
    this._11a = ref.offset(1, 0x11aL).cast(UnsignedByteRef::new);

    this.lights_11c = ref.offset(4, 0x11cL).cast(ArrayRef.of(GsF_LIGHT.class, 3, 0x10, GsF_LIGHT::new));

    this.ambientLight_14c = ref.offset(2, 0x14cL).cast(SVECTOR::new);
    this._154 = ref.offset(4, 0x154L).cast(UnboundedArrayRef.of(0x18, WMapSubStruct18::new));

    this._196c = ref.offset(4, 0x196cL).cast(IntRef::new);
    this._1970 = ref.offset(4, 0x1970L).cast(IntRef::new);
    this._1974 = ref.offset(4, 0x1974L).cast(IntRef::new);

    this._19a8 = ref.offset(2, 0x19a8L).cast(ArrayRef.of(ShortRef.class, 3, 2, ShortRef::new));
    this._19ae = ref.offset(2, 0x19aeL).cast(ArrayRef.of(ShortRef.class, 3, 2, ShortRef::new));
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
