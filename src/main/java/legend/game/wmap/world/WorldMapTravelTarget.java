package legend.game.wmap.world;

import org.legendofdragoon.modloader.registries.RegistryId;

import java.util.Objects;

/** A destination on the world map, independent of submap cuts and scenes. */
public sealed interface WorldMapTravelTarget {
  /** Physical distance fraction, measured from the directed route's start to its end. */
  static Route atRouteDistance(final RegistryId route, final float distanceFraction) {
    return new Route(route, distanceFraction);
  }

  static Route atRouteDistance(final RegistryId route, final double distanceFraction) {
    return new Route(route, (float)distanceFraction, distanceFraction);
  }
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
  record Route(RegistryId id, float progress, double preciseProgress) implements WorldMapTravelTarget {
    public Route(final RegistryId id, final float progress) {
      this(id, progress, progress);
    }

    public Route {
      Objects.requireNonNull(id, "id");
      if(!Float.isFinite(progress) || progress < 0.0f || progress > 1.0f) {
        throw new IllegalArgumentException("World map travel progress must be between zero and one");
      }
      if(!Double.isFinite(preciseProgress) || preciseProgress < 0.0 || preciseProgress > 1.0 || Float.compare(progress, (float)preciseProgress) != 0) {
        throw new IllegalArgumentException("Precise world map progress must match its compatibility value");
      }
    }
  }
}
