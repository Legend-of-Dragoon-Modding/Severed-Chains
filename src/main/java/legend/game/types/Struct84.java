package legend.game.types;

import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.ShortRef;
import legend.core.memory.types.UnsignedByteRef;
import legend.core.memory.types.UnsignedIntRef;
import legend.core.memory.types.UnsignedShortRef;

public class Struct84 implements MemoryRef {
  private final Value ref;

  public final UnsignedIntRef _00;
  public final UnsignedShortRef _04;

  public final UnsignedIntRef _08;

  public final ShortRef _1c;
  public final ShortRef _1e;

  public final UnsignedIntRef ptr_24;

  public final UnsignedByteRef _28;

  public final ShortRef _2c;

  public final UnsignedIntRef _30;

  public final ShortRef _34;
  public final ShortRef _36;

  public final ShortRef _3a;
  public final ShortRef _3c;
  public final ShortRef _3e;
  public final ShortRef _40;

  public final ShortRef _44;
  public final ArrayRef<ShortRef> _46;

  public final UnsignedIntRef ptr_58;
  public final UnsignedIntRef _5c;
  public final UnsignedIntRef _60;
  public final UnsignedIntRef _64;
  public final IntRef _68;
  public final IntRef _6c;
  public final ShortRef _70;
  public final ShortRef _72;

  public final UnsignedIntRef _78;
  public final UnsignedIntRef _7c;
  public final UnsignedIntRef _80;

  public Struct84(final Value ref) {
    this.ref = ref;

    this._00 = ref.offset(4, 0x00L).cast(UnsignedIntRef::new);
    this._04 = ref.offset(2, 0x04L).cast(UnsignedShortRef::new);

    this._08 = ref.offset(4, 0x08L).cast(UnsignedIntRef::new);

    this._1c = ref.offset(2, 0x1cL).cast(ShortRef::new);
    this._1e = ref.offset(2, 0x1eL).cast(ShortRef::new);

    this.ptr_24 = ref.offset(4, 0x24L).cast(UnsignedIntRef::new);

    this._28 = ref.offset(1, 0x28L).cast(UnsignedByteRef::new);

    this._2c = ref.offset(2, 0x2cL).cast(ShortRef::new);

    this._30 = ref.offset(4, 0x30L).cast(UnsignedIntRef::new);

    this._34 = ref.offset(2, 0x34L).cast(ShortRef::new);
    this._36 = ref.offset(2, 0x36L).cast(ShortRef::new);

    this._3a = ref.offset(2, 0x3aL).cast(ShortRef::new);
    this._3c = ref.offset(2, 0x3cL).cast(ShortRef::new);
    this._3e = ref.offset(2, 0x3eL).cast(ShortRef::new);
    this._40 = ref.offset(2, 0x40L).cast(ShortRef::new);

    this._44 = ref.offset(2, 0x44L).cast(ShortRef::new);
    this._46 = ref.offset(2, 0x46L).cast(ArrayRef.of(ShortRef.class, 8, 2, ShortRef::new));

    this.ptr_58 = ref.offset(4, 0x58L).cast(UnsignedIntRef::new);
    this._5c = ref.offset(4, 0x5cL).cast(UnsignedIntRef::new);
    this._60 = ref.offset(4, 0x60L).cast(UnsignedIntRef::new);
    this._64 = ref.offset(4, 0x64L).cast(UnsignedIntRef::new);
    this._68 = ref.offset(4, 0x68L).cast(IntRef::new);
    this._6c = ref.offset(4, 0x6cL).cast(IntRef::new);
    this._70 = ref.offset(2, 0x70L).cast(ShortRef::new);
    this._72 = ref.offset(2, 0x72L).cast(ShortRef::new);

    this._78 = ref.offset(4, 0x78L).cast(UnsignedIntRef::new);
    this._7c = ref.offset(4, 0x7cL).cast(UnsignedIntRef::new);
    this._80 = ref.offset(4, 0x80L).cast(UnsignedIntRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
