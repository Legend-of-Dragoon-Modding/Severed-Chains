package legend.game.wmap.registries;

import org.legendofdragoon.modloader.events.registries.RegistryEvent;
import org.legendofdragoon.modloader.registries.MutableRegistry;

public final class RegisterWorldMapTeleportLinksEvent extends RegistryEvent.Register<WorldMapTeleportLinkEntry> {
  public RegisterWorldMapTeleportLinksEvent(final MutableRegistry<WorldMapTeleportLinkEntry> registry) {
    super(registry);
  }
}
