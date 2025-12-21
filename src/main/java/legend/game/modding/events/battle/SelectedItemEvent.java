package legend.game.modding.events.battle;

import legend.game.combat.Battle;
import legend.game.combat.bent.BattleEvent;
import legend.game.inventory.Item;

/**
 * DEPRECATED: subject to removal, use not recommended. Better ways to do this will be introduced in the future.
 */
@Deprecated
public class SelectedItemEvent extends BattleEvent {
  public short itemId;
  public Item item;

  public SelectedItemEvent(final Battle battle, final short itemId, final Item item) {
    super(battle);
    this.itemId = itemId;
    this.item = item;
  }
}