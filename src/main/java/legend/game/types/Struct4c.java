package legend.game.types;

import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.ShortRef;
import legend.core.memory.types.UnsignedIntRef;
import legend.core.memory.types.UnsignedShortRef;

public class Struct4c implements MemoryRef {
  private final Value ref;

  public final UnsignedIntRef _00;
  public final ShortRef _04;
  public final ShortRef _06;
  public final UnsignedIntRef _08;
  public final UnsignedIntRef _0c;
  public final UnsignedIntRef _10;
  public final UnsignedShortRef _14;
  public final UnsignedShortRef _16;
  public final UnsignedShortRef _18;
  public final UnsignedShortRef _1a;
  public final UnsignedShortRef _1c;
  public final UnsignedShortRef _1e;
  public final ShortRef _20;
  public final ShortRef _22;
  public final ArrayRef<UnsignedIntRef> _24;

  public Struct4c(final Value ref) {
    this.ref = ref;

    this._00 = ref.offset(4, 0x00L).cast(UnsignedIntRef::new);
    this._04 = ref.offset(2, 0x04L).cast(ShortRef::new);
    this._06 = ref.offset(2, 0x06L).cast(ShortRef::new);
    this._08 = ref.offset(4, 0x08L).cast(UnsignedIntRef::new);
    this._0c = ref.offset(4, 0x0cL).cast(UnsignedIntRef::new);
    this._10 = ref.offset(4, 0x10L).cast(UnsignedIntRef::new);
    this._14 = ref.offset(2, 0x14L).cast(UnsignedShortRef::new);
    this._16 = ref.offset(2, 0x16L).cast(UnsignedShortRef::new);
    this._18 = ref.offset(2, 0x18L).cast(UnsignedShortRef::new);
    this._1a = ref.offset(2, 0x1aL).cast(UnsignedShortRef::new);
    this._1c = ref.offset(2, 0x1cL).cast(UnsignedShortRef::new);
    this._1e = ref.offset(2, 0x1eL).cast(UnsignedShortRef::new);
    this._20 = ref.offset(2, 0x20L).cast(ShortRef::new);
    this._22 = ref.offset(2, 0x22L).cast(ShortRef::new);
    this._24 = ref.offset(4, 0x24L).cast(ArrayRef.of(UnsignedIntRef.class, 10, 4, UnsignedIntRef::new));
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
