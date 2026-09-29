package legend.game.wmap.registries;

import org.legendofdragoon.modloader.events.registries.RegistryEvent;
import org.legendofdragoon.modloader.registries.MutableRegistry;

public final class RegisterWorldMapServicesEvent extends RegistryEvent.Register<WorldMapServiceEntry> {
  public RegisterWorldMapServicesEvent(final MutableRegistry<WorldMapServiceEntry> registry) {
    super(registry);
  }
}
