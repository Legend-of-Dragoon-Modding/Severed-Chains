package legend.game.types;

import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.ShortRef;
import legend.core.memory.types.UnsignedByteRef;
import legend.core.memory.types.UnsignedIntRef;
import legend.core.memory.types.UnsignedShortRef;

/** 0x2c bytes */
public class McqHeader implements MemoryRef {
  public static final long MAGIC_1 = 0x151_434dL;
  public static final long MAGIC_2 = 0x251_434dL;

  private final Value ref;

  public final UnsignedIntRef magic_00;
  public final UnsignedIntRef imageDataOffset_04;
  public final ShortRef width_08;
  public final ShortRef height_0a;
  public final UnsignedShortRef _0c;
  public final UnsignedShortRef _0e;
  public final UnsignedShortRef _10;
  public final UnsignedShortRef _12;
  public final UnsignedShortRef _14;
  public final UnsignedShortRef _16;

  public final UnsignedByteRef _18;
  public final UnsignedByteRef _19;
  public final UnsignedByteRef _1a;

  public final UnsignedByteRef _20;
  public final UnsignedByteRef _21;
  public final UnsignedByteRef _22;

  public final ShortRef _28;
  public final ShortRef _2a;

  public McqHeader(final Value ref) {
    this.ref = ref;

    this.magic_00 = ref.offset(4, 0x00L).cast(UnsignedIntRef::new);
    this.imageDataOffset_04 = ref.offset(4, 0x04L).cast(UnsignedIntRef::new);
    this.width_08 = ref.offset(2, 0x08L).cast(ShortRef::new);
    this.height_0a = ref.offset(2, 0x0aL).cast(ShortRef::new);
    this._0c = ref.offset(2, 0x0cL).cast(UnsignedShortRef::new);
    this._0e = ref.offset(2, 0x0eL).cast(UnsignedShortRef::new);
    this._10 = ref.offset(2, 0x10L).cast(UnsignedShortRef::new);
    this._12 = ref.offset(2, 0x12L).cast(UnsignedShortRef::new);
    this._14 = ref.offset(2, 0x14L).cast(UnsignedShortRef::new);
    this._16 = ref.offset(2, 0x16L).cast(UnsignedShortRef::new);

    this._18 = ref.offset(1, 0x18L).cast(UnsignedByteRef::new);
    this._19 = ref.offset(1, 0x19L).cast(UnsignedByteRef::new);
    this._1a = ref.offset(1, 0x1aL).cast(UnsignedByteRef::new);

    this._20 = ref.offset(1, 0x20L).cast(UnsignedByteRef::new);
    this._21 = ref.offset(1, 0x21L).cast(UnsignedByteRef::new);
    this._22 = ref.offset(1, 0x22L).cast(UnsignedByteRef::new);

    this._28 = ref.offset(2, 0x28L).cast(ShortRef::new);
    this._2a = ref.offset(2, 0x2aL).cast(ShortRef::new);
  }

  public long getImageDataAddress() {
    return this.getAddress() + this.imageDataOffset_04.get();
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
