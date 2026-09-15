package legend.game.wmap.world;

import org.legendofdragoon.modloader.registries.RegistryId;

import java.util.Objects;

/** Authored traversal owns route identity and exact distance; dot fields are an adapter only. */
public final class WorldMapTraversalPosition {
  private RegistryId route;
  private WorldMapRouteMetric metric;
  private double distance;
  private WorldMapRouteMetric.Cursor exported;

  /** Import only on route/geometry changes or an actual write to the compatibility cursor. */
  public void synchronize(final RegistryId route, final WorldMapRouteMetric metric, final int dot, final float offset) {
    if(!Objects.equals(this.route, route) || this.metric != metric || this.exported == null || this.exported.index() != dot || Float.compare(this.exported.offset(), offset) != 0) {
      this.set(route, metric, metric.distance(dot, offset));
      this.exported = new WorldMapRouteMetric.Cursor(dot, offset, 0);
    }
  }

  public void set(final RegistryId route, final WorldMapRouteMetric metric, final double distance) {
    this.route = Objects.requireNonNull(route, "route");
    this.metric = Objects.requireNonNull(metric, "metric");
    this.distance = metric.clamp(distance);
    this.exported = metric.cursor(this.distance);
  }

  public RegistryId route() {
    return this.route;
  }

  public double distance() {
    return this.distance;
  }

  public float progress() {
    return (float)(this.distance / this.metric.length());
  }

  /** Endpoint signaling derives from exact distance, independently of float cursor rounding. */
  public WorldMapRouteMetric.Cursor advance(final double movement) {
    if(!Double.isFinite(movement)) throw new IllegalArgumentException("World map movement must be finite");
    final double next = this.distance + movement;
    this.distance = this.metric.clamp(next);
    final WorldMapRouteMetric.Cursor cursor = this.metric.cursor(this.distance);
    this.exported = new WorldMapRouteMetric.Cursor(cursor.index(), cursor.offset(), next <= 0.0 && movement < 0 ? -1 : next >= this.metric.length() && movement > 0 ? 1 : 0);
    return this.exported;
  }

  public WorldMapRouteMetric.Cursor cursor() {
    return this.exported;
  }
}
