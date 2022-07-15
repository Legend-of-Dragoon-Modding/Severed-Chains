package legend.game.combat.types;

import legend.core.gte.SVECTOR;
import legend.core.gte.VECTOR;
import legend.core.memory.Value;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.UnsignedByteRef;
import legend.core.memory.types.UnsignedIntRef;
import legend.game.types.GsRVIEW2;

public class BattleCamera implements MemoryRef {
  private final Value ref;

  public final GsRVIEW2 rview2_00;

  public final VECTOR vec_20;

  public final IntRef _2c;
  public final IntRef _30;

  public final IntRef _38;
  public final IntRef _3c;
  public final IntRef _40;
  public final IntRef _44;
  public final IntRef _48;

  public final IntRef _54;

  public final IntRef _5c;
  public final VECTOR vec_60;
  public final IntRef _6c;
  public final IntRef _70;
  public final VECTOR vec_74;

  public final IntRef bobjIndex_80;

  public final IntRef callbackIndex_88;
  public final SVECTOR svec_8c;
  public final VECTOR vec_94;

  public final IntRef _a0;
  public final IntRef _a4;

  public final IntRef _ac;
  public final IntRef _b0;
  public final IntRef _b4;
  public final IntRef _b8;
  public final IntRef _bc;

  public final IntRef _c8;

  public final IntRef _d0;
  public final IntRef _d4;
  public final IntRef _d8;
  public final IntRef _dc;
  public final IntRef _e0;
  public final IntRef _e4;
  public final IntRef _e8;
  public final IntRef _ec;
  public final IntRef _f0;
  public final IntRef bobjIndex_f4;

  public final IntRef callbackIndex_fc;
  public final IntRef _100;
  public final IntRef _104;
  public final IntRef _108;
  public final IntRef _10c;
  public final IntRef _110;
  public final IntRef _114;
  public final UnsignedByteRef _118;

  public final UnsignedIntRef _11c;
  public final UnsignedByteRef _120;
  public final UnsignedByteRef _121;
  public final UnsignedByteRef _122;
  public final UnsignedByteRef _123;

  public BattleCamera(final Value ref) {
    this.ref = ref;

    this.rview2_00 = ref.offset(4, 0x00L).cast(GsRVIEW2::new);

    this.vec_20 = ref.offset(4, 0x20L).cast(VECTOR::new);

    this._2c = ref.offset(4, 0x2cL).cast(IntRef::new);
    this._30 = ref.offset(4, 0x30L).cast(IntRef::new);

    this._38 = ref.offset(4, 0x38L).cast(IntRef::new);
    this._3c = ref.offset(4, 0x3cL).cast(IntRef::new);
    this._40 = ref.offset(4, 0x40L).cast(IntRef::new);
    this._44 = ref.offset(4, 0x44L).cast(IntRef::new);
    this._48 = ref.offset(4, 0x48L).cast(IntRef::new);

    this._54 = ref.offset(4, 0x54L).cast(IntRef::new);

    this._5c = ref.offset(4, 0x5cL).cast(IntRef::new);
    this.vec_60 = ref.offset(4, 0x60L).cast(VECTOR::new);
    this._6c = ref.offset(4, 0x6cL).cast(IntRef::new);
    this._70 = ref.offset(4, 0x70L).cast(IntRef::new);
    this.vec_74 = ref.offset(4, 0x74L).cast(VECTOR::new);

    this.bobjIndex_80 = ref.offset(4, 0x80L).cast(IntRef::new);

    this.callbackIndex_88 = ref.offset(4, 0x88L).cast(IntRef::new);
    this.svec_8c = ref.offset(2, 0x8cL).cast(SVECTOR::new);
    this.vec_94 = ref.offset(4, 0x94L).cast(VECTOR::new);

    this._a0 = ref.offset(4, 0xa0L).cast(IntRef::new);
    this._a4 = ref.offset(4, 0xa4L).cast(IntRef::new);

    this._ac = ref.offset(4, 0xacL).cast(IntRef::new);
    this._b0 = ref.offset(4, 0xb0L).cast(IntRef::new);
    this._b4 = ref.offset(4, 0xb4L).cast(IntRef::new);
    this._b8 = ref.offset(4, 0xb8L).cast(IntRef::new);
    this._bc = ref.offset(4, 0xbcL).cast(IntRef::new);

    this._c8 = ref.offset(4, 0xc8L).cast(IntRef::new);

    this._d0 = ref.offset(4, 0xd0L).cast(IntRef::new);
    this._d4 = ref.offset(4, 0xd4L).cast(IntRef::new);
    this._d8 = ref.offset(4, 0xd8L).cast(IntRef::new);
    this._dc = ref.offset(4, 0xdcL).cast(IntRef::new);
    this._e0 = ref.offset(4, 0xe0L).cast(IntRef::new);
    this._e4 = ref.offset(4, 0xe4L).cast(IntRef::new);
    this._e8 = ref.offset(4, 0xe8L).cast(IntRef::new);
    this._ec = ref.offset(4, 0xecL).cast(IntRef::new);
    this._f0 = ref.offset(4, 0xf0L).cast(IntRef::new);
    this.bobjIndex_f4 = ref.offset(4, 0xf4L).cast(IntRef::new);

    this.callbackIndex_fc = ref.offset(4, 0xfcL).cast(IntRef::new);
    this._100 = ref.offset(4, 0x100L).cast(IntRef::new);
    this._104 = ref.offset(4, 0x104L).cast(IntRef::new);
    this._108 = ref.offset(4, 0x108L).cast(IntRef::new);
    this._10c = ref.offset(4, 0x10cL).cast(IntRef::new);
    this._110 = ref.offset(4, 0x110L).cast(IntRef::new);
    this._114 = ref.offset(4, 0x114L).cast(IntRef::new);
    this._118 = ref.offset(1, 0x118L).cast(UnsignedByteRef::new);

    this._11c = ref.offset(4, 0x11cL).cast(UnsignedIntRef::new);
    this._120 = ref.offset(1, 0x120L).cast(UnsignedByteRef::new);
    this._121 = ref.offset(1, 0x121L).cast(UnsignedByteRef::new);
    this._122 = ref.offset(1, 0x122L).cast(UnsignedByteRef::new);
    this._123 = ref.offset(1, 0x123L).cast(UnsignedByteRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
