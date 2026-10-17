package legend.game.wmap.registries;

import org.legendofdragoon.modloader.registries.MutableRegistry;
import org.legendofdragoon.modloader.registries.RegistryId;

public final class WorldMapNodeRegistry extends MutableRegistry<WorldMapNodeEntry> {
  public WorldMapNodeRegistry() {
    super(new RegistryId("lod_core", "world_map_nodes"));
  }
}
