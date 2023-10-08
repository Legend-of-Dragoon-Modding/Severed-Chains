package legend.game.combat.environment;

import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.ShortRef;

public class BattleItemMenuArrowUvMetrics06 extends BattleMenuBackgroundUvMetrics04 implements MemoryRef {
  public final ShortRef uvShiftType_04;

  public BattleItemMenuArrowUvMetrics06(final Value ref) {
    super(ref);

    this.uvShiftType_04 = ref.offset(2, 0x04).cast(ShortRef::new);
  }
}
