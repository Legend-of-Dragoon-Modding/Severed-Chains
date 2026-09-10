package legend.game.wmap.world;

import legend.core.tags.FloatTag;
import legend.core.tags.IntTag;
import legend.core.tags.MapTag;
import legend.core.tags.RegistryIdTag;
import legend.core.tags.Tag;
import legend.game.types.GameState52c;
import org.legendofdragoon.modloader.registries.RegistryId;

import javax.annotation.Nullable;
import java.util.Objects;

/** Preserves legacy position tags while optionally identifying the saved route by stable ID. */
public final class WorldMapSave {
  private WorldMapSave() { }

  public static Tag write(final GameState52c gameState, @Nullable final WorldMapDefinition definition) {
    final MapTag tag = new MapTag();
    tag.set("pathIndex", new IntTag(gameState.pathIndex_4d8));
    tag.set("dotIndex", new IntTag(gameState.dotIndex_4da));
    tag.set("dotOffset", new FloatTag(gameState.dotOffset_4dc));
    tag.set("facing", new IntTag(gameState.facing_4dd));
    tag.set("directionalPathIndex", new IntTag(gameState.directionalPathIndex_4de));

    if(definition != null && gameState.directionalPathIndex_4de >= 0 && gameState.directionalPathIndex_4de < definition.routes().size()) {
      tag.set("schemaVersion", new IntTag(1));
      tag.set("routeId", new RegistryIdTag(definition.route(gameState.directionalPathIndex_4de).id()));
    }

    return tag;
  }

  @Nullable
  public static RegistryId read(final GameState52c gameState, @Nullable final Tag tag) {
    if(tag == null) {
      return null;
    }

    final MapTag map = tag.asMap();
    if(!map.has("pathIndex")) {
      return null;
    }

    if(map.has("schemaVersion") && map.get("schemaVersion").asInt().get() != 1) {
      throw new IllegalArgumentException("Unsupported world map save schema version " + map.get("schemaVersion").asInt().get());
    }

    final RegistryId routeId;
    if(map.has("routeId")) {
      if(!map.has("schemaVersion")) {
        throw new IllegalArgumentException("World map save routeId requires schemaVersion 1");
      }

      routeId = Objects.requireNonNull(map.get("routeId").asRegistryId().get(), "World map save routeId");
    } else {
      routeId = null;
    }

    gameState.pathIndex_4d8 = map.get("pathIndex").asInt().get();
    gameState.dotIndex_4da = map.get("dotIndex").asInt().get();
    gameState.dotOffset_4dc = map.get("dotOffset").asFloat().get();
    gameState.facing_4dd = map.get("facing").asInt().get();
    gameState.directionalPathIndex_4de = map.get("directionalPathIndex").asInt().get();
    return routeId;
  }

  /** Resolves only tagged saves; callers retain the legacy loading path when no route ID is stored. */
  public static void restoreRoute(final GameState52c gameState, final WorldMapDefinition definition, final RegistryId routeId) {
    final WorldMapRoute route;
    try {
      route = definition.route(routeId);
    } catch(final IllegalArgumentException exception) {
      throw new IllegalArgumentException("Saved world map route " + routeId + " is missing from the configured world map", exception);
    }

    final int lastDotIndex = definition.geometry().get(route.segmentIndex()).size() - 2;
    if(gameState.dotIndex_4da < 0 || gameState.dotIndex_4da > lastDotIndex) {
      throw new IllegalArgumentException("Saved world map dot index " + gameState.dotIndex_4da + " is outside route " + routeId + " bounds 0.." + lastDotIndex);
    }

    if(!Float.isFinite(gameState.dotOffset_4dc) || gameState.dotOffset_4dc < 0.0f || gameState.dotOffset_4dc >= 4.0f) {
      throw new IllegalArgumentException("Saved world map dot offset " + gameState.dotOffset_4dc + " is outside [0, 4) for route " + routeId);
    }

    gameState.directionalPathIndex_4de = route.legacyIndex();
    gameState.pathIndex_4d8 = route.segmentIndex();
  }
}
