package legend.game.wmap.world;

import java.util.List;

/** Arc-length calculations; the legacy dot cursor remains only the interpolation adapter. */
public final class WorldMapRouteMetric {
  private final double[] distances;

  public WorldMapRouteMetric(final List<WorldMapPoint> points) {
    this.distances = new double[points.size()];
    for(int i = 1; i < points.size(); i++) {
      final WorldMapPoint a = points.get(i - 1);
      final WorldMapPoint b = points.get(i);
      final double dx = (double)b.x() - a.x();
      final double dy = (double)b.y() - a.y();
      final double dz = (double)b.z() - a.z();
      this.distances[i] = this.distances[i - 1] + Math.sqrt(dx * dx + dy * dy + dz * dz);
    }
    if(this.length() <= 0.0) throw new IllegalArgumentException("World map route requires nonzero length");
  }

  public double length() { return this.distances[this.distances.length - 1]; }

  public double distance(final int index, final float offset) {
    return this.distances[index] + (this.distances[index + 1] - this.distances[index]) * offset / 4.0;
  }

  public float progress(final int index, final float offset) {
    return (float)Math.clamp(this.distance(index, offset) / this.length(), 0.0, 1.0);
  }

  public Cursor move(final int index, final float offset, final double movement) {
    final double distance = this.distance(index, offset) + movement;
    if(distance <= 0.0) return new Cursor(0, 0.0f, movement < 0 ? -1 : 0);
    if(distance >= this.length()) return new Cursor(this.distances.length - 2, 4.0f, movement > 0 ? 1 : 0);
    for(int i = 0; i < this.distances.length - 1; i++) {
      if(distance < this.distances[i + 1]) {
        return new Cursor(i, (float)((distance - this.distances[i]) / (this.distances[i + 1] - this.distances[i]) * 4.0), 0);
      }
    }
    throw new IllegalStateException("World map distance cannot be resolved");
  }

  public record Cursor(int index, float offset, int endpoint) { }
}
