package legend.game.wmap.registries;

import legend.game.wmap.world.WorldMapNode;
import org.legendofdragoon.modloader.registries.RegistryId;

import javax.annotation.Nullable;
import java.util.function.Function;

public final class WorldMapNodeEntry extends WorldMapDataEntry<WorldMapNode> {
  public WorldMapNodeEntry(final Function<RegistryId, WorldMapNode> factory) {
    super(factory);
  }

  public WorldMapNodeEntry(@Nullable final RegistryId replaces, final int priority, final Function<RegistryId, WorldMapNode> factory) {
    super(replaces, priority, factory);
  }
}
