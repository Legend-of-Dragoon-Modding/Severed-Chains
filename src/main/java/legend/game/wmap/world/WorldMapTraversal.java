package legend.game.wmap.world;

import legend.game.wmap.Continent;
import org.joml.Vector3f;
import org.legendofdragoon.modloader.registries.RegistryId;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Explicit endpoint adjacency, retaining retail candidate order and duplicate portal bindings. */
public final class WorldMapTraversal {
  public record Connection(int portalIndex, int routeIndex, int direction, Continent continent, WorldMapPoint nextPoint) { }

  private final Map<WorldMapPoint, List<Connection>> adjacency = new LinkedHashMap<>();
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
        // Retail checks the physical final endpoint before the first, irrespective of direction.
        final WorldMapPoint last = points.get(points.size() - 1);
        final WorldMapPoint first = points.getFirst();
        final WorldMapPoint next;
        if(matches(node.position(), last)) {
          next = points.get(points.size() - 2);
        } else if(matches(node.position(), first)) {
          next = points.get(1);
        } else {
          continue;
        }
        connections.add(new Connection(portal.legacyIndex(), route.legacyIndex(), route.direction(), portal.continent(), next));
      }
      this.adjacency.put(node.position(), List.copyOf(connections));
    }
  }

  public List<Connection> connections(final Vector3f position, final Continent continent, final int facing, final WorldMapView view) {
    final List<Connection> result = new ArrayList<>();
    final List<Connection> candidates = this.adjacency.get(new WorldMapPoint(position.x, position.y, position.z));
    if(candidates == null) {
      throw new IllegalArgumentException("World-map junction is not a defined endpoint: " + position);
    }
    for(final Connection connection : candidates) {
      if(connection.continent == continent && (facing == 0 || Integer.signum(facing) == connection.direction) && view.access(connection.portalIndex, WorldMapAction.TRAVERSE).allowed()) {
        result.add(connection);
      }
    }
    return List.copyOf(result);
  }

  private static boolean matches(final WorldMapPoint a, final WorldMapPoint b) {
    return Math.abs(a.x() - b.x()) < 0.00001f && Math.abs(a.y() - b.y()) < 0.00001f && Math.abs(a.z() - b.z()) < 0.00001f;
  }

  public List<Connection> connections(final Vector3f position, final RegistryId region, final int facing, final WorldMapView view) {
    final List<Connection> candidates = this.adjacency.get(new WorldMapPoint(position.x, position.y, position.z));
    if(candidates == null) {
      throw new IllegalArgumentException("World-map junction is not a defined endpoint: " + position);
    }
    return candidates.stream().filter(connection -> WorldMapRegion.idFor(this.definition.portal(connection.portalIndex())).equals(region)
      && (facing == 0 || Integer.signum(facing) == connection.direction())
      && view.access(connection.portalIndex(), WorldMapAction.TRAVERSE).allowed()).toList();
  }
}
