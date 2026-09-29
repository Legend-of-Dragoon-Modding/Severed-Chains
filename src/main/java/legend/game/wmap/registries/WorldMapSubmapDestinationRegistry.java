package legend.game.wmap.registries;

import org.legendofdragoon.modloader.registries.MutableRegistry;
import org.legendofdragoon.modloader.registries.RegistryId;

public final class WorldMapSubmapDestinationRegistry extends MutableRegistry<WorldMapSubmapDestinationEntry> {
  public WorldMapSubmapDestinationRegistry() {
    super(new RegistryId("lod_core", "world_map_submap_destinations"));
  }
}
