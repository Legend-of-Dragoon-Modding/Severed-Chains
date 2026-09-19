package legend.game.wmap.registries;

import legend.game.wmap.world.WorldMapTraversalProfile;
import org.legendofdragoon.modloader.registries.RegistryId;

import javax.annotation.Nullable;
import java.util.function.Function;

public final class WorldMapTraversalProfileEntry extends WorldMapDataEntry<WorldMapTraversalProfile> {
  public WorldMapTraversalProfileEntry(final Function<RegistryId, WorldMapTraversalProfile> factory) {
    super(factory);
  }

  public WorldMapTraversalProfileEntry(@Nullable final RegistryId replaces, final int priority, final Function<RegistryId, WorldMapTraversalProfile> factory) {
    super(replaces, priority, factory);
  }
}
