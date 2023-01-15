package legend.game.combat.types;

import legend.core.memory.Value;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.UnsignedIntRef;
import legend.core.memory.types.UnsignedShortRef;

public class AdditionStarburstEffect10 implements BttlScriptData6cSubBase1, MemoryRef {
  private final Value ref;

  public final IntRef scriptIndex_00;
  public final UnsignedShortRef count_04;

  public final UnsignedIntRef _08;
  /** TODO ptr */
  public final UnsignedIntRef _0c;

  public AdditionStarburstEffect10(final Value ref) {
    this.ref = ref;

    this.scriptIndex_00 = ref.offset(4, 0x00L).cast(IntRef::new);
    this.count_04 = ref.offset(2, 0x04L).cast(UnsignedShortRef::new);
    this._08 = ref.offset(4, 0x08L).cast(UnsignedIntRef::new);
    this._0c = ref.offset(4, 0x0cL).cast(UnsignedIntRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
