package legend.game.modding.events.inventory;

import legend.game.inventory.Item;
import legend.game.inventory.OverflowMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Fired any time the player receives an item
 */
public class GiveItemEvent extends InventoryEvent {
  /** The items that were given. Modders may add or remove items from this list to change what items the player receives. */
  public final List<Item> givenItems = new ArrayList<>();
  /** An unmodifiable list of the player's current items */
  public final List<Item> currentItems;
  /** The maximum number of items the player can hold */
  public final int maxInventorySize;
  /** How to handle too many items being given */
  public OverflowMode overflowMode = OverflowMode.FAIL;

  public GiveItemEvent(final Item givenItem, final List<Item> currentItems, final int maxInventorySize) {
    this.givenItems.add(givenItem);
    this.currentItems = currentItems;
    this.maxInventorySize = maxInventorySize;
  }
}
