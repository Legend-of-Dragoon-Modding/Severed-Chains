package legend.game.modding.events.inventory;

import legend.game.inventory.Item;

/**
 * Fired any time the player's item is taken
 */
public class TakeItemEvent extends InventoryEvent {
  /** The item that was taken */
  public final Item item;
  /** The slot from which the item was taken. Modify this to take an item from a different slot. */
  public int itemSlot;

  public TakeItemEvent(final Item item, final int itemSlot) {
    this.item = item;
    this.itemSlot = itemSlot;
  }
}
