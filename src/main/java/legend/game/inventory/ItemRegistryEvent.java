package legend.game.inventory;

import legend.game.modding.events.registries.RegistryEvent;
import legend.game.modding.registries.MutableRegistry;

public class ItemRegistryEvent extends RegistryEvent.Register<Item> {
  public ItemRegistryEvent(final MutableRegistry<Item> registry) {
    super(registry);
  }
}
