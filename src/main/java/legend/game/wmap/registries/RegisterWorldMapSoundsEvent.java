package legend.game.wmap.registries;

import org.legendofdragoon.modloader.events.registries.RegistryEvent;
import org.legendofdragoon.modloader.registries.MutableRegistry;

public final class RegisterWorldMapSoundsEvent extends RegistryEvent.Register<WorldMapSoundEntry> {
  public RegisterWorldMapSoundsEvent(final MutableRegistry<WorldMapSoundEntry> registry) {
    super(registry);
  }
}
