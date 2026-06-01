package legend.game.modding.events.inventory;

import legend.game.inventory.InventoryEntry;
import legend.game.inventory.screens.ShopScreen;
import legend.game.types.Shop;

import java.util.List;

public class ShopContentsEvent extends ShopEvent {
  public final List<ShopScreen.ShopEntry<InventoryEntry<?>>> contents;

  public ShopContentsEvent(final Shop shop, final List<ShopScreen.ShopEntry<InventoryEntry<?>>> contents) {
    super(shop);
    this.contents = contents;
  }
}
