package legend.game.modding.events.battle;

import legend.game.combat.bent.BattleEvent;
import legend.game.inventory.Item;

/**
 * DEPRECATED: subject to removal, use not recommended. Better ways to do this will be introduced in the future.
 */
@Deprecated
public class ItemBattleDescriptionEvent extends BattleEvent {
  public final Item item;
  public String description;

  public ItemBattleDescriptionEvent(final Item item, final String description) {
    this.item = item;
    this.description = description;
  }
}
