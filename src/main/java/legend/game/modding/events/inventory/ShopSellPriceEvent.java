package legend.game.modding.events.inventory;

import legend.game.modding.events.Event;

public class ShopSellPriceEvent extends Event {
  public final int shopId;
  public final int itemId;
  public int price;

  public ShopSellPriceEvent(final int shopId, final int itemId, final int price) {
    this.shopId = shopId;
    this.itemId = itemId;
    this.price = price;
  }
}
