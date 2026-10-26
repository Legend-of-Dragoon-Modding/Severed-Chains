package legend.game.wmap.world;

import java.util.List;

/** Immutable geometry data retaining the legacy segment slot at the adapter boundary. */
public record WorldMapGeometry(int legacyIndex, List<WorldMapPoint> points, Motion motion, float unitsPerStep) {
  public enum Motion { LEGACY_INTERVAL, DISTANCE }

  /** Numeric-slot constructors preserve retail movement and older mod binaries. */
  public WorldMapGeometry(final int legacyIndex, final List<WorldMapPoint> points) {
    this(legacyIndex, points, Motion.LEGACY_INTERVAL, 1.0f);
  }

  public WorldMapGeometry(final List<WorldMapPoint> points) {
    this(-1, points, Motion.DISTANCE, 1.0f);
  }

  public WorldMapGeometry withLegacyIndex(final int legacyIndex) {
    return new WorldMapGeometry(legacyIndex, this.points, this.motion, this.unitsPerStep);
  }

  public WorldMapGeometry {
    points = List.copyOf(points);
    // Older preset records omit these fields.
    if(motion == null) motion = Motion.LEGACY_INTERVAL;
    if(unitsPerStep == 0.0f) unitsPerStep = 1.0f;
    if(!Float.isFinite(unitsPerStep) || unitsPerStep < 0.0f) throw new IllegalArgumentException("World map movement units must be positive and finite");
  }
}
