package legend.game.combat.types;

import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.ShortRef;

public class BattleStruct3c implements MemoryRef {
  private final Value ref;

  public final ShortRef charIndex_00;
  public final ShortRef _02;
  public final ShortRef _04;
  /**
   * 0x4 - max SP
   * 0x8 - also set when at max SP?
   */
  public final ShortRef flags_06;
  public final ShortRef _08;
  public final ShortRef _0a;
  public final ShortRef _0c;
  public final ShortRef _0e;
  public final ShortRef _10;
  public final ShortRef _12;
  public final ArrayRef<IntRef> _14;

  public BattleStruct3c(final Value ref) {
    this.ref = ref;

    this.charIndex_00 = ref.offset(2, 0x00L).cast(ShortRef::new);
    this._02 = ref.offset(2, 0x02L).cast(ShortRef::new);
    this._04 = ref.offset(2, 0x04L).cast(ShortRef::new);
    this.flags_06 = ref.offset(2, 0x06L).cast(ShortRef::new);
    this._08 = ref.offset(2, 0x08L).cast(ShortRef::new);
    this._0a = ref.offset(2, 0x0aL).cast(ShortRef::new);
    this._0c = ref.offset(2, 0x0cL).cast(ShortRef::new);
    this._0e = ref.offset(2, 0x0eL).cast(ShortRef::new);
    this._10 = ref.offset(2, 0x10L).cast(ShortRef::new);
    this._12 = ref.offset(2, 0x12L).cast(ShortRef::new);
    this._14 = ref.offset(4, 0x14L).cast(ArrayRef.of(IntRef.class, 10, 4, IntRef::new));
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
