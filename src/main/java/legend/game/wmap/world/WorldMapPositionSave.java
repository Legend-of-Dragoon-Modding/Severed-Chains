package legend.game.wmap.world;

import legend.core.tags.MapTag;
import legend.core.tags.StringTag;
import legend.core.tags.Tag;

import javax.annotation.Nullable;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.List;

/** Optional precision extension: old readers still consume the unchanged legacy cursor. */
public final class WorldMapPositionSave {
  private WorldMapPositionSave() { }

  public static void write(final MapTag tag, final WorldMapRoute route, final List<WorldMapPoint> geometry, final double distance) {
    final double length = new WorldMapRouteMetric(geometry).length();
    if(!Double.isFinite(distance) || distance < 0.0 || distance > length) throw new IllegalArgumentException("Invalid saved world map distance");
    tag.set("routeDistance", new StringTag(Double.toHexString(distance)));
    tag.set("routeGeometry", new StringTag(fingerprint(route, geometry)));
  }

  /** Geometry or compatibility cursor edits take precedence over stale precision metadata. */
  public static double read(@Nullable final Tag saved, final WorldMapRoute route, final List<WorldMapPoint> geometry, final int dot, final float offset) {
    if(saved == null) return Double.NaN;
    final MapTag tag = saved.asMap();
    if(!tag.has("routeDistance") || !tag.has("routeGeometry") || !tag.has("routeId")) return Double.NaN;
    if(!route.id().equals(tag.get("routeId").asRegistryId().get()) || !fingerprint(route, geometry).equals(tag.get("routeGeometry").asString().get())) return Double.NaN;
    final WorldMapRouteMetric metric = new WorldMapRouteMetric(geometry);
    final double distance = Double.parseDouble(tag.get("routeDistance").asString().get());
    if(!Double.isFinite(distance) || distance < 0.0 || distance > metric.length()) throw new IllegalArgumentException("Invalid saved world map distance for " + route.id());
    final WorldMapRouteMetric.Cursor cursor = metric.cursor(distance);
    if(cursor.index() != dot || Float.compare(Math.min(cursor.offset(), Math.nextDown(4.0f)), offset) != 0) return Double.NaN;
    return distance;
  }

  private static String fingerprint(final WorldMapRoute route, final List<WorldMapPoint> geometry) {
    try {
      final MessageDigest digest = MessageDigest.getInstance("SHA-256");
      final ByteBuffer pointBytes = ByteBuffer.allocate(12);
      digest.update((byte)route.direction());
      for(final WorldMapPoint point : geometry) {
        pointBytes.clear();
        pointBytes.putFloat(point.x()).putFloat(point.y()).putFloat(point.z());
        digest.update(pointBytes.array());
      }
      return HexFormat.of().formatHex(digest.digest());
    } catch(final NoSuchAlgorithmException failure) {
      throw new IllegalStateException("Required SHA-256 algorithm unavailable", failure);
    }
  }
}
