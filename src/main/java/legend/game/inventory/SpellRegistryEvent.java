package legend.game.inventory;

import legend.game.modding.events.registries.RegistryEvent;
import legend.game.modding.registries.MutableRegistry;

public class SpellRegistryEvent extends RegistryEvent.Register<Spell> {
  public SpellRegistryEvent(final MutableRegistry<Spell> registry) {
    super(registry);
  }
}
