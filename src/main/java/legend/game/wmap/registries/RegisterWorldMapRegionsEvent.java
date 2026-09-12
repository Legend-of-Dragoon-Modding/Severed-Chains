package legend.game.wmap.registries;

import org.legendofdragoon.modloader.events.registries.RegistryEvent;
import org.legendofdragoon.modloader.registries.MutableRegistry;

public final class RegisterWorldMapRegionsEvent extends RegistryEvent.Register<WorldMapRegionEntry> {
  public RegisterWorldMapRegionsEvent(final MutableRegistry<WorldMapRegionEntry> registry) {
    super(registry);
  }
}
