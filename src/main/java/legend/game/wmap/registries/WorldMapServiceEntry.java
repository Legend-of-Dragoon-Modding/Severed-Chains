package legend.game.wmap.registries;

import legend.game.wmap.world.WorldMapService;
import org.legendofdragoon.modloader.registries.RegistryId;

import javax.annotation.Nullable;
import java.util.function.Function;

public final class WorldMapServiceEntry extends WorldMapDataEntry<WorldMapService> {
  public WorldMapServiceEntry(final Function<RegistryId, WorldMapService> factory) {
    super(factory);
  }

  public WorldMapServiceEntry(@Nullable final RegistryId replaces, final int priority, final Function<RegistryId, WorldMapService> factory) {
    super(replaces, priority, factory);
  }
}
