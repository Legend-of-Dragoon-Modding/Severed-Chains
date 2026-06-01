package legend.game.modding.events.inventory;

import legend.game.inventory.Inventory;
import legend.game.inventory.ItemStack;
import legend.game.inventory.OverflowMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Fired any time the player receives an item
 */
public class GiveItemEvent extends InventoryEvent {
  /** An unmodifiable list of the player's current items */
  public final Inventory inventory;
  /** The items that were given. Modders may add or remove items from this list to change what items the player receives. */
  public final List<ItemStack> givenItems = new ArrayList<>();
  /** How to handle too many items being given */
  public OverflowMode overflowMode;

  public GiveItemEvent(final Inventory inventory, final ItemStack givenItem, final OverflowMode overflowMode) {
    this.inventory = inventory;
    this.givenItems.add(givenItem);
    this.overflowMode = overflowMode;
  }
}
