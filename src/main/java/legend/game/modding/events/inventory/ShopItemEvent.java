package legend.game.modding.events.inventory;

import legend.game.inventory.Item;
import legend.game.inventory.screens.ShopScreen;
import org.legendofdragoon.modloader.events.Event;

import java.util.List;

public class ShopItemEvent extends Event {
  public final int shopId;
  public final List<ShopScreen.ShopEntry<Item>> items;

  public ShopItemEvent(final int shopId, final List<ShopScreen.ShopEntry<Item>> items) {
    this.shopId = shopId;
    this.items = items;
  }
}
