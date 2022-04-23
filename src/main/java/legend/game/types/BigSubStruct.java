package legend.game.types;

import legend.core.gte.SVECTOR;
import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.ShortRef;
import legend.core.memory.types.UnsignedIntRef;

public class BigSubStruct implements MemoryRef {
  private final Value ref;

  public final UnsignedIntRef _00;
  public final UnsignedIntRef _04;
  public final UnsignedIntRef _08;
  public final UnsignedIntRef _0c;
  public final UnsignedIntRef _10;

  public final UnsignedIntRef _18;
  public final ShortRef _1c;
  public final SVECTOR _1e;

  public final UnsignedIntRef _28;

  public final UnsignedIntRef _30;
  public final UnsignedIntRef _34;
  public final ShortRef _38;

  public BigSubStruct(final Value ref) {
    this.ref = ref;

    this._00 = ref.offset(4, 0x00L).cast(UnsignedIntRef::new);
    this._04 = ref.offset(4, 0x04L).cast(UnsignedIntRef::new);
    this._08 = ref.offset(4, 0x08L).cast(UnsignedIntRef::new);
    this._0c = ref.offset(4, 0x0cL).cast(UnsignedIntRef::new);
    this._10 = ref.offset(4, 0x10L).cast(UnsignedIntRef::new);

    this._18 = ref.offset(4, 0x18L).cast(UnsignedIntRef::new);
    this._1c = ref.offset(2, 0x1cL).cast(ShortRef::new);
    this._1e = ref.offset(2, 0x1eL).cast(SVECTOR::new);

    this._28 = ref.offset(4, 0x28L).cast(UnsignedIntRef::new);

    this._30 = ref.offset(4, 0x30L).cast(UnsignedIntRef::new);
    this._34 = ref.offset(4, 0x34L).cast(UnsignedIntRef::new);
    this._38 = ref.offset(2, 0x38L).cast(ShortRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
