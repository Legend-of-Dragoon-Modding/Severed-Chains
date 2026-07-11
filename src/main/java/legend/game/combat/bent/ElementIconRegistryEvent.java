package legend.game.combat.bent;

import org.legendofdragoon.modloader.events.registries.RegistryEvent;
import org.legendofdragoon.modloader.registries.MutableRegistry;

public class ElementIconRegistryEvent extends RegistryEvent.Register<ElementIcon> {
  public ElementIconRegistryEvent(final MutableRegistry<ElementIcon> registry) {
    super(registry);
  }
}
