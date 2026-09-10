package legend.game.wmap.registries;

import legend.game.wmap.world.WorldMapGeometry;
import org.legendofdragoon.modloader.registries.RegistryId;

import javax.annotation.Nullable;
import java.util.function.Function;

public final class WorldMapGeometryEntry extends WorldMapDataEntry<WorldMapGeometry> {
  public WorldMapGeometryEntry(final Function<RegistryId, WorldMapGeometry> factory) {
    super(factory);
  }

  public WorldMapGeometryEntry(@Nullable final RegistryId replaces, final int priority, final Function<RegistryId, WorldMapGeometry> factory) {
    super(replaces, priority, factory);
  }
}
