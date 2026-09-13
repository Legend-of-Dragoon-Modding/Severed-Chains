package legend.game.wmap.registries;

import org.legendofdragoon.modloader.registries.MutableRegistry;
import org.legendofdragoon.modloader.registries.RegistryId;

public final class WorldMapServiceRegistry extends MutableRegistry<WorldMapServiceEntry> {
  public WorldMapServiceRegistry() {
    super(new RegistryId("lod_core", "world_map_services"));
  }
}
