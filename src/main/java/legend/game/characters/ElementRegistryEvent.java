package legend.game.characters;

import legend.game.modding.events.registries.RegistryEvent;
import legend.game.modding.registries.MutableRegistry;

public class ElementRegistryEvent extends RegistryEvent.Register<Element> {
  public ElementRegistryEvent(final MutableRegistry<Element> registry) {
    super(registry);
  }
}
