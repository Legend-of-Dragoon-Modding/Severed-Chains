package legend.game.wmap.world;

import org.legendofdragoon.modloader.registries.RegistryId;

import java.util.Objects;

/** A destination on the world map, independent of submap cuts and scenes. */
public sealed interface WorldMapTravelTarget {
  /** Arrive at the start of the portal's directed route. */
  record Portal(RegistryId id) implements WorldMapTravelTarget {
    public Portal {
      Objects.requireNonNull(id, "id");
    }
  }

  /** Arrive at a node using the first permitted portal-bound route at that node. */
  record Node(RegistryId id) implements WorldMapTravelTarget {
    public Node {
      Objects.requireNonNull(id, "id");
    }
  }

  /** Progress is distance along the directed route, from zero at its start to one at its end. */
  record Route(RegistryId id, float progress) implements WorldMapTravelTarget {
    public Route {
      Objects.requireNonNull(id, "id");
      if(!Float.isFinite(progress) || progress < 0.0f || progress > 1.0f) {
        throw new IllegalArgumentException("World map travel progress must be between zero and one");
      }
    }
  }
}
