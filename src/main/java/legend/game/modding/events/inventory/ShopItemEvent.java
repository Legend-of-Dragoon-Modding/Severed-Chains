package legend.game.modding.events.inventory;

import org.legendofdragoon.modloader.events.Event;

public class ShopItemEvent extends Event {
  public final int shopId;
  public final int slotId;
  public int itemId;
  public int price;

  public ShopItemEvent(final int shopId, final int slotId, final int itemId, final int price) {
    this.shopId = shopId;
    this.slotId = slotId;
    this.itemId = itemId;
    this.price = price;
  }
}
