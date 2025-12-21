package legend.game.additions;

import org.legendofdragoon.modloader.events.registries.RegistryEvent;
import org.legendofdragoon.modloader.registries.MutableRegistry;

public class AdditionRegistryEvent extends RegistryEvent.Register<Addition> {
  public AdditionRegistryEvent(final MutableRegistry<Addition> registry) {
    super(registry);
  }
}
