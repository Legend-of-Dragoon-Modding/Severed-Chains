package legend.game.inventory;

import org.legendofdragoon.modloader.events.registries.RegistryEvent;
import org.legendofdragoon.modloader.registries.MutableRegistry;

public class GoodsRegistryEvent extends RegistryEvent.Register<Good> {
  public GoodsRegistryEvent(final MutableRegistry<Good> registry) {
    super(registry);
  }
}
