package legend.game.modding.events.inventory;

import legend.game.inventory.Inventory;
import legend.game.inventory.ItemStack;

/**
 * Fired any time the player's item is taken
 */
public class TakeItemEvent extends InventoryEvent {
  /** The slot from which the item was taken */
  public final Inventory inventory;
  /** The stack to be removed from the inventory */
  public ItemStack stack;

  public TakeItemEvent(final Inventory inventory, final ItemStack stack) {
    this.inventory = inventory;
    this.stack = stack;
  }
}
