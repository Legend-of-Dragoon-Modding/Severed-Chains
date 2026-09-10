package legend.game.wmap.world;

import org.legendofdragoon.modloader.registries.RegistryId;

import java.util.Objects;

/** One directed legacy traversal of a physical world map path segment. */
public record WorldMapRoute(RegistryId id, int legacyIndex, RegistryId start, RegistryId end, int segmentIndex, int direction, int encounterRate, int battleStage, int encounterIndex, int modelIndex) {
  public WorldMapRoute {
    Objects.requireNonNull(id, "id");
    Objects.requireNonNull(start, "start");
    Objects.requireNonNull(end, "end");
  }
}
