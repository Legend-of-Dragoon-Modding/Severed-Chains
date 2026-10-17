package legend.game.wmap.registries;

import legend.game.wmap.world.WorldMapAvatar;
import org.legendofdragoon.modloader.registries.RegistryId;

import javax.annotation.Nullable;
import java.util.function.Function;

public final class WorldMapAvatarEntry extends WorldMapDataEntry<WorldMapAvatar> {
  public WorldMapAvatarEntry(final Function<RegistryId, WorldMapAvatar> factory) {
    super(factory);
  }

  public WorldMapAvatarEntry(@Nullable final RegistryId replaces, final int priority, final Function<RegistryId, WorldMapAvatar> factory) {
    super(replaces, priority, factory);
  }
}

