package legend.game.modding.events.battle;

import legend.game.combat.bent.BattleEntity27c;
import legend.game.combat.bent.BattleEvent;
import legend.game.types.ItemStats0c;

public class TemporaryItemStatsEvent extends BattleEvent {
  public int itemId;
  public ItemStats0c itemStats;
  public int attackType;
  public final BattleEntity27c bent;

  public TemporaryItemStatsEvent(final int itemId, final ItemStats0c itemStats, int attackType, BattleEntity27c bent) {
    this.itemId = itemId;
    this.itemStats = itemStats;
    this.attackType = attackType;
    this.bent = bent;
  }
}
