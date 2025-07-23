package legend.game.characters;

import org.legendofdragoon.modloader.events.registries.RegistryEvent;
import org.legendofdragoon.modloader.registries.MutableRegistry;

public class SpellRingRegistryEvent extends RegistryEvent.Register<SpellRing> {
  public SpellRingRegistryEvent(final MutableRegistry<SpellRing> registry) {
    super(registry);
  }
}
