package legend.game.combat.ui;

import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.ShortRef;

public class BattleHudCharacterDisplay3c implements MemoryRef {
  private final Value ref;

  public final ShortRef charIndex_00;
  public final ShortRef charId_02;
  public final ShortRef unused_04;
  /**
   * 0x4 - max SP
   * 0x8 - max SP blinking border (turns on and off each frame)
   */
  public final ShortRef flags_06;
  public final ShortRef x_08;
  public final ShortRef y_0a;
  public final ShortRef unused_0c;
  public final ShortRef unused_0e;
  public final ShortRef unused_10;
  public final ShortRef unused_12;
  /** TODO This should be another struct I think */
  public final ArrayRef<IntRef> _14;

  public BattleHudCharacterDisplay3c(final Value ref) {
    this.ref = ref;

    this.charIndex_00 = ref.offset(2, 0x00L).cast(ShortRef::new);
    this.charId_02 = ref.offset(2, 0x02L).cast(ShortRef::new);
    this.unused_04 = ref.offset(2, 0x04L).cast(ShortRef::new);
    this.flags_06 = ref.offset(2, 0x06L).cast(ShortRef::new);
    this.x_08 = ref.offset(2, 0x08L).cast(ShortRef::new);
    this.y_0a = ref.offset(2, 0x0aL).cast(ShortRef::new);
    this.unused_0c = ref.offset(2, 0x0cL).cast(ShortRef::new);
    this.unused_0e = ref.offset(2, 0x0eL).cast(ShortRef::new);
    this.unused_10 = ref.offset(2, 0x10L).cast(ShortRef::new);
    this.unused_12 = ref.offset(2, 0x12L).cast(ShortRef::new);
    this._14 = ref.offset(4, 0x14L).cast(ArrayRef.of(IntRef.class, 10, 4, IntRef::new));
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
