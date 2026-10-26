package legend.game.wmap.world;

import legend.core.tags.MapTag;
import legend.core.tags.RegistryIdTag;
import legend.core.tags.StringTag;
import legend.game.EngineDestination;
import org.junit.jupiter.api.Test;
import org.legendofdragoon.modloader.registries.RegistryId;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WorldMapPositionSaveTest {
  private static final RegistryId ID = new RegistryId("test", "route");
  private static final WorldMapRoute ROUTE = new WorldMapRoute(ID, 0, new RegistryId("test", "a"), new RegistryId("test", "b"), 0, 1, 0, 0, 0, 0);
  private static final List<WorldMapPoint> GEOMETRY = List.of(new WorldMapPoint(0, 0, 0), new WorldMapPoint(1000000, 0, 0));

  @Test void exactDistanceSurvivesRepeatedSaveAndReturnEncoding() {
    final double distance = 123456.789012345;
    final WorldMapRouteMetric metric = new WorldMapRouteMetric(GEOMETRY);
    final var cursor = metric.cursor(distance);
    final MapTag tag = new MapTag();
    tag.set("routeId", new RegistryIdTag(ID));
    WorldMapPositionSave.write(tag, ROUTE, GEOMETRY, distance);
    assertEquals(distance, WorldMapPositionSave.read(tag, ROUTE, GEOMETRY, cursor.index(), cursor.offset()));
    assertNotEquals(distance, metric.distance(cursor.index(), cursor.offset()));
    final var target = WorldMapTravelTarget.atRouteDistance(ID, distance / metric.length());
    assertEquals(target, EngineDestination.worldMapTarget(EngineDestination.worldMap(target).data()));
    final MapTag legacyEdit = EngineDestination.worldMap(target).data().asMap();
    legacyEdit.get("worldMapTarget").asMap().set("distance", new legend.core.tags.FloatTag(0.75f));
    assertEquals(WorldMapTravelTarget.atRouteDistance(ID, 0.75f), EngineDestination.worldMapTarget(legacyEdit));
  }

  @Test void legacyCursorOrGeometryEditsInvalidatePrecisionMetadata() {
    final MapTag tag = new MapTag();
    tag.set("routeId", new RegistryIdTag(ID));
    WorldMapPositionSave.write(tag, ROUTE, GEOMETRY, 250000);
    assertTrue(Double.isNaN(WorldMapPositionSave.read(tag, ROUTE, GEOMETRY, 0, 2)));
    assertTrue(Double.isNaN(WorldMapPositionSave.read(tag, ROUTE, List.of(GEOMETRY.getFirst(), new WorldMapPoint(2000000, 0, 0)), 0, 1)));
    assertTrue(Double.isNaN(WorldMapPositionSave.read(new MapTag(), ROUTE, GEOMETRY, 0, 1)));
  }

  @Test void exactEndpointAndNonfiniteDataAreHandledExplicitly() {
    final MapTag tag = new MapTag();
    tag.set("routeId", new RegistryIdTag(ID));
    WorldMapPositionSave.write(tag, ROUTE, GEOMETRY, 1000000);
    assertEquals(1000000, WorldMapPositionSave.read(tag, ROUTE, GEOMETRY, 0, Math.nextDown(4.0f)));
    tag.set("routeDistance", new StringTag("NaN"));
    assertThrows(IllegalArgumentException.class, () -> WorldMapPositionSave.read(tag, ROUTE, GEOMETRY, 0, Math.nextDown(4.0f)));
  }
}
