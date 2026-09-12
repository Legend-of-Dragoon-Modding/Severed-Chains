package legend.game.wmap.world;

import legend.core.tags.FloatTag;
import legend.core.tags.IntTag;
import legend.core.tags.MapTag;
import legend.core.tags.RegistryIdTag;
import legend.core.tags.Tag;
import legend.game.types.GameState52c;
import org.legendofdragoon.modloader.registries.RegistryId;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/** Preserves legacy position tags and adds route-geometry metadata for topology-safe restores. */
public final class WorldMapSave {
  private static final int SCHEMA_V1 = 1;
  private static final int SCHEMA_V2 = 2;
  private static final float OFFSET_SCALE = 4.0f;
  private static final float MAX_RESTORE_DISTANCE = 32.0f;
  private static final float EXACT_RESTORE_DISTANCE = 0.01f;
  private static final float MIN_TANGENT_DOT = 0.95f;
  private static final float AMBIGUITY_EPSILON = 0.001f;

  private WorldMapSave() {
  }

  public static Tag write(final GameState52c gameState, @Nullable final WorldMapDefinition definition) {
    final MapTag tag = new MapTag();
    tag.set("pathIndex", new IntTag(gameState.pathIndex_4d8));
    tag.set("dotIndex", new IntTag(gameState.dotIndex_4da));
    tag.set("dotOffset", new FloatTag(gameState.dotOffset_4dc));
    tag.set("facing", new IntTag(gameState.facing_4dd));
    tag.set("directionalPathIndex", new IntTag(gameState.directionalPathIndex_4de));
    if(definition == null || gameState.directionalPathIndex_4de < 0 || gameState.directionalPathIndex_4de >= definition.routes().size()) return tag;

    final WorldMapRoute route = definition.route(gameState.directionalPathIndex_4de);
    final List<WorldMapPoint> geometry = definition.geometry().get(route.segmentIndex());
    validateLegacyPosition(gameState.dotIndex_4da, gameState.dotOffset_4dc, geometry.size(), route.id());
    final WorldMapPoint position = position(geometry, gameState.dotIndex_4da, gameState.dotOffset_4dc);
    final WorldMapPoint start = geometry.getFirst();
    final WorldMapPoint end = geometry.getLast();
    tag.set("schemaVersion", new IntTag(SCHEMA_V2));
    tag.set("routeId", new RegistryIdTag(route.id()));
    tag.set("routeDirection", new IntTag(route.direction()));
    point(tag, "geometryStart", start);
    point(tag, "geometryEnd", end);
    point(tag, "dotStart", geometry.get(gameState.dotIndex_4da));
    point(tag, "dotEnd", geometry.get(gameState.dotIndex_4da + 1));
    point(tag, "position", position);
    return tag;
  }

  @Nullable
  public static RegistryId read(final GameState52c gameState, @Nullable final Tag tag) {
    if(tag == null) return null;
    final MapTag map = tag.asMap();
    if(!map.has("pathIndex")) return null;
    final int schemaVersion = schemaVersion(map);
    final RegistryId routeId = routeId(map, schemaVersion);
    gameState.pathIndex_4d8 = requiredInt(map, "pathIndex");
    gameState.dotIndex_4da = requiredInt(map, "dotIndex");
    gameState.dotOffset_4dc = requiredFloat(map, "dotOffset");
    gameState.facing_4dd = requiredInt(map, "facing");
    gameState.directionalPathIndex_4de = requiredInt(map, "directionalPathIndex");
    if(schemaVersion == SCHEMA_V2) metadata(map);
    return routeId;
  }

  public static void restoreRoute(final GameState52c gameState, final WorldMapDefinition definition, final RegistryId routeId) {
    final WorldMapRoute route;
    try {
      route = definition.route(routeId);
    } catch(final IllegalArgumentException ex) {
      throw new IllegalArgumentException("Saved world map route " + routeId + " is unavailable", ex);
    }
    final List<WorldMapPoint> geometry = definition.geometry().get(route.segmentIndex());
    validateLegacyPosition(gameState.dotIndex_4da, gameState.dotOffset_4dc, geometry.size(), routeId);
    gameState.directionalPathIndex_4de = route.legacyIndex();
    gameState.pathIndex_4d8 = route.segmentIndex();
  }

  public static void restoreRoute(final GameState52c gameState, final WorldMapDefinition definition, final RegistryId routeId, @Nullable final Tag savedTag) {
    if(savedTag == null) {
      restoreRoute(gameState, definition, routeId);
      return;
    }
    final MapTag map = savedTag.asMap();
    if(schemaVersion(map) != SCHEMA_V2) {
      restoreRoute(gameState, definition, routeId);
      return;
    }

    final Metadata metadata = metadata(map);
    if(metadata.direction() != 1 && metadata.direction() != -1) {
      throw new IllegalArgumentException("Saved world map route direction must be -1 or 1");
    }
    final RestoredPosition restored = restorePosition(gameState, definition, routeId, metadata);
    gameState.directionalPathIndex_4de = restored.route().legacyIndex();
    gameState.pathIndex_4d8 = restored.route().segmentIndex();
    gameState.dotIndex_4da = restored.dotIndex();
    gameState.dotOffset_4dc = restored.dotOffset();
  }

  private static RestoredPosition restorePosition(final GameState52c gameState, final WorldMapDefinition definition, final RegistryId routeId, final Metadata metadata) {
    final RestoredPosition unchanged = unchangedPosition(gameState, definition, routeId, metadata);
    if(unchanged != null) return unchanged;

    final List<WorldMapRoute> candidates = new ArrayList<>();
    for(final WorldMapRoute route : definition.routes()) {
      if(route.direction() == metadata.direction()) candidates.add(route);
    }
    if(candidates.isEmpty()) throw new IllegalArgumentException("Saved world map route " + routeId + " has no same-direction replacement");

    final List<Candidate> projections = new ArrayList<>();
    for(final WorldMapRoute route : candidates) {
      final Projection projection = project(definition.geometry().get(route.segmentIndex()), metadata.position(), route.direction(), metadata);
      projections.add(new Candidate(route, projection));
    }
    projections.sort(Comparator.comparingDouble(candidate -> candidate.projection().distanceSquared()));
    final Candidate retained = projections.stream().filter(candidate -> candidate.route().id().equals(routeId)).findFirst().orElse(null);
    final List<Candidate> exact = projections.stream()
      .filter(candidate -> candidate.projection().distanceSquared() <= EXACT_RESTORE_DISTANCE * EXACT_RESTORE_DISTANCE)
      .filter(candidate -> candidate.projection().tangentDot() >= MIN_TANGENT_DOT)
      .toList();
    final Candidate selected;
    if(!exact.isEmpty()) {
      final Candidate best = exact.getFirst();
      if(retained != null && exact.contains(retained)) {
        selected = retained;
      } else {
        final List<Candidate> tied = exact.stream()
          .filter(candidate -> Math.abs(candidate.projection().distanceSquared() - best.projection().distanceSquared()) <= AMBIGUITY_EPSILON)
          .toList();
        final List<Candidate> topology = tied.stream()
          .filter(candidate -> sharesOriginalEndpoint(definition.geometry().get(candidate.route().segmentIndex()), metadata))
          .toList();
        if(topology.size() == 1) {
          selected = topology.getFirst();
        } else if(tied.size() == 1) {
          selected = best;
        } else {
          throw new IllegalArgumentException("Saved world map position is ambiguous between same-direction routes");
        }
      }
    } else if(retained != null && sharesOriginalEndpoint(definition.geometry().get(retained.route().segmentIndex()), metadata) && retained.projection().tangentDot() >= MIN_TANGENT_DOT && Math.sqrt(retained.projection().distanceSquared()) <= MAX_RESTORE_DISTANCE) {
      selected = retained;
    } else {
      throw new IllegalArgumentException("Saved world map position has no matching same-direction route");
    }
    return new RestoredPosition(selected.route(), selected.projection().dotIndex(), selected.projection().dotOffset());
  }

  @Nullable
  private static RestoredPosition unchangedPosition(final GameState52c gameState, final WorldMapDefinition definition, final RegistryId routeId, final Metadata metadata) {
    final WorldMapRoute retained;
    try {
      retained = definition.route(routeId);
    } catch(final IllegalArgumentException ex) {
      return null;
    }

    if(retained.direction() != metadata.direction()) return null;

    final List<WorldMapPoint> geometry = definition.geometry().get(retained.segmentIndex());
    validateLegacyPosition(gameState.dotIndex_4da, gameState.dotOffset_4dc, geometry.size(), routeId);
    if(!geometry.get(gameState.dotIndex_4da).equals(metadata.dotStart()) || !geometry.get(gameState.dotIndex_4da + 1).equals(metadata.dotEnd())) return null;
    if(!position(geometry, gameState.dotIndex_4da, gameState.dotOffset_4dc).equals(metadata.position())) {
      throw new IllegalArgumentException("Saved world map position does not match saved dot metadata");
    }

    return new RestoredPosition(retained, gameState.dotIndex_4da, gameState.dotOffset_4dc);
  }

  private static boolean sharesOriginalEndpoint(final List<WorldMapPoint> geometry, final Metadata metadata) {
    return geometry.getFirst().equals(metadata.start()) || geometry.getFirst().equals(metadata.end())
      || geometry.getLast().equals(metadata.start()) || geometry.getLast().equals(metadata.end());
  }

  private static float tangentDot(final WorldMapPoint start, final WorldMapPoint end, final int direction, final Metadata metadata) {
    final float candidateX = (end.x() - start.x()) * direction;
    final float candidateY = (end.y() - start.y()) * direction;
    final float candidateZ = (end.z() - start.z()) * direction;
    final float savedX = (metadata.dotEnd().x() - metadata.dotStart().x()) * metadata.direction();
    final float savedY = (metadata.dotEnd().y() - metadata.dotStart().y()) * metadata.direction();
    final float savedZ = (metadata.dotEnd().z() - metadata.dotStart().z()) * metadata.direction();
    final float candidateLength = (float)Math.sqrt(candidateX * candidateX + candidateY * candidateY + candidateZ * candidateZ);
    final float savedLength = (float)Math.sqrt(savedX * savedX + savedY * savedY + savedZ * savedZ);
    if(candidateLength == 0.0f || savedLength == 0.0f) return -1.0f;
    return (candidateX * savedX + candidateY * savedY + candidateZ * savedZ) / candidateLength / savedLength;
  }

  private static Projection project(final List<WorldMapPoint> geometry, final WorldMapPoint position, final int direction, final Metadata metadata) {
    if(geometry.size() < 2) throw new IllegalArgumentException("World map geometry must contain at least two points");
    Projection best = null;
    for(int index = 0; index < geometry.size() - 1; index++) {
      final WorldMapPoint start = geometry.get(index);
      final WorldMapPoint end = geometry.get(index + 1);
      final float dx = end.x() - start.x();
      final float dy = end.y() - start.y();
      final float dz = end.z() - start.z();
      final float lengthSquared = dx * dx + dy * dy + dz * dz;
      final float fraction = lengthSquared == 0.0f ? 0.0f : Math.clamp(((position.x() - start.x()) * dx + (position.y() - start.y()) * dy + (position.z() - start.z()) * dz) / lengthSquared, 0.0f, 1.0f);
      final float px = start.x() + dx * fraction;
      final float py = start.y() + dy * fraction;
      final float pz = start.z() + dz * fraction;
      final float distanceSquared = square(position.x() - px) + square(position.y() - py) + square(position.z() - pz);
      final int dotIndex = fraction >= 1.0f && index < geometry.size() - 2 ? index + 1 : index;
      final float offset = fraction >= 1.0f && index == geometry.size() - 2 ? Math.nextDown(OFFSET_SCALE) : fraction >= 1.0f ? 0.0f : fraction * OFFSET_SCALE;
      final Projection candidate = new Projection(dotIndex, offset, distanceSquared, tangentDot(start, end, direction, metadata));
      if(best == null || candidate.distanceSquared() < best.distanceSquared()) best = candidate;
    }
    return Objects.requireNonNull(best);
  }

  private static float square(final float value) {
    return value * value;
  }

  private static WorldMapPoint position(final List<WorldMapPoint> geometry, final int dotIndex, final float dotOffset) {
    final WorldMapPoint start = geometry.get(dotIndex);
    final WorldMapPoint end = geometry.get(dotIndex + 1);
    final float fraction = dotOffset / OFFSET_SCALE;
    return new WorldMapPoint(start.x() + (end.x() - start.x()) * fraction, start.y() + (end.y() - start.y()) * fraction, start.z() + (end.z() - start.z()) * fraction);
  }

  private static void validateLegacyPosition(final int dotIndex, final float dotOffset, final int points, final RegistryId routeId) {
    if(points < 2) throw new IllegalArgumentException("Saved world map route " + routeId + " has fewer than two points");
    if(dotIndex < 0 || dotIndex >= points - 1) throw new IllegalArgumentException("Saved world map dot index " + dotIndex + " outside route " + routeId);
    if(!Float.isFinite(dotOffset) || dotOffset < 0.0f || dotOffset >= OFFSET_SCALE) {
      throw new IllegalArgumentException("Saved world map dot offset " + dotOffset + " outside [0, 4) for route " + routeId);
    }
  }

  private static int schemaVersion(final MapTag map) {
    if(!map.has("schemaVersion")) return 0;
    final int value = requiredInt(map, "schemaVersion");
    if(value < SCHEMA_V1 || value > SCHEMA_V2) throw new IllegalArgumentException("Unsupported world map save schema " + value);
    return value;
  }

  @Nullable
  private static RegistryId routeId(final MapTag map, final int schemaVersion) {
    if(!map.has("routeId")) return null;
    if(schemaVersion == 0) throw new IllegalArgumentException("World map route ID requires schema version");
    return Objects.requireNonNull(map.get("routeId").asRegistryId().get(), "Saved world map route ID");
  }

  private static Metadata metadata(final MapTag map) {
    final int direction = requiredInt(map, "routeDirection");
    final WorldMapPoint start = point(map, "geometryStart");
    final WorldMapPoint end = point(map, "geometryEnd");
    final WorldMapPoint dotStart = point(map, "dotStart");
    final WorldMapPoint dotEnd = point(map, "dotEnd");
    final WorldMapPoint position = point(map, "position");
    if(dotStart.equals(dotEnd)) throw new IllegalArgumentException("Saved world map dot segment endpoints must differ");
    return new Metadata(direction, start, end, dotStart, dotEnd, position);
  }

  private static void point(final MapTag tag, final String key, final WorldMapPoint point) {
    tag.set(key + "X", new FloatTag(point.x()));
    tag.set(key + "Y", new FloatTag(point.y()));
    tag.set(key + "Z", new FloatTag(point.z()));
  }

  private static WorldMapPoint point(final MapTag tag, final String key) {
    final float x = requiredFloat(tag, key + "X");
    final float y = requiredFloat(tag, key + "Y");
    final float z = requiredFloat(tag, key + "Z");
    if(!Float.isFinite(x) || !Float.isFinite(y) || !Float.isFinite(z)) throw new IllegalArgumentException("Saved world map " + key + " is non-finite");
    return new WorldMapPoint(x, y, z);
  }

  private static int requiredInt(final MapTag tag, final String key) {
    if(!tag.has(key)) throw new IllegalArgumentException("Saved world map data is missing " + key);
    return tag.get(key).asInt().get();
  }

  private static float requiredFloat(final MapTag tag, final String key) {
    if(!tag.has(key)) throw new IllegalArgumentException("Saved world map data is missing " + key);
    return tag.get(key).asFloat().get();
  }

  private record Metadata(int direction, WorldMapPoint start, WorldMapPoint end, WorldMapPoint dotStart, WorldMapPoint dotEnd, WorldMapPoint position) {
  }

  private record Candidate(WorldMapRoute route, Projection projection) {
  }

  private record Projection(int dotIndex, float dotOffset, float distanceSquared, float tangentDot) {
  }

  private record RestoredPosition(WorldMapRoute route, int dotIndex, float dotOffset) {
  }
}
