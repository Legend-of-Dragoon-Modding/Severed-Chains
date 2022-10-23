package legend.game.types;

import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.ShortRef;
import legend.core.memory.types.UnsignedByteRef;
import legend.core.memory.types.UnsignedIntRef;

public class Struct84 implements MemoryRef {
  private final Value ref;

  public final IntRef _00;
  public final ShortRef _04;

  public final UnsignedIntRef _08;
  public final IntRef z_0c;
  public final UnsignedIntRef _10;
  public final ShortRef _14;
  public final ShortRef _16;
  public final ShortRef _18;
  public final ShortRef _1a;
  public final ShortRef _1c;
  public final ShortRef _1e;
  public final ShortRef _20;
  public final ShortRef _22;
  public final Pointer<LodString> str_24;

  public final UnsignedByteRef _28;

  public final ShortRef _2a;
  public final ShortRef _2c;

  public final IntRef _30;

  public final ShortRef _34;
  public final ShortRef _36;
  public final ShortRef _38;
  public final ShortRef _3a;
  public final ShortRef _3c;
  public final ShortRef _3e;
  public final ShortRef _40;
  public final ShortRef _42;
  public final ShortRef _44;
  public final ArrayRef<ShortRef> _46;

  public final UnsignedIntRef ptr_58;
  public final IntRef _5c;
  public final IntRef _60;
  public final IntRef _64;
  public final IntRef _68;
  public final IntRef _6c;
  public final ShortRef _70;
  public final ShortRef _72;

  public final IntRef _78;
  public final IntRef _7c;
  public final IntRef _80;

  public Struct84(final Value ref) {
    this.ref = ref;

    this._00 = ref.offset(4, 0x00L).cast(IntRef::new);
    this._04 = ref.offset(2, 0x04L).cast(ShortRef::new);

    this._08 = ref.offset(4, 0x08L).cast(UnsignedIntRef::new);
    this.z_0c = ref.offset(4, 0x0cL).cast(IntRef::new);
    this._10 = ref.offset(4, 0x10L).cast(UnsignedIntRef::new);
    this._14 = ref.offset(2, 0x14L).cast(ShortRef::new);
    this._16 = ref.offset(2, 0x16L).cast(ShortRef::new);
    this._18 = ref.offset(2, 0x18L).cast(ShortRef::new);
    this._1a = ref.offset(2, 0x1aL).cast(ShortRef::new);
    this._1c = ref.offset(2, 0x1cL).cast(ShortRef::new);
    this._1e = ref.offset(2, 0x1eL).cast(ShortRef::new);
    this._20 = ref.offset(2, 0x20L).cast(ShortRef::new);
    this._22 = ref.offset(2, 0x22L).cast(ShortRef::new);
    this.str_24 = ref.offset(4, 0x24L).cast(Pointer.deferred(2, LodString::new));

    this._28 = ref.offset(1, 0x28L).cast(UnsignedByteRef::new);

    this._2a = ref.offset(2, 0x2aL).cast(ShortRef::new);
    this._2c = ref.offset(2, 0x2cL).cast(ShortRef::new);

    this._30 = ref.offset(4, 0x30L).cast(IntRef::new);

    this._34 = ref.offset(2, 0x34L).cast(ShortRef::new);
    this._36 = ref.offset(2, 0x36L).cast(ShortRef::new);
    this._38 = ref.offset(2, 0x38L).cast(ShortRef::new);
    this._3a = ref.offset(2, 0x3aL).cast(ShortRef::new);
    this._3c = ref.offset(2, 0x3cL).cast(ShortRef::new);
    this._3e = ref.offset(2, 0x3eL).cast(ShortRef::new);
    this._40 = ref.offset(2, 0x40L).cast(ShortRef::new);
    this._42 = ref.offset(2, 0x42L).cast(ShortRef::new);
    this._44 = ref.offset(2, 0x44L).cast(ShortRef::new);
    this._46 = ref.offset(2, 0x46L).cast(ArrayRef.of(ShortRef.class, 8, 2, ShortRef::new));

    this.ptr_58 = ref.offset(4, 0x58L).cast(UnsignedIntRef::new);
    this._5c = ref.offset(4, 0x5cL).cast(IntRef::new);
    this._60 = ref.offset(4, 0x60L).cast(IntRef::new);
    this._64 = ref.offset(4, 0x64L).cast(IntRef::new);
    this._68 = ref.offset(4, 0x68L).cast(IntRef::new);
    this._6c = ref.offset(4, 0x6cL).cast(IntRef::new);
    this._70 = ref.offset(2, 0x70L).cast(ShortRef::new);
    this._72 = ref.offset(2, 0x72L).cast(ShortRef::new);

    this._78 = ref.offset(4, 0x78L).cast(IntRef::new);
    this._7c = ref.offset(4, 0x7cL).cast(IntRef::new);
    this._80 = ref.offset(4, 0x80L).cast(IntRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
