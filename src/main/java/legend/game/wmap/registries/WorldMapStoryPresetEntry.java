package legend.game.wmap.registries;

import legend.game.wmap.world.WorldMapStoryPreset;
import org.legendofdragoon.modloader.registries.RegistryId;

import javax.annotation.Nullable;
import java.util.function.Function;

public final class WorldMapStoryPresetEntry extends WorldMapDataEntry<WorldMapStoryPreset> {
  public WorldMapStoryPresetEntry(final Function<RegistryId, WorldMapStoryPreset> factory) {
    super(factory);
  }

  public WorldMapStoryPresetEntry(@Nullable final RegistryId replaces, final int priority, final Function<RegistryId, WorldMapStoryPreset> factory) {
    super(replaces, priority, factory);
  }
}
