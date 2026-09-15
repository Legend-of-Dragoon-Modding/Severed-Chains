package legend.game.wmap.world;

import javax.annotation.Nullable;

/** Resolved adapter position. Resolution does not change progression or visited state. */
public record WorldMapTravelPosition(WorldMapPortal portal, WorldMapRoute route, int dot, float offset, int facing, double distance) {
  /** Compatibility constructor for callers that only have a legacy cursor. */
  public WorldMapTravelPosition(final WorldMapPortal portal, final WorldMapRoute route, final int dot, final float offset, final int facing) {
    this(portal, route, dot, offset, facing, Double.NaN);
  }

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
    final WorldMapRouteMetric metric = new WorldMapRouteMetric(definition.geometry().get(route.segmentIndex()));
    final double distance = metric.clamp(metric.length() * (route.direction() > 0 ? progress : 1.0 - progress));
    final WorldMapRouteMetric.Cursor cursor = metric.cursor(distance);
    // The compatibility cursor remains serializable; distance preserves the exact endpoint.
    return new WorldMapTravelPosition(portal, route, cursor.index(), Math.min(cursor.offset(), Math.nextDown(4.0f)), route.direction(), distance);
  }
}
