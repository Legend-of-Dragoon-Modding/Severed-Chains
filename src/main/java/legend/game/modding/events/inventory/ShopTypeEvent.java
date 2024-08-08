package legend.game.modding.events.inventory;

import org.legendofdragoon.modloader.events.Event;

public class ShopTypeEvent extends Event {
  public int shopType;
  public final int shopId;

  public ShopTypeEvent(int shopType, int shopId) {
    this.shopType = shopType;
    this.shopId = shopId;
  }
}
