package legend.game.inventory.screens;

import legend.game.inventory.InventoryEntry;
import legend.game.types.Shop;
import org.legendofdragoon.modloader.events.Event;

import java.util.HashMap;
import java.util.Map;

public class GatherShopExtensionsEvent extends Event {
  public final ShopScreen screen;
  public final Shop shop;

  final Map<Class, ShopExtension> extensions = new HashMap<>();

  public GatherShopExtensionsEvent(final ShopScreen screen, final Shop shop) {
    this.screen = screen;
    this.shop = shop;
  }

  public <T extends InventoryEntry<T>> void addExtension(final Class<T> itemClass, final ShopExtension<T> extension) {
    this.extensions.put(itemClass, extension);
  }
}
