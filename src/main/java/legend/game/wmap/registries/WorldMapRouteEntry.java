package legend.game.wmap.registries;

import legend.game.wmap.world.WorldMapRouteData;
import org.legendofdragoon.modloader.registries.RegistryId;

import javax.annotation.Nullable;
import java.util.function.Function;

public final class WorldMapRouteEntry extends WorldMapDataEntry<WorldMapRouteData> {
  public WorldMapRouteEntry(final Function<RegistryId, WorldMapRouteData> factory) {
    super(factory);
  }

  public WorldMapRouteEntry(@Nullable final RegistryId replaces, final int priority, final Function<RegistryId, WorldMapRouteData> factory) {
    super(replaces, priority, factory);
  }
}
