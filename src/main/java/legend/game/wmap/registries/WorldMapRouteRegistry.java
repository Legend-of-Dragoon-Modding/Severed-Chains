package legend.game.wmap.registries;

import org.legendofdragoon.modloader.registries.MutableRegistry;
import org.legendofdragoon.modloader.registries.RegistryId;

public final class WorldMapRouteRegistry extends MutableRegistry<WorldMapRouteEntry> {
  public WorldMapRouteRegistry() {
    super(new RegistryId("lod_core", "world_map_routes"));
  }
}
