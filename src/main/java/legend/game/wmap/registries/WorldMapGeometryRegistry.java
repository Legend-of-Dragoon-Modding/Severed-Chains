package legend.game.wmap.registries;

import org.legendofdragoon.modloader.registries.MutableRegistry;
import org.legendofdragoon.modloader.registries.RegistryId;

public final class WorldMapGeometryRegistry extends MutableRegistry<WorldMapGeometryEntry> {
  public WorldMapGeometryRegistry() {
    super(new RegistryId("lod_core", "world_map_geometry"));
  }
}
