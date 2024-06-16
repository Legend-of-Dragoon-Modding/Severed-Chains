package legend.game.modding.events.battle;

import legend.game.combat.bent.BattleEvent;
import legend.game.types.ItemStats0c;

public class TemporaryItemStatsEvent extends BattleEvent {
  public int itemId;
  public ItemStats0c itemStats;

  public TemporaryItemStatsEvent(final int itemId, final ItemStats0c itemStats) {
    this.itemId = itemId;
    this.itemStats = itemStats;
  }
}
