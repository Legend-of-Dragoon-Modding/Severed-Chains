package legend.game.wmap.registries;

import org.legendofdragoon.modloader.registries.MutableRegistry;
import org.legendofdragoon.modloader.registries.RegistryId;

public final class WorldMapCoolonDestinationRegistry extends MutableRegistry<WorldMapCoolonDestinationEntry> {
  public WorldMapCoolonDestinationRegistry() {
    super(new RegistryId("lod_core", "world_map_coolon_destinations"));
  }
}
