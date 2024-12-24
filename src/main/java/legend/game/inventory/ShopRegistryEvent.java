package legend.game.inventory;

import legend.game.types.ShopStruct40;
import org.legendofdragoon.modloader.events.registries.RegistryEvent;
import org.legendofdragoon.modloader.registries.MutableRegistry;

public class ShopRegistryEvent extends RegistryEvent.Register<ShopStruct40> {
  public ShopRegistryEvent(final MutableRegistry<ShopStruct40> registry) {
    super(registry);
  }
}
