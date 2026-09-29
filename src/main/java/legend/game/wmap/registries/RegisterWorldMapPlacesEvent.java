package legend.game.wmap.registries;

import org.legendofdragoon.modloader.events.registries.RegistryEvent;
import org.legendofdragoon.modloader.registries.MutableRegistry;

public final class RegisterWorldMapPlacesEvent extends RegistryEvent.Register<WorldMapPlaceEntry> {
  public RegisterWorldMapPlacesEvent(final MutableRegistry<WorldMapPlaceEntry> registry) {
    super(registry);
  }
}
