package legend.game.wmap.registries;

import org.legendofdragoon.modloader.events.registries.RegistryEvent;
import org.legendofdragoon.modloader.registries.MutableRegistry;

public final class RegisterWorldMapStoryPresetsEvent extends RegistryEvent.Register<WorldMapStoryPresetEntry> {
  public RegisterWorldMapStoryPresetsEvent(final MutableRegistry<WorldMapStoryPresetEntry> registry) {
    super(registry);
  }
}
