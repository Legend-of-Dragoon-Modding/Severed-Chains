package legend.game.wmap.registries;

import org.legendofdragoon.modloader.registries.MutableRegistry;
import org.legendofdragoon.modloader.registries.RegistryId;

public final class WorldMapStoryPresetRegistry extends MutableRegistry<WorldMapStoryPresetEntry> {
  public WorldMapStoryPresetRegistry() {
    super(new RegistryId("lod_core", "world_map_story_presets"));
  }
}
