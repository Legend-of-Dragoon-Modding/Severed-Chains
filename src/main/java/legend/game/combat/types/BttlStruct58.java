package legend.game.combat.types;

import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.ShortRef;
import legend.core.memory.types.UnsignedIntRef;
import legend.core.memory.types.UnsignedShortRef;

public class BttlStruct58 implements MemoryRef {
  private final Value ref;

  public final ShortRef _00;
  public final UnsignedShortRef _02;
  public final ShortRef _04;
  public final ShortRef _06;
  public final ShortRef _08;
  public final ShortRef _0a;
  public final ShortRef _0c;
  public final ShortRef _0e;
  public final ArrayRef<ShortRef> _10;
  public final ShortRef _22;
  public final ShortRef _24;
  public final ShortRef _26;
  public final ShortRef _28;
  public final ShortRef _2a;
  public final ShortRef _2c;

  /** Overlaps to end of struct */
  public final ArrayRef<UnsignedIntRef> all_30;
  public final IntRef _30;
  public final IntRef _34;
  public final IntRef _38;
  public final IntRef _3c;
  public final UnsignedIntRef _40;
  public final UnsignedIntRef _44;
  public final IntRef _48;
  public final UnsignedIntRef _4c;
  public final UnsignedIntRef _50;
  public final IntRef _54;

  public BttlStruct58(final Value ref) {
    this.ref = ref;

    this._00 = ref.offset(2, 0x00L).cast(ShortRef::new);
    this._02 = ref.offset(2, 0x02L).cast(UnsignedShortRef::new);
    this._04 = ref.offset(2, 0x04L).cast(ShortRef::new);
    this._06 = ref.offset(2, 0x06L).cast(ShortRef::new);
    this._08 = ref.offset(2, 0x08L).cast(ShortRef::new);
    this._0a = ref.offset(2, 0x0aL).cast(ShortRef::new);
    this._0c = ref.offset(2, 0x0cL).cast(ShortRef::new);
    this._0e = ref.offset(2, 0x0eL).cast(ShortRef::new);
    this._10 = ref.offset(2, 0x10L).cast(ArrayRef.of(ShortRef.class, 9, 2, ShortRef::new));
    this._22 = ref.offset(2, 0x22L).cast(ShortRef::new);
    this._24 = ref.offset(2, 0x24L).cast(ShortRef::new);
    this._26 = ref.offset(2, 0x26L).cast(ShortRef::new);
    this._28 = ref.offset(2, 0x28L).cast(ShortRef::new);
    this._2a = ref.offset(2, 0x2aL).cast(ShortRef::new);
    this._2c = ref.offset(2, 0x2cL).cast(ShortRef::new);

    this.all_30 = ref.offset(4, 0x30L).cast(ArrayRef.of(UnsignedIntRef.class, 10, 4, UnsignedIntRef::new));
    this._30 = ref.offset(4, 0x30L).cast(IntRef::new);
    this._34 = ref.offset(4, 0x34L).cast(IntRef::new);
    this._38 = ref.offset(4, 0x38L).cast(IntRef::new);
    this._3c = ref.offset(4, 0x3cL).cast(IntRef::new);
    this._40 = ref.offset(4, 0x40L).cast(UnsignedIntRef::new);
    this._44 = ref.offset(4, 0x44L).cast(UnsignedIntRef::new);
    this._48 = ref.offset(4, 0x48L).cast(IntRef::new);
    this._4c = ref.offset(4, 0x4cL).cast(UnsignedIntRef::new);
    this._50 = ref.offset(4, 0x50L).cast(UnsignedIntRef::new);
    this._54 = ref.offset(4, 0x54L).cast(IntRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
