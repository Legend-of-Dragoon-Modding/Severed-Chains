package legend.game.wmap.world;

import org.legendofdragoon.modloader.registries.RegistryId;

import javax.annotation.Nullable;
import java.util.List;

/** Legacy story-flag availability preset and optional world-map objective marker. */
public record WorldMapStoryPreset(int order, int storyFlag, List<RegistryId> enabledPortals, int x, int y, @Nullable RegistryId place, Composition composition) {
  public enum Composition { REPLACE, ENABLE, DISABLE }

  public WorldMapStoryPreset(final int order, final int storyFlag, final List<RegistryId> enabledPortals, final int x, final int y, @Nullable final RegistryId place) {
    this(order, storyFlag, enabledPortals, x, y, place, Composition.REPLACE);
  }

  public WorldMapStoryPreset {
    enabledPortals = List.copyOf(enabledPortals);
    if(composition == null) composition = Composition.REPLACE;
  }
}
