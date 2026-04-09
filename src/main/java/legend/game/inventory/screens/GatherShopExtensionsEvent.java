package legend.game.inventory.screens;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import legend.game.inventory.InventoryEntry;
import legend.game.types.Shop;
import org.legendofdragoon.modloader.events.Event;

public class GatherShopExtensionsEvent extends Event {
  public final ShopScreen screen;
  public final Shop shop;

  final Object2IntMap<ShopExtension> extensions = new Object2IntOpenHashMap<>();

  public GatherShopExtensionsEvent(final ShopScreen screen, final Shop shop) {
    this.screen = screen;
    this.shop = shop;
  }

  /**
   * @param priority Lower numbers are higher priority. Highest priority handlers will be called first.
   */
  public <T extends InventoryEntry<T>> void addExtension(final ShopExtension<T> extension, final int priority) {
    this.extensions.put(extension, priority);
  }
}
