package legend.game.wmap.registries;

import org.legendofdragoon.modloader.events.registries.RegistryEvent;
import org.legendofdragoon.modloader.registries.MutableRegistry;

public class RegisterWorldMapPresentationProfilesEvent extends RegistryEvent.Register<WorldMapPresentationProfileEntry> {
  public RegisterWorldMapPresentationProfilesEvent(final MutableRegistry<WorldMapPresentationProfileEntry> registry) {
    super(registry);
  }
}
