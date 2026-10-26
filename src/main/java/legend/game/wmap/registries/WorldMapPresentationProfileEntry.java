package legend.game.wmap.registries;

import legend.game.wmap.world.WorldMapPresentationProfile;
import org.legendofdragoon.modloader.registries.RegistryId;

import javax.annotation.Nullable;
import java.util.function.Function;

public class WorldMapPresentationProfileEntry extends WorldMapDataEntry<WorldMapPresentationProfile> {
  public WorldMapPresentationProfileEntry(final Function<RegistryId, WorldMapPresentationProfile> factory) {
    super(factory);
  }

  public WorldMapPresentationProfileEntry(@Nullable final RegistryId replaces, final int priority, final Function<RegistryId, WorldMapPresentationProfile> factory) {
    super(replaces, priority, factory);
  }
}
