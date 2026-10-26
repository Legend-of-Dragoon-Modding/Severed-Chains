package legend.game.wmap.registries;

import legend.game.wmap.world.WorldMapRegion;
import org.legendofdragoon.modloader.registries.RegistryId;

import javax.annotation.Nullable;
import java.util.function.Function;

public final class WorldMapRegionEntry extends WorldMapDataEntry<WorldMapRegion> {
  public WorldMapRegionEntry(final Function<RegistryId, WorldMapRegion> factory) {
    super(factory);
  }

  public WorldMapRegionEntry(@Nullable final RegistryId replaces, final int priority, final Function<RegistryId, WorldMapRegion> factory) {
    super(replaces, priority, factory);
  }
}
