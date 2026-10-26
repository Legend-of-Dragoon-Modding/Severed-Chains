package legend.game.wmap.world;

import legend.game.wmap.DirectionalPathSegmentData08;
import legend.game.wmap.Location14;
import legend.game.wmap.Place0c;
import legend.game.wmap.WmapStatics;
import org.joml.Vector3f;
import org.legendofdragoon.modloader.registries.RegistryId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Imports the mutable legacy WMAP tables into a detached immutable definition. */
public final class LegacyWorldMap {
  private static final String MOD_ID = "lod";

  private LegacyWorldMap() {
  }

  public static WorldMapDefinition importDefinition() {
    return importDefinition(WmapStatics.locations_800f0e34, WmapStatics.places_800f0234, WmapStatics.directionalPathSegmentData_800f2248, WmapStatics.pathDotPosArr_800f591c, WmapStatics.pathSegmentLengths_800f5810);
  }

  public static WorldMapDefinition importDefinition(final Location14[] locations, final Place0c[] places, final DirectionalPathSegmentData08[] routes, final Vector3f[][] geometry, final int[] lengths) {
    if(locations == null || places == null || routes == null || geometry == null || lengths == null) throw new IllegalArgumentException("World map legacy tables must not be null");
    if(lengths.length != geometry.length + 1 || lengths[lengths.length - 1] != -1) throw new IllegalArgumentException("World map path lengths must contain one trailing -1 sentinel");

    final List<List<WorldMapPoint>> importedGeometry = importGeometry(geometry, lengths);
    final List<WorldMapNode> nodes = new ArrayList<>();
    final Map<WorldMapPoint, RegistryId> nodesByPosition = new HashMap<>();
    final List<WorldMapRoute> importedRoutes = importRoutes(routes, importedGeometry, nodes, nodesByPosition);
    final List<WorldMapPlace> importedPlaces = importPlaces(places);
    final List<Integer> routeIndices = new ArrayList<>(locations.length);
    final List<Integer> placeIndices = new ArrayList<>(locations.length);
    final List<WorldMapPortal> importedPortals = importPortals(locations, importedRoutes, importedPlaces, routeIndices, placeIndices);
    return new WorldMapDefinition(importedPortals, importedRoutes, importedPlaces, nodes, importedGeometry, routeIndices, placeIndices, lengths);
  }

  private static List<List<WorldMapPoint>> importGeometry(final Vector3f[][] geometry, final int[] lengths) {
    final List<List<WorldMapPoint>> imported = new ArrayList<>(geometry.length);
    for(int i = 0; i < geometry.length; i++) {
      if(geometry[i] == null || geometry[i].length != lengths[i]) throw new IllegalArgumentException("World map geometry segment " + i + " does not match its length " + lengths[i]);
      final List<WorldMapPoint> segment = new ArrayList<>(geometry[i].length);
      for(final Vector3f point : geometry[i]) {
        if(point == null) throw new IllegalArgumentException("World map geometry segment " + i + " contains null point");
        segment.add(new WorldMapPoint(point.x, point.y, point.z));
      }
      imported.add(segment);
    }
    return imported;
  }

  private static List<WorldMapRoute> importRoutes(final DirectionalPathSegmentData08[] routes, final List<List<WorldMapPoint>> geometry, final List<WorldMapNode> nodes, final Map<WorldMapPoint, RegistryId> nodesByPosition) {
    final List<WorldMapRoute> imported = new ArrayList<>();
    boolean foundSentinel = false;
    for(int i = 0; i < routes.length; i++) {
      final DirectionalPathSegmentData08 route = routes[i];
      if(route == null) throw new IllegalArgumentException("World map route " + i + " is null");
      final int signedSegment = route.pathSegmentIndexAndDirection_00;
      if(signedSegment == 0) {
        if(i != routes.length - 1) throw new IllegalArgumentException("World map route sentinel appears before the final row at " + i);
        foundSentinel = true;
        continue;
      }
      if(foundSentinel) throw new IllegalArgumentException("World map route appears after sentinel at " + i);
      final int segmentIndex = Math.abs(signedSegment) - 1;
      if(segmentIndex < 0 || segmentIndex >= geometry.size()) throw new IllegalArgumentException("World map route " + i + " references invalid geometry segment " + segmentIndex);
      final List<WorldMapPoint> segment = geometry.get(segmentIndex);
      final RegistryId first = nodeId(segment.getFirst(), nodes, nodesByPosition);
      final RegistryId last = nodeId(segment.getLast(), nodes, nodesByPosition);
      final int direction = signedSegment < 0 ? -1 : 1;
      imported.add(new WorldMapRoute(id("route", i), i, direction > 0 ? first : last, direction > 0 ? last : first, segmentIndex, direction, route.encounterRate_03, route.battleStage_04, route.encounterIndex_05, route.modelIndex_06));
    }
    if(!foundSentinel) throw new IllegalArgumentException("World map routes are missing their trailing signed-zero sentinel");
    return imported;
  }

  private static RegistryId nodeId(final WorldMapPoint point, final List<WorldMapNode> nodes, final Map<WorldMapPoint, RegistryId> nodesByPosition) {
    RegistryId id = nodesByPosition.get(point);
    if(id == null) {
      id = id("node", nodes.size());
      nodesByPosition.put(point, id);
      nodes.add(new WorldMapNode(id, point));
    }
    return id;
  }

  private static List<WorldMapPlace> importPlaces(final Place0c[] places) {
    final List<WorldMapPlace> imported = new ArrayList<>(places.length);
    for(int i = 0; i < places.length; i++) {
      final Place0c place = places[i];
      if(place == null || place.soundIndices_06 == null) throw new IllegalArgumentException("World map place " + i + " is incomplete");
      final List<Integer> sounds = new ArrayList<>(place.soundIndices_06.length);
      for(final int sound : place.soundIndices_06) sounds.add(sound);
      imported.add(new WorldMapPlace(id("place", i), i, place.name_00, place.fileIndex_04, place.servicesFlag_05, sounds));
    }
    return imported;
  }

  private static List<WorldMapPortal> importPortals(final Location14[] locations, final List<WorldMapRoute> routes, final List<WorldMapPlace> places, final List<Integer> routeIndices, final List<Integer> placeIndices) {
    final List<WorldMapPortal> imported = new ArrayList<>(locations.length);
    for(int i = 0; i < locations.length; i++) {
      final Location14 location = locations[i];
      if(location == null) throw new IllegalArgumentException("World map portal " + i + " is null");
      routeIndices.add(location.directionalPathIndex_00);
      placeIndices.add(location.placeIndex_02);
      if(location.directionalPathIndex_00 < -1) throw new IllegalArgumentException("World map portal " + i + " has invalid route " + location.directionalPathIndex_00);
      final RegistryId route = location.directionalPathIndex_00 < 0 ? null : routeId(location.directionalPathIndex_00, routes, i);
      if(route != null && (location.placeIndex_02 < 0 || location.placeIndex_02 >= places.size())) throw new IllegalArgumentException("World map portal " + i + " has a valid route but no valid place");
      final RegistryId place = location.placeIndex_02 < 0 || location.placeIndex_02 >= places.size() ? null : placeId(location.placeIndex_02, places, i);
      imported.add(new WorldMapPortal(id("location", i), i, route, place, new SubmapEndpoint(location.submapCutFrom_04, location.submapSceneFrom_06), new SubmapEndpoint(location.submapCutTo_08, location.submapSceneTo_0a), location.unknownIndex_0c, location.continent_0e, location.thumbnailShouldUseFullBrightness_10, location.effectFlags_12));
    }
    return imported;
  }

  private static RegistryId routeId(final int index, final List<WorldMapRoute> routes, final int portal) {
    if(index < 0 || index >= routes.size()) throw new IllegalArgumentException("World map portal " + portal + " references invalid route " + index);
    return routes.get(index).id();
  }

  private static RegistryId placeId(final int index, final List<WorldMapPlace> places, final int portal) {
    if(index < 0 || index >= places.size()) throw new IllegalArgumentException("World map portal " + portal + " references invalid place " + index);
    return places.get(index).id();
  }

  private static RegistryId id(final String type, final int index) {
    return new RegistryId(MOD_ID, "wmap_" + type + '_' + index);
  }
}
