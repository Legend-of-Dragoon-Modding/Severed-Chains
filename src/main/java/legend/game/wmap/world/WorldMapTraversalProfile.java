package legend.game.wmap.world;

import legend.game.modding.events.worldmap.WorldMapTraversalEvent;
import org.legendofdragoon.modloader.registries.RegistryId;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Route traversal hooks, applied in ascending priority and then registry-ID order. An empty route
 * set applies to every route. Handlers compose by modifying the same TICK event; later handlers see
 * earlier changes. Store mutable gameplay state on the campaign/engine, not in this shared recipe.
 */
public record WorldMapTraversalProfile(int priority, Set<RegistryId> routes, List<Marker> markers, Consumer<WorldMapTraversalEvent> handler) {
  /**
   * A crossing point measured along the geometry's point intervals: zero is its first point, one is
   * its last point, regardless of route direction. This is not a fraction of physical path length.
   * A marker fires once per actual crossing in either direction; arriving/warping past it does not fire.
   */
  public record Marker(RegistryId id, float progress) {
    public Marker {
      Objects.requireNonNull(id, "id");
      if(!Float.isFinite(progress) || progress < 0.0f || progress > 1.0f) {
        throw new IllegalArgumentException("World map traversal marker progress must be between zero and one");
      }
    }
  }

  public WorldMapTraversalProfile(final int priority, final Set<RegistryId> routes, final Consumer<WorldMapTraversalEvent> handler) {
    this(priority, routes, List.of(), handler);
  }

  public WorldMapTraversalProfile {
    routes = Set.copyOf(routes);
    markers = List.copyOf(markers);
    Objects.requireNonNull(handler, "handler");
    final Set<RegistryId> ids = new HashSet<>();
    for(final Marker marker : markers) {
      if(!ids.add(marker.id())) {
        throw new IllegalArgumentException("Duplicate world map traversal marker " + marker.id());
      }
    }
  }

  public boolean appliesTo(final WorldMapRoute route) {
    return this.routes.isEmpty() || this.routes.contains(route.id());
  }
}
