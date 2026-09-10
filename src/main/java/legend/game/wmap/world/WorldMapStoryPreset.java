package legend.game.wmap.world;

import org.legendofdragoon.modloader.registries.RegistryId;

import javax.annotation.Nullable;
import java.util.List;

/** Legacy story-flag availability preset and optional world-map objective marker. */
public record WorldMapStoryPreset(int order, int storyFlag, List<RegistryId> enabledPortals, int x, int y, @Nullable RegistryId place) {
  public WorldMapStoryPreset {
    enabledPortals = List.copyOf(enabledPortals);
  }
}
