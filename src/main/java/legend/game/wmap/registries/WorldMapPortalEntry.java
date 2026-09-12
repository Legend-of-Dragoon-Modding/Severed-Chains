package legend.game.wmap.registries;

import legend.game.wmap.world.WorldMapPortal;
import org.legendofdragoon.modloader.registries.RegistryId;

import javax.annotation.Nullable;
import java.util.function.Function;

public final class WorldMapPortalEntry extends WorldMapDataEntry<WorldMapPortal> {
  public WorldMapPortalEntry(final Function<RegistryId, WorldMapPortal> factory) {
    super(factory);
  }

  public WorldMapPortalEntry(@Nullable final RegistryId replaces, final int priority, final Function<RegistryId, WorldMapPortal> factory) {
    super(replaces, priority, factory);
  }
}
