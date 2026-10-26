package legend.game.wmap.registries;

import legend.game.wmap.world.WorldMapTeleportLink;
import org.legendofdragoon.modloader.registries.RegistryId;

import javax.annotation.Nullable;
import java.util.function.Function;

public final class WorldMapTeleportLinkEntry extends WorldMapDataEntry<WorldMapTeleportLink> {
  public WorldMapTeleportLinkEntry(final Function<RegistryId, WorldMapTeleportLink> factory) {
    super(factory);
  }

  public WorldMapTeleportLinkEntry(@Nullable final RegistryId replaces, final int priority, final Function<RegistryId, WorldMapTeleportLink> factory) {
    super(replaces, priority, factory);
  }
}
