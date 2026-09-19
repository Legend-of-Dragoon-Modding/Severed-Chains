package legend.game.wmap.registries;

import legend.game.wmap.world.WorldMapThumbnail;
import org.legendofdragoon.modloader.registries.RegistryId;

import javax.annotation.Nullable;
import java.util.function.Function;

public final class WorldMapThumbnailEntry extends WorldMapDataEntry<WorldMapThumbnail> {
  public WorldMapThumbnailEntry(final Function<RegistryId, WorldMapThumbnail> factory) {
    super(factory);
  }

  public WorldMapThumbnailEntry(@Nullable final RegistryId replaces, final int priority, final Function<RegistryId, WorldMapThumbnail> factory) {
    super(replaces, priority, factory);
  }
}
