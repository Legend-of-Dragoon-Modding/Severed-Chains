package legend.game.wmap.world;

import javax.annotation.Nullable;
import java.util.Objects;

/** Region camera defaults; bounds clamp the local camera target, never the player's route position. */
public record WorldMapCameraSettings(WorldMapPoint viewpoint, WorldMapPoint refpoint, float projectionDistance, @Nullable WorldMapPoint overviewPosition, boolean overviewEnabled, @Nullable WorldMapPoint minimum, @Nullable WorldMapPoint maximum) {
  public WorldMapCameraSettings {
    Objects.requireNonNull(viewpoint, "viewpoint");
    Objects.requireNonNull(refpoint, "refpoint");
    requireFinite(viewpoint);
    requireFinite(refpoint);
    if(overviewPosition != null) {
      requireFinite(overviewPosition);
    }
    if(!Float.isFinite(projectionDistance) || projectionDistance <= 0) {
      throw new IllegalArgumentException("World map projection distance must be positive");
    }
    if((minimum == null) != (maximum == null)) {
      throw new IllegalArgumentException("World map camera bounds require both corners");
    }
    if(minimum != null) {
      requireFinite(minimum);
      requireFinite(maximum);
    }
    if(minimum != null && (minimum.x() > maximum.x() || minimum.y() > maximum.y() || minimum.z() > maximum.z())) {
      throw new IllegalArgumentException("World map camera bounds are inverted");
    }
  }

  private static void requireFinite(final WorldMapPoint point) {
    if(!Float.isFinite(point.x()) || !Float.isFinite(point.y()) || !Float.isFinite(point.z())) {
      throw new IllegalArgumentException("World map camera coordinates must be finite");
    }
  }

  /** Null overview position inherits the presentation profile's legacy continent anchor. */
  public static WorldMapCameraSettings legacy(@Nullable final WorldMapPoint overviewPosition) {
    return new WorldMapCameraSettings(new WorldMapPoint(0, -300, -900), new WorldMapPoint(0, 300, 900), 1100, overviewPosition, true, null, null);
  }
}
