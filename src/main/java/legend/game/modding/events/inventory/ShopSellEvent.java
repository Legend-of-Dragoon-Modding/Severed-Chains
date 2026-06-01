package legend.game.modding.events.inventory;

import legend.game.inventory.InventoryEntry;
import legend.game.types.Shop;

public class ShopSellEvent extends ShopEvent {
  public final InventoryEntry<?> item;

  public ShopSellEvent(final Shop shop, final InventoryEntry<?> item) {
    super(shop);
    this.item = item;
  }
}
