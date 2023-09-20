package legend.game.modding.events.inventory;

import legend.game.inventory.InventoryEntry;
import org.legendofdragoon.modloader.events.Event;

public class ShopSellPriceEvent extends Event {
  public final int shopId;
  public final InventoryEntry inv;
  public int price;

  public ShopSellPriceEvent(final int shopId, final InventoryEntry inv, final int price) {
    this.shopId = shopId;
    this.inv = inv;
    this.price = price;
  }
}
