package legend.game.wmap.registries;

import org.legendofdragoon.modloader.registries.MutableRegistry;
import org.legendofdragoon.modloader.registries.RegistryId;

public final class WorldMapAvatarRegistry extends MutableRegistry<WorldMapAvatarEntry> {
  public WorldMapAvatarRegistry() {
    super(new RegistryId("lod_core", "world_map_avatars"));
  }
}

