package legend.game.wmap.registries;

import legend.game.wmap.world.WorldMapSubmapDestination;
import org.legendofdragoon.modloader.registries.RegistryId;

import javax.annotation.Nullable;
import java.util.function.Function;

public final class WorldMapSubmapDestinationEntry extends WorldMapDataEntry<WorldMapSubmapDestination> {
  public WorldMapSubmapDestinationEntry(final Function<RegistryId, WorldMapSubmapDestination> factory) {
    super(factory);
  }

  public WorldMapSubmapDestinationEntry(@Nullable final RegistryId replaces, final int priority, final Function<RegistryId, WorldMapSubmapDestination> factory) {
    super(replaces, priority, factory);
  }
}
