package legend.game.wmap.registries;

import org.legendofdragoon.modloader.registries.MutableRegistry;
import org.legendofdragoon.modloader.registries.RegistryId;

public final class WorldMapTeleportLinkRegistry extends MutableRegistry<WorldMapTeleportLinkEntry> {
  public WorldMapTeleportLinkRegistry() {
    super(new RegistryId("lod_core", "world_map_teleport_links"));
  }
}
