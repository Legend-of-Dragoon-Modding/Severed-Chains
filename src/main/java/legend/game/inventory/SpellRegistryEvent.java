package legend.game.inventory;

import org.legendofdragoon.modloader.events.registries.RegistryEvent;
import org.legendofdragoon.modloader.registries.MutableRegistry;

public class SpellRegistryEvent extends RegistryEvent.Register<Spell> {
  public SpellRegistryEvent(final MutableRegistry<Spell> registry) {
    super(registry);
  }
}
