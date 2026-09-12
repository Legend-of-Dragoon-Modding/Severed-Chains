package legend.game.wmap.registries;

import org.legendofdragoon.modloader.registries.MutableRegistry;
import org.legendofdragoon.modloader.registries.RegistryId;

public final class WorldMapPlaceRegistry extends MutableRegistry<WorldMapPlaceEntry> {
  public WorldMapPlaceRegistry() {
    super(new RegistryId("lod_core", "world_map_places"));
  }
}
