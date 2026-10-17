package legend.game.wmap.registries;

import legend.game.wmap.world.WorldMapPlace;
import org.legendofdragoon.modloader.registries.RegistryId;

import javax.annotation.Nullable;
import java.util.function.Function;

public final class WorldMapPlaceEntry extends WorldMapDataEntry<WorldMapPlace> {
  public WorldMapPlaceEntry(final Function<RegistryId, WorldMapPlace> factory) {
    super(factory);
  }

  public WorldMapPlaceEntry(@Nullable final RegistryId replaces, final int priority, final Function<RegistryId, WorldMapPlace> factory) {
    super(replaces, priority, factory);
  }
}
