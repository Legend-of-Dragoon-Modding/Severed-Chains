package legend.game.modding.events.inventory;

import legend.game.inventory.InventoryEntry;
import legend.game.inventory.screens.ShopScreen;
import legend.game.types.Shop;
import org.legendofdragoon.modloader.events.Event;

import java.util.List;

public class ShopContentsEvent extends Event {
  public final Shop shop;
  public final List<ShopScreen.ShopEntry<InventoryEntry>> contents;

  public ShopContentsEvent(final Shop shop, final List<ShopScreen.ShopEntry<InventoryEntry>> contents) {
    this.shop = shop;
    this.contents = contents;
  }
}
