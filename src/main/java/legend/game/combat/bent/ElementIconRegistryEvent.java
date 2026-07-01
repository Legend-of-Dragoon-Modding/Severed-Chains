package legend.game.combat.bent;

import legend.game.characters.Element;
import org.legendofdragoon.modloader.events.registries.RegistryEvent;
import org.legendofdragoon.modloader.registries.MutableRegistry;

public class ElementIconRegistryEvent extends RegistryEvent.Register<ElementIconString> {
  public ElementIconRegistryEvent(final MutableRegistry<ElementIconString> registry) {
    super(registry);
  }
}
