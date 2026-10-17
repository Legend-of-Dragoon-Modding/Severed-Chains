package legend.game.wmap.registries;

import org.legendofdragoon.modloader.registries.MutableRegistry;
import org.legendofdragoon.modloader.registries.RegistryId;

public final class WorldMapThumbnailRegistry extends MutableRegistry<WorldMapThumbnailEntry> {
  public WorldMapThumbnailRegistry() {
    super(new RegistryId("lod_core", "world_map_thumbnails"));
  }
}
