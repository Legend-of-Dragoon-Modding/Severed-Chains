package legend.game.modding.events.inventory;

import legend.game.inventory.InventoryEntry;
import legend.game.types.Shop;

public class ShopSellPriceEvent extends ShopEvent {
  public final InventoryEntry<?> inv;
  public int price;

  public ShopSellPriceEvent(final Shop shop, final InventoryEntry<?> inv, final int price) {
    super(shop);
    this.inv = inv;
    this.price = price;
  }
}
