package legend.game.types;

import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.ShortRef;
import legend.core.memory.types.UnsignedByteRef;
import legend.core.memory.types.UnsignedIntRef;

public class MemcardDataStruct3c implements MemoryRef {
  private final Value ref;

  public final UnsignedByteRef _04;

  public final UnsignedIntRef char0Index_08;
  public final UnsignedIntRef char1Index_0c;
  public final UnsignedIntRef char2Index_10;

  public final UnsignedByteRef _14;
  public final UnsignedByteRef _15;
  public final ShortRef _16;
  public final ShortRef _18;

  public final UnsignedIntRef _1c;
  public final UnsignedIntRef _20;
  public final UnsignedIntRef _24;
  public final UnsignedIntRef _28;
  public final UnsignedByteRef _2c;
  public final UnsignedByteRef _2d;

  public MemcardDataStruct3c(final Value ref) {
    this.ref = ref;

    this._04 = ref.offset(1, 0x04L).cast(UnsignedByteRef::new);

    this.char0Index_08 = ref.offset(4, 0x08L).cast(UnsignedIntRef::new);
    this.char1Index_0c = ref.offset(4, 0x0cL).cast(UnsignedIntRef::new);
    this.char2Index_10 = ref.offset(4, 0x10L).cast(UnsignedIntRef::new);

    this._14 = ref.offset(1, 0x14L).cast(UnsignedByteRef::new);
    this._15 = ref.offset(1, 0x15L).cast(UnsignedByteRef::new);
    this._16 = ref.offset(2, 0x16L).cast(ShortRef::new);
    this._18 = ref.offset(2, 0x18L).cast(ShortRef::new);

    this._1c = ref.offset(4, 0x1cL).cast(UnsignedIntRef::new);
    this._20 = ref.offset(4, 0x20L).cast(UnsignedIntRef::new);
    this._24 = ref.offset(4, 0x24L).cast(UnsignedIntRef::new);
    this._28 = ref.offset(4, 0x28L).cast(UnsignedIntRef::new);
    this._2c = ref.offset(1, 0x2cL).cast(UnsignedByteRef::new);
    this._2d = ref.offset(1, 0x2dL).cast(UnsignedByteRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
