package legend.game.wmap.registries;

import org.legendofdragoon.modloader.registries.MutableRegistry;
import org.legendofdragoon.modloader.registries.RegistryId;

public final class WorldMapPortalRegistry extends MutableRegistry<WorldMapPortalEntry> {
  public WorldMapPortalRegistry() {
    super(new RegistryId("lod_core", "world_map_portals"));
  }
}
