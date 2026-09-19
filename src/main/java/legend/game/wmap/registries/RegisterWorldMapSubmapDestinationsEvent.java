package legend.game.wmap.registries;

import org.legendofdragoon.modloader.events.registries.RegistryEvent;
import org.legendofdragoon.modloader.registries.MutableRegistry;

public final class RegisterWorldMapSubmapDestinationsEvent extends RegistryEvent.Register<WorldMapSubmapDestinationEntry> {
  public RegisterWorldMapSubmapDestinationsEvent(final MutableRegistry<WorldMapSubmapDestinationEntry> registry) {
    super(registry);
  }
}
