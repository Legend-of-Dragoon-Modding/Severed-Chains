package legend.game.wmap.world;

import java.util.List;

/** Immutable geometry data retaining the legacy segment slot at the adapter boundary. */
public record WorldMapGeometry(int legacyIndex, List<WorldMapPoint> points) {
  public WorldMapGeometry(final List<WorldMapPoint> points) {
    this(-1, points);
  }

  public WorldMapGeometry withLegacyIndex(final int legacyIndex) {
    return new WorldMapGeometry(legacyIndex, this.points);
  }

  public WorldMapGeometry {
    points = List.copyOf(points);
  }
}
