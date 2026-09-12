package legend.game.wmap.world;

import javax.annotation.Nullable;

/** Resolved adapter position. Resolution does not change progression or visited state. */
public record WorldMapTravelPosition(WorldMapPortal portal, WorldMapRoute route, int dot, float offset, int facing) {
  @Nullable
  public static WorldMapTravelPosition resolve(final WorldMapTravelTarget target, final WorldMapDefinition definition, final WorldMapView view, final boolean respectAccess) {
    if(target instanceof WorldMapTravelTarget.Portal portalTarget) {
      final WorldMapPortal portal = definition.portal(portalTarget.id());
      if(portal.route() == null) {
        throw new IllegalArgumentException("World map travel portal has no route: " + portal.id());
      }
      return allowed(portal, view, respectAccess) ? position(portal, definition.route(portal.route()), 0.0f, definition) : null;
    }

    if(target instanceof WorldMapTravelTarget.Node nodeTarget) {
      definition.node(nodeTarget.id());
    } else if(target instanceof WorldMapTravelTarget.Route routeTarget) {
      definition.route(routeTarget.id());
    }

    for(final WorldMapPortal portal : definition.portals()) {
      if(portal.route() == null || !allowed(portal, view, respectAccess)) {
        continue;
      }
      final WorldMapRoute route = definition.route(portal.route());
      if(target instanceof WorldMapTravelTarget.Route routeTarget && route.id().equals(routeTarget.id())) {
        return position(portal, route, routeTarget.progress(), definition);
      }
      if(target instanceof WorldMapTravelTarget.Node nodeTarget) {
        if(route.start().equals(nodeTarget.id())) {
          return position(portal, route, 0.0f, definition);
        }
        if(route.end().equals(nodeTarget.id())) {
          return position(portal, route, 1.0f, definition);
        }
      }
    }
    return null;
  }

  private static boolean allowed(final WorldMapPortal portal, final WorldMapView view, final boolean respectAccess) {
    return !respectAccess || view.access(portal.legacyIndex(), WorldMapAction.TRAVERSE).allowed();
  }

  private static WorldMapTravelPosition position(final WorldMapPortal portal, final WorldMapRoute route, final float progress, final WorldMapDefinition definition) {
    final var points = definition.geometry().get(route.segmentIndex());
    double length = 0.0;
    for(int i = 1; i < points.size(); i++) {
      length += distance(points.get(i - 1), points.get(i));
    }
    double remaining = length * (route.direction() > 0 ? progress : 1.0f - progress);
    for(int i = 0; i < points.size() - 1; i++) {
      final double interval = distance(points.get(i), points.get(i + 1));
      if(remaining < interval || i == points.size() - 2) {
        final float offset = interval == 0.0 ? 0.0f : (float)(remaining / interval * 4.0);
        // The movement adapter stores the final endpoint in its preceding interval.
        return new WorldMapTravelPosition(portal, route, i, Math.clamp(offset, 0.0f, Math.nextDown(4.0f)), route.direction());
      }
      remaining -= interval;
    }
    throw new IllegalArgumentException("World map travel route has no geometry: " + route.id());
  }

  private static double distance(final WorldMapPoint a, final WorldMapPoint b) {
    final double x = (double)a.x() - b.x();
    final double y = (double)a.y() - b.y();
    final double z = (double)a.z() - b.z();
    return Math.sqrt(x * x + y * y + z * z);
  }
}
