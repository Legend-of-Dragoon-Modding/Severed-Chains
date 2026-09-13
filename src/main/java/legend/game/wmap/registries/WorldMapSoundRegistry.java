package legend.game.wmap.registries;

import org.legendofdragoon.modloader.registries.MutableRegistry;
import org.legendofdragoon.modloader.registries.RegistryId;

public final class WorldMapSoundRegistry extends MutableRegistry<WorldMapSoundEntry> {
  public WorldMapSoundRegistry() {
    super(new RegistryId("lod_core", "world_map_sounds"));
  }
}
