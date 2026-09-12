package legend.game.wmap.world;

import legend.game.wmap.Continent;
import org.joml.Vector3f;
import org.legendofdragoon.modloader.registries.RegistryId;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/** Node-identity adjacency. Portal order and duplicate bindings retain the retail candidate order. */
public final class WorldMapTraversal {
  public record Connection(int portalIndex, int routeIndex, int direction, Continent continent, WorldMapPoint nextPoint) { }

  private final Map<RegistryId, List<Connection>> adjacency = new LinkedHashMap<>();
  private final WorldMapDefinition definition;

  public WorldMapTraversal(final WorldMapDefinition definition) {
    this.definition = definition;
    for(final WorldMapNode node : definition.nodes()) {
      final List<Connection> connections = new ArrayList<>();
      for(final WorldMapPortal portal : definition.portals()) {
        if(portal.route() == null || portal.junctionIndex() == -1) {
          continue;
        }
        final WorldMapRoute route = definition.route(portal.route());
        final var points = definition.geometry().get(route.segmentIndex());
        final RegistryId first = route.direction() > 0 ? route.start() : route.end();
        final RegistryId last = route.direction() > 0 ? route.end() : route.start();
        final WorldMapPoint next;
        // Last endpoint takes precedence for a loop, matching retail ordering.
        if(node.id().equals(last)) {
          next = points.get(points.size() - 2);
        } else if(node.id().equals(first)) {
          next = points.get(1);
        } else {
          continue;
        }
        connections.add(new Connection(portal.legacyIndex(), route.legacyIndex(), route.direction(), portal.continent(), next));
      }
      this.adjacency.put(node.id(), List.copyOf(connections));
    }
  }

  /** Resolve from the active route, never from a global position search. */
  public RegistryId endpoint(final WorldMapRoute route, final boolean lastGeometryPoint) {
    return lastGeometryPoint == (route.direction() > 0) ? route.end() : route.start();
  }

  public List<Connection> connections(final RegistryId node, final RegistryId region, final int facing, final WorldMapView view) {
    return this.connections(node, connection -> WorldMapRegion.idFor(this.definition.portal(connection.portalIndex())).equals(region), facing, view);
  }

  private List<Connection> connections(final RegistryId node, final Predicate<Connection> region, final int facing, final WorldMapView view) {
    this.definition.node(node);
    return this.adjacency.get(node).stream()
      .filter(region)
      .filter(connection -> (facing == 0 || Integer.signum(facing) == connection.direction()) && view.access(connection.portalIndex(), WorldMapAction.TRAVERSE).allowed())
      .toList();
  }

  /**
   * Compatibility query for callers without an active route. Ambiguous co-located nodes are
   * rejected instead of silently joining independent paths. Prefer the node-ID overload.
   */
  @Deprecated
  public List<Connection> connections(final Vector3f position, final RegistryId region, final int facing, final WorldMapView view) {
    final Predicate<Connection> belongs = connection -> WorldMapRegion.idFor(this.definition.portal(connection.portalIndex())).equals(region);
    return this.connections(this.uniqueNode(position, belongs), belongs, facing, view);
  }

  @Deprecated
  public List<Connection> connections(final Vector3f position, final Continent continent, final int facing, final WorldMapView view) {
    final Predicate<Connection> belongs = connection -> connection.continent() == continent;
    return this.connections(this.uniqueNode(position, belongs), belongs, facing, view);
  }

  private RegistryId uniqueNode(final Vector3f position, final Predicate<Connection> belongs) {
    RegistryId selected = null;
    for(final WorldMapNode node : this.definition.nodes()) {
      if(matches(node.position(), position) && this.adjacency.get(node.id()).stream().anyMatch(belongs)) {
        if(selected != null) {
          throw new IllegalArgumentException("Ambiguous world map junction at " + position + ": " + selected + " and " + node.id() + "; query by node ID");
        }
        selected = node.id();
      }
    }
    if(selected == null) {
      throw new IllegalArgumentException("World-map junction has no node in the requested region at " + position);
    }
    return selected;
  }

  private static boolean matches(final WorldMapPoint point, final Vector3f position) {
    return Math.abs(point.x() - position.x) < 0.0001f && Math.abs(point.y() - position.y) < 0.0001f && Math.abs(point.z() - position.z) < 0.0001f;
  }
}
