package legend.game.wmap.world;

import java.util.List;

/** Immutable geometry data retaining the legacy segment slot at the adapter boundary. */
public record WorldMapGeometry(int legacyIndex, List<WorldMapPoint> points) {
  public WorldMapGeometry {
    points = List.copyOf(points);
  }
}
