package legend.game.inventory;

import legend.game.types.Shop;
import org.legendofdragoon.modloader.events.registries.RegistryEvent;
import org.legendofdragoon.modloader.registries.MutableRegistry;

public class ShopRegistryEvent extends RegistryEvent.Register<Shop> {
  public ShopRegistryEvent(final MutableRegistry<Shop> registry) {
    super(registry);
  }
}
