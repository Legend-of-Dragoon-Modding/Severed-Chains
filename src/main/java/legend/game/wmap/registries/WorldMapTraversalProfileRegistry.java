package legend.game.wmap.registries;

import org.legendofdragoon.modloader.registries.MutableRegistry;
import org.legendofdragoon.modloader.registries.RegistryId;

public final class WorldMapTraversalProfileRegistry extends MutableRegistry<WorldMapTraversalProfileEntry> {
  public WorldMapTraversalProfileRegistry() {
    super(new RegistryId("lod_core", "world_map_traversal_profiles"));
  }
}
