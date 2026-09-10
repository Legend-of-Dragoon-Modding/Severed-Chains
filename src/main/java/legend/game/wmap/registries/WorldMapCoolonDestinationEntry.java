package legend.game.wmap.registries;

import legend.game.wmap.world.WorldMapCoolonDestination;
import org.legendofdragoon.modloader.registries.RegistryId;

import javax.annotation.Nullable;
import java.util.function.Function;

public final class WorldMapCoolonDestinationEntry extends WorldMapDataEntry<WorldMapCoolonDestination> {
  public WorldMapCoolonDestinationEntry(final Function<RegistryId, WorldMapCoolonDestination> factory) {
    super(factory);
  }

  public WorldMapCoolonDestinationEntry(@Nullable final RegistryId replaces, final int priority, final Function<RegistryId, WorldMapCoolonDestination> factory) {
    super(replaces, priority, factory);
  }
}
