package legend.game.wmap.registries;

import legend.game.wmap.world.WorldMapSound;
import org.legendofdragoon.modloader.registries.RegistryId;

import javax.annotation.Nullable;
import java.util.function.Function;

public final class WorldMapSoundEntry extends WorldMapDataEntry<WorldMapSound> {
  public WorldMapSoundEntry(final Function<RegistryId, WorldMapSound> factory) {
    super(factory);
  }

  public WorldMapSoundEntry(@Nullable final RegistryId replaces, final int priority, final Function<RegistryId, WorldMapSound> factory) {
    super(replaces, priority, factory);
  }
}
