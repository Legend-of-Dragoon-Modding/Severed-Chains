package legend.game.wmap.registries;

import org.legendofdragoon.modloader.events.registries.RegistryEvent;
import org.legendofdragoon.modloader.registries.MutableRegistry;

public final class RegisterWorldMapPortalsEvent extends RegistryEvent.Register<WorldMapPortalEntry> {
  public RegisterWorldMapPortalsEvent(final MutableRegistry<WorldMapPortalEntry> registry) {
    super(registry);
  }
}
