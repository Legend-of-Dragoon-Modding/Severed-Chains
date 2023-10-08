package legend.game.inventory;

import org.legendofdragoon.modloader.events.registries.RegistryEvent;
import org.legendofdragoon.modloader.registries.MutableRegistry;

public class ItemRegistryEvent extends RegistryEvent.Register<Item> {
  public ItemRegistryEvent(final MutableRegistry<Item> registry) {
    super(registry);
  }
}
