package legend.game.wmap.registries;

import org.legendofdragoon.modloader.registries.MutableRegistry;
import org.legendofdragoon.modloader.registries.RegistryId;

public class WorldMapPresentationProfileRegistry extends MutableRegistry<WorldMapPresentationProfileEntry> {
  public WorldMapPresentationProfileRegistry() {
    super(new RegistryId("lod_core", "world_map_presentation_profiles"));
  }
}
