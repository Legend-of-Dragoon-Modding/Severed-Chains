package legend.game.wmap.registries;

import org.legendofdragoon.modloader.events.registries.RegistryEvent;
import org.legendofdragoon.modloader.registries.MutableRegistry;

public final class RegisterWorldMapEncounterPoolsEvent extends RegistryEvent.Register<WorldMapEncounterPoolEntry> {
  public RegisterWorldMapEncounterPoolsEvent(final MutableRegistry<WorldMapEncounterPoolEntry> registry) {
    super(registry);
  }
}
