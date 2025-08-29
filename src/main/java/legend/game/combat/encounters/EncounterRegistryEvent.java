package legend.game.combat.encounters;

import org.legendofdragoon.modloader.events.registries.RegistryEvent;
import org.legendofdragoon.modloader.registries.MutableRegistry;

public class EncounterRegistryEvent extends RegistryEvent.Register<Encounter> {
  public EncounterRegistryEvent(final MutableRegistry<Encounter> registry) {
    super(registry);
  }
}
