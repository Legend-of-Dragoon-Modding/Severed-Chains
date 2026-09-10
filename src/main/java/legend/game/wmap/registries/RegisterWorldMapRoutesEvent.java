package legend.game.wmap.registries;

import org.legendofdragoon.modloader.events.registries.RegistryEvent;
import org.legendofdragoon.modloader.registries.MutableRegistry;

public final class RegisterWorldMapRoutesEvent extends RegistryEvent.Register<WorldMapRouteEntry> {
  public RegisterWorldMapRoutesEvent(final MutableRegistry<WorldMapRouteEntry> registry) {
    super(registry);
  }
}
