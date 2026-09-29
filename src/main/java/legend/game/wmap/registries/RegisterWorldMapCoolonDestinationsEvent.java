package legend.game.wmap.registries;

import org.legendofdragoon.modloader.events.registries.RegistryEvent;
import org.legendofdragoon.modloader.registries.MutableRegistry;

public final class RegisterWorldMapCoolonDestinationsEvent extends RegistryEvent.Register<WorldMapCoolonDestinationEntry> {
  public RegisterWorldMapCoolonDestinationsEvent(final MutableRegistry<WorldMapCoolonDestinationEntry> registry) {
    super(registry);
  }
}
