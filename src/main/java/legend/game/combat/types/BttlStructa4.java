package legend.game.combat.types;

import legend.core.memory.Value;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.ShortRef;
import legend.core.memory.types.UnsignedIntRef;
import legend.core.memory.types.UnsignedShortRef;

/** Used for rendering the spell menu, maybe other stuff? */
public class BttlStructa4 implements MemoryRef {
  private final Value ref;

  public final ShortRef _00;
  public final UnsignedShortRef _02;
  public final UnsignedShortRef x_04;
  public final UnsignedShortRef y_06;
  public final ShortRef charIndex_08;
  public final ShortRef _0a;
  public final UnsignedShortRef _0c;
  public final UnsignedShortRef _0e;
  public final UnsignedShortRef _10;
  public final UnsignedShortRef _12;
  public final UnsignedShortRef _14;
  public final UnsignedShortRef _16;
  public final ShortRef _18;
  public final ShortRef _1a;
  public final ShortRef spellId_1c;
  public final ShortRef _1e;
  public final ShortRef _20;
  public final ShortRef count_22;
  public final ShortRef _24;
  public final ShortRef _26;
  public final ShortRef _28;
  public final ShortRef _2a;
  public final UnsignedIntRef _2c;
  public final ShortRef _30;

  public final IntRef _7c;
  public final IntRef _80;
  public final IntRef _84;
  public final IntRef _88;
  public final IntRef _8c;
  public final UnsignedIntRef _90;
  public final UnsignedIntRef _94;

  public final IntRef _a0;

  public BttlStructa4(final Value ref) {
    this.ref = ref;

    this._00 = ref.offset(2, 0x00L).cast(ShortRef::new);
    this._02 = ref.offset(2, 0x02L).cast(UnsignedShortRef::new);
    this.x_04 = ref.offset(2, 0x04L).cast(UnsignedShortRef::new);
    this.y_06 = ref.offset(2, 0x06L).cast(UnsignedShortRef::new);
    this.charIndex_08 = ref.offset(2, 0x08L).cast(ShortRef::new);
    this._0a = ref.offset(2, 0x0aL).cast(ShortRef::new);
    this._0c = ref.offset(2, 0x0cL).cast(UnsignedShortRef::new);
    this._0e = ref.offset(2, 0x0eL).cast(UnsignedShortRef::new);
    this._10 = ref.offset(2, 0x10L).cast(UnsignedShortRef::new);
    this._12 = ref.offset(2, 0x12L).cast(UnsignedShortRef::new);
    this._14 = ref.offset(2, 0x14L).cast(UnsignedShortRef::new);
    this._16 = ref.offset(2, 0x16L).cast(UnsignedShortRef::new);
    this._18 = ref.offset(2, 0x18L).cast(ShortRef::new);
    this._1a = ref.offset(2, 0x1aL).cast(ShortRef::new);
    this.spellId_1c = ref.offset(2, 0x1cL).cast(ShortRef::new);
    this._1e = ref.offset(2, 0x1eL).cast(ShortRef::new);
    this._20 = ref.offset(2, 0x20L).cast(ShortRef::new);
    this.count_22 = ref.offset(2, 0x22L).cast(ShortRef::new);
    this._24 = ref.offset(2, 0x24L).cast(ShortRef::new);
    this._26 = ref.offset(2, 0x26L).cast(ShortRef::new);
    this._28 = ref.offset(2, 0x28L).cast(ShortRef::new);
    this._2a = ref.offset(2, 0x2aL).cast(ShortRef::new);
    this._2c = ref.offset(4, 0x2cL).cast(UnsignedIntRef::new);
    this._30 = ref.offset(2, 0x30L).cast(ShortRef::new);

    this._7c = ref.offset(4, 0x7cL).cast(IntRef::new);
    this._80 = ref.offset(4, 0x80L).cast(IntRef::new);
    this._84 = ref.offset(4, 0x84L).cast(IntRef::new);
    this._88 = ref.offset(4, 0x88L).cast(IntRef::new);
    this._8c = ref.offset(4, 0x8cL).cast(IntRef::new);
    this._90 = ref.offset(4, 0x90L).cast(UnsignedIntRef::new);
    this._94 = ref.offset(4, 0x94L).cast(UnsignedIntRef::new);

    this._a0 = ref.offset(4, 0xa0L).cast(IntRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
