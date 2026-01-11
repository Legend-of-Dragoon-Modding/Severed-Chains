package legend.game.modding.events.inventory;

import legend.game.types.Shop;
import org.legendofdragoon.modloader.events.Event;

public class ShopEvent extends Event {
  public final Shop shop;

  public ShopEvent(final Shop shop) {
    this.shop = shop;
  }
}
