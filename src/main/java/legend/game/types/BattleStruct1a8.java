package legend.game.types;

import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.ShortRef;
import legend.core.memory.types.UnsignedByteRef;
import legend.core.memory.types.UnsignedIntRef;
import legend.core.memory.types.UnsignedShortRef;

public class BattleStruct1a8 implements MemoryRef {
  private final Value ref;

  /** TODO pointer to a file of some kind */
  public final UnsignedIntRef _00;

  public final UnsignedIntRef _08;

  /** TODO pointer to a file of some kind */
  public final UnsignedIntRef _10;
  public final UnsignedIntRef _14;

  public final UnsignedByteRef _1d;

  public final UnsignedIntRef _194;
  public final UnsignedIntRef _198;
  public final ShortRef _19c;
  public final UnsignedShortRef _19e;
  public final ShortRef _1a0;
  public final ShortRef _1a2;
  public final ShortRef _1a4;
  public final ShortRef _1a6;

  public BattleStruct1a8(final Value ref) {
    this.ref = ref;

    this._00 = ref.offset(4, 0x00L).cast(UnsignedIntRef::new);

    this._08 = ref.offset(4, 0x08L).cast(UnsignedIntRef::new);

    this._10 = ref.offset(4, 0x10L).cast(UnsignedIntRef::new);
    this._14 = ref.offset(4, 0x14L).cast(UnsignedIntRef::new);

    this._1d = ref.offset(1, 0x1dL).cast(UnsignedByteRef::new);

    this._194 = ref.offset(4, 0x194L).cast(UnsignedIntRef::new);
    this._198 = ref.offset(4, 0x198L).cast(UnsignedIntRef::new);
    this._19c = ref.offset(2, 0x19cL).cast(ShortRef::new);
    this._19e = ref.offset(2, 0x19eL).cast(UnsignedShortRef::new);
    this._1a0 = ref.offset(2, 0x1a0L).cast(ShortRef::new);
    this._1a2 = ref.offset(2, 0x1a2L).cast(ShortRef::new);
    this._1a4 = ref.offset(2, 0x1a4L).cast(ShortRef::new);
    this._1a6 = ref.offset(2, 0x1a6L).cast(ShortRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
