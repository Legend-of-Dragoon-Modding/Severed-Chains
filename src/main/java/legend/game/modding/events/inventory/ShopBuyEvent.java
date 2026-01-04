package legend.game.modding.events.inventory;

import legend.game.inventory.InventoryEntry;
import legend.game.types.Shop;

public class ShopBuyEvent extends ShopEvent {
  public final InventoryEntry<?> item;

  public ShopBuyEvent(final Shop shop, final InventoryEntry<?> item) {
    super(shop);
    this.item = item;
  }
}
