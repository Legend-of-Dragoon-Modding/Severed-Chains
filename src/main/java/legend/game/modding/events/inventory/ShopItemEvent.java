package legend.game.modding.events.inventory;

import legend.game.modding.events.Event;

public class ShopItemEvent extends Event {
  public final int shopId;
  public final int slotId;
  public int itemId;

  public ShopItemEvent(final int shopId, final int slotId, final int itemId) {
    this.shopId = shopId;
    this.slotId = slotId;
    this.itemId = itemId;
  }
}
