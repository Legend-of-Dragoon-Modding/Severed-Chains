package legend.game.wmap.registries;

import org.legendofdragoon.modloader.registries.MutableRegistry;
import org.legendofdragoon.modloader.registries.RegistryId;

public final class WorldMapRegionRegistry extends MutableRegistry<WorldMapRegionEntry> {
  public WorldMapRegionRegistry() {
    super(new RegistryId("lod_core", "world_map_regions"));
  }
}
