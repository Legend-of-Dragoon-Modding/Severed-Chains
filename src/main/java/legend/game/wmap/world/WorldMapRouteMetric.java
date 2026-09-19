package legend.game.wmap.world;

import java.util.List;

/** Immutable arc-length index with a legacy dot-cursor adapter. */
public final class WorldMapRouteMetric {
  private final double[] distances;

  public WorldMapRouteMetric(final List<WorldMapPoint> points) {
    if(points.size() < 2) throw new IllegalArgumentException("World map route requires two points");
    this.distances = new double[points.size()];
    for(int i = 1; i < points.size(); i++) {
      final WorldMapPoint a = points.get(i - 1);
      final WorldMapPoint b = points.get(i);
      final double dx = (double)b.x() - a.x();
      final double dy = (double)b.y() - a.y();
      final double dz = (double)b.z() - a.z();
      this.distances[i] = this.distances[i - 1] + Math.sqrt(dx * dx + dy * dy + dz * dz);
    }
    if(!Double.isFinite(this.length()) || this.length() <= 0.0) throw new IllegalArgumentException("World map route requires finite nonzero length");
  }

  public double length() {
    return this.distances[this.distances.length - 1];
  }

  public double clamp(final double distance) {
    if(!Double.isFinite(distance)) throw new IllegalArgumentException("World map distance must be finite");
    return Math.clamp(distance, 0.0, this.length());
  }

  public double distance(final int index, final float offset) {
    if(!Float.isFinite(offset)) throw new IllegalArgumentException("World map cursor must be finite");
    if(index >= this.distances.length - 1) return this.length();
    final int interval = Math.max(0, index);
    return this.distances[interval] + (this.distances[interval + 1] - this.distances[interval]) * Math.clamp(offset, 0.0f, 4.0f) / 4.0;
  }

  public float progress(final int index, final float offset) {
    return (float)(this.distance(index, offset) / this.length());
  }

  /** Exact endpoint cursors are safe for interpolation; save adapters may cap offset separately. */
  public Cursor cursor(final double distance) {
    final double position = this.clamp(distance);
    if(position == 0.0) return new Cursor(0, 0.0f, 0);
    if(position == this.length()) return new Cursor(this.distances.length - 2, 4.0f, 0);
    int low = 1;
    int high = this.distances.length - 1;
    // Upper bound skips zero-length intervals, including repeated interior vertices.
    while(low < high) {
      final int middle = (low + high) >>> 1;
      if(this.distances[middle] <= position) low = middle + 1;
      else high = middle;
    }
    final int interval = low - 1;
    return new Cursor(interval, (float)((position - this.distances[interval]) / (this.distances[low] - this.distances[interval]) * 4.0), 0);
  }

  /** Compatibility helper; authored movement should retain distance between calls. */
  public Cursor move(final int index, final float offset, final double movement) {
    if(!Double.isFinite(movement)) throw new IllegalArgumentException("World map movement must be finite");
    final double position = this.distance(index, offset) + movement;
    final Cursor cursor = this.cursor(position);
    return new Cursor(cursor.index(), cursor.offset(), position <= 0.0 && movement < 0 ? -1 : position >= this.length() && movement > 0 ? 1 : 0);
  }

  public record Cursor(int index, float offset, int endpoint) { }
}
