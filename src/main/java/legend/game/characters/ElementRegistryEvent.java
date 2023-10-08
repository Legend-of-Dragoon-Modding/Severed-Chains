package legend.game.characters;

import org.legendofdragoon.modloader.events.registries.RegistryEvent;
import org.legendofdragoon.modloader.registries.MutableRegistry;

public class ElementRegistryEvent extends RegistryEvent.Register<Element> {
  public ElementRegistryEvent(final MutableRegistry<Element> registry) {
    super(registry);
  }
}
