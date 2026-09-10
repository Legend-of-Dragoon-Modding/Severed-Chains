package legend.game.wmap.world;

import legend.core.tags.IntTag;
import legend.core.tags.MapTag;
import legend.core.tags.RegistryIdTag;
import legend.game.types.GameState52c;
import legend.game.types.Flags;
import legend.game.wmap.Continent;
import legend.game.wmap.DirectionalPathSegmentData08;
import legend.game.wmap.Location14;
import legend.game.wmap.Place0c;
import legend.game.wmap.WMapDestinationMarker2c;
import legend.game.wmap.WmapStatics;
import org.joml.Vector3f;
import org.legendofdragoon.modloader.registries.RegistryId;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/** Standalone source-table oracle; throws assertions without requiring JVM -ea. */
public final class WorldMapCompatibilityCheck {
  private static long checks;

  private WorldMapCompatibilityCheck() { }

  public static void main(final String[] args) {
    final WorldMapDefinition definition = LegacyWorldMap.importDefinition();
    projection(definition);
    projection(definition.toBuilder().build());
    story();
    parity(definition);
    policies(definition);
    invalidDefinitions(definition);
    saves(definition);
    travel(definition);
    objectives(definition);
    replacementGeometry(definition);
    dynamicAvailability(definition);
    System.out.println("WorldMapCompatibilityCheck passed " + checks + " assertions; 49 presets, 256 slots, 9 continents, all geometry endpoints and facings");
  }

  private static void check(final boolean condition, final String message) {
    checks++;
    if(!condition) throw new AssertionError(message);
  }

  private static void equal(final Object expected, final Object actual, final String message) {
    check(Objects.equals(expected, actual), message + ": expected " + expected + ", got " + actual);
  }

  private static void rejects(final Runnable action, final String message) {
    try {
      action.run();
    } catch(final IllegalArgumentException exception) {
      check(exception.getMessage() != null && !exception.getMessage().isBlank(), message + " has diagnostic");
      return;
    }
    throw new AssertionError(message + " was accepted");
  }

  private static void projection(final WorldMapDefinition definition) {
    final Location14[] locations = definition.locationData();
    equal(WmapStatics.locations_800f0e34.length, locations.length, "location count");
    for(int i = 0; i < locations.length; i++) {
      final Location14 a = WmapStatics.locations_800f0e34[i];
      final Location14 b = locations[i];
      check(Arrays.equals(new int[]{a.directionalPathIndex_00, a.placeIndex_02, a.submapCutFrom_04, a.submapSceneFrom_06, a.submapCutTo_08, a.submapSceneTo_0a, a.unknownIndex_0c, a.effectFlags_12}, new int[]{b.directionalPathIndex_00, b.placeIndex_02, b.submapCutFrom_04, b.submapSceneFrom_06, b.submapCutTo_08, b.submapSceneTo_0a, b.unknownIndex_0c, b.effectFlags_12}), "location fields " + i);
      equal(a.continent_0e, b.continent_0e, "location continent " + i);
      equal(a.thumbnailShouldUseFullBrightness_10, b.thumbnailShouldUseFullBrightness_10, "location brightness " + i);
      equal(i, definition.portal(i).legacyIndex(), "location ABI " + i);
    }
    final Place0c[] places = definition.placeData();
    equal(WmapStatics.places_800f0234.length, places.length, "place count");
    for(int i = 0; i < places.length; i++) {
      final Place0c a = WmapStatics.places_800f0234[i];
      final Place0c b = places[i];
      equal(a.name_00, b.name_00, "place name " + i);
      equal(a.fileIndex_04, b.fileIndex_04, "place thumbnail " + i);
      equal(a.servicesFlag_05, b.servicesFlag_05, "place services " + i);
      check(Arrays.equals(a.soundIndices_06, b.soundIndices_06), "place sounds " + i);
    }
    final DirectionalPathSegmentData08[] routes = definition.directionalPathData();
    equal(WmapStatics.directionalPathSegmentData_800f2248.length, routes.length, "route count including sentinel");
    for(int i = 0; i < routes.length; i++) {
      final DirectionalPathSegmentData08 a = WmapStatics.directionalPathSegmentData_800f2248[i];
      final DirectionalPathSegmentData08 b = routes[i];
      check(Arrays.equals(new int[]{a.pathSegmentIndexAndDirection_00, a.encounterRate_03, a.battleStage_04, a.encounterIndex_05, a.modelIndex_06}, new int[]{b.pathSegmentIndexAndDirection_00, b.encounterRate_03, b.battleStage_04, b.encounterIndex_05, b.modelIndex_06}), "route fields " + i);
    }
    check(Arrays.equals(WmapStatics.pathSegmentLengths_800f5810, definition.pathLengths()), "path lengths and sentinel");
    final Vector3f[][] paths = definition.pathData();
    equal(WmapStatics.pathDotPosArr_800f591c.length, paths.length, "geometry count");
    for(int i = 0; i < paths.length; i++) {
      equal(WmapStatics.pathDotPosArr_800f591c[i].length, paths[i].length, "geometry length " + i);
      for(int j = 0; j < paths[i].length; j++) equal(WmapStatics.pathDotPosArr_800f591c[i][j], paths[i][j], "geometry " + i + '/' + j);
    }
    locations[0].directionalPathIndex_00 = -1;
    paths[0][0].zero();
    definition.pathLengths()[0] = 999;
    equal(WmapStatics.locations_800f0e34[0].directionalPathIndex_00, definition.locationData()[0].directionalPathIndex_00, "detached location projection");
    equal(WmapStatics.pathDotPosArr_800f591c[0][0], definition.pathData()[0][0], "detached geometry projection");
    equal(WmapStatics.pathSegmentLengths_800f5810[0], definition.pathLengths()[0], "detached lengths projection");
  }

  private static Flags flags(final int[] words) {
    final Flags result = new Flags(words.length);
    for(int i = 0; i < words.length; i++) result.setRaw(i, words[i]);
    return result;
  }

  private static int legacySelect(final Flags story) {
    int result = -1;
    for(int i = 0; i < 49; i++) {
      if(story.get(WmapStatics.wmapDestinationMarkers_800f5a6c[i].packedFlag_00)) result = i;
    }
    return result;
  }

  private static void story() {
    final WMapDestinationMarker2c[] presets = WmapStatics.wmapDestinationMarkers_800f5a6c;
    for(int i = -1; i < 49; i++) {
      final Flags story = new Flags(256);
      if(i >= 0) story.set(presets[i].packedFlag_00, true);
      checkStory(story, "single preset " + i);
      if(i >= 0) {
        for(int j = 0; j < 49; j++) {
          final Flags combined = new Flags(256);
          combined.set(presets[i].packedFlag_00, true);
          combined.set(presets[j].packedFlag_00, true);
          checkStory(combined, "combined presets " + i + '/' + j);
        }
      }
    }
    final Flags all = new Flags(256);
    for(int i = 0; i < 49; i++) all.set(presets[i].packedFlag_00, true);
    checkStory(all, "all presets last wins");
  }

  private static void checkStory(final Flags story, final String context) {
    final Flags locations = new Flags(8);
    for(int i = 0; i < 8; i++) locations.setRaw(i, 0x55aa55aa ^ i);
    final int expected = legacySelect(story);
    equal(expected, WorldMapStory.select(story, WmapStatics.wmapDestinationMarkers_800f5a6c), context + " select");
    equal(expected, WorldMapStory.apply(story, locations, WmapStatics.wmapDestinationMarkers_800f5a6c), context + " apply");
    for(int i = 0; i < 8; i++) equal(expected < 0 ? 0x55aa55aa ^ i : WmapStatics.wmapDestinationMarkers_800f5a6c[expected].flags_04[i], locations.getRaw(i), context + " word " + i);
  }

  private static int legacyValidity(final int index, final int mode, final Continent continent, final Flags flags, final Vector3f position) {
    final Location14 location = WmapStatics.locations_800f0e34[index];
    if(location.directionalPathIndex_00 == -1) return -1;
    if(mode != -1 && location.continent_0e != continent) return -2;
    if(!flags.get(index)) return 1;
    if(mode == 0 || mode == -1) return 0;
    final int signed = WmapStatics.directionalPathSegmentData_800f2248[location.directionalPathIndex_00].pathSegmentIndexAndDirection_00;
    if(signed == 0) return -3;
    final int segment = Math.abs(signed) - 1;
    position.set(WmapStatics.pathDotPosArr_800f591c[segment][signed > 0 ? 0 : WmapStatics.pathSegmentLengths_800f5810[segment] - 1]);
    return 0;
  }

  private record ExpectedConnection(int portal, int route, WorldMapPoint next) { }

  private static boolean near(final Vector3f a, final Vector3f b) {
    return Math.abs(a.x - b.x) < 0.00001f && Math.abs(a.y - b.y) < 0.00001f && Math.abs(a.z - b.z) < 0.00001f;
  }

  private static List<ExpectedConnection> legacyConnections(final Vector3f position, final Continent continent, final int facing, final Flags flags) {
    final List<ExpectedConnection> result = new ArrayList<>();
    for(int i = 0; i < WmapStatics.locations_800f0e34.length; i++) {
      if(legacyValidity(i, 0, continent, flags, null) != 0) continue;
      final Location14 location = WmapStatics.locations_800f0e34[i];
      if(location.unknownIndex_0c == -1) continue;
      final int route = location.directionalPathIndex_00;
      final int signed = WmapStatics.directionalPathSegmentData_800f2248[route].pathSegmentIndexAndDirection_00;
      if(facing > 0 && signed < 0 || facing < 0 && signed > 0) continue;
      final int segment = Math.abs(signed) - 1;
      final Vector3f[] points = WmapStatics.pathDotPosArr_800f591c[segment];
      final int count = WmapStatics.pathSegmentLengths_800f5810[segment];
      if(near(position, points[count - 1])) result.add(expectedConnection(i, route, points[count - 2]));
      if(near(position, points[0])) result.add(expectedConnection(i, route, points[1]));
    }
    return result;
  }

  private static ExpectedConnection expectedConnection(final int portal, final int route, final Vector3f point) {
    return new ExpectedConnection(portal, route, new WorldMapPoint(point.x, point.y, point.z));
  }

  private static void parity(final WorldMapDefinition definition) {
    final WorldMapRuntime runtime = new WorldMapRuntime(definition, new WorldMapRules.Builder().build());
    for(int preset = 0; preset < 49; preset++) {
      final Flags locations = flags(WmapStatics.wmapDestinationMarkers_800f5a6c[preset].flags_04);
      runtime.resolve(new WorldMapProgression.Builder(new Flags(256), locations).build());
      for(final Continent continent : Continent.values()) {
        for(int index = 0; index < 256; index++) {
          for(final int mode : new int[]{0, -1, 1}) {
            final Vector3f expectedPoint = new Vector3f(123, 456, 789);
            final Vector3f actualPoint = new Vector3f(expectedPoint);
            final String context = "validity preset=" + preset + " continent=" + continent + " slot=" + index + " mode=" + mode;
            equal(legacyValidity(index, mode, continent, locations, expectedPoint), runtime.legacyValidity(index, mode, continent, actualPoint, WorldMapAction.SEE), context);
            equal(expectedPoint, actualPoint, context + " position");
          }
        }
        for(final Vector3f[] path : WmapStatics.pathDotPosArr_800f591c) {
          for(final Vector3f endpoint : new Vector3f[]{path[0], path[path.length - 1]}) {
            for(final int facing : new int[]{-1, 0, 1}) {
              final List<ExpectedConnection> actual = runtime.traversal().connections(endpoint, continent, facing, runtime.view()).stream().map(c -> new ExpectedConnection(c.portalIndex(), c.routeIndex(), c.nextPoint())).toList();
              equal(legacyConnections(endpoint, continent, facing, locations), actual, "junction preset=" + preset + " continent=" + continent + " facing=" + facing + " endpoint=" + endpoint);
            }
          }
        }
      }
    }
  }

  private static void policies(final WorldMapDefinition definition) {
    final Flags story = new Flags(256);
    final Flags locations = new Flags(8);
    final WorldMapProgression progression = new WorldMapProgression.Builder(story, locations).build();
    final WorldMapRuntime open = new WorldMapRuntime(definition, new WorldMapRules.Builder().policy(WorldMapPolicy.OPEN).build());
    open.resolve(progression);
    final List<Integer> eight = open.traversal().connections(new Vector3f(-249, -6, -57), Continent.NORTH_SERDIO_1, 0, open.view()).stream().map(WorldMapTraversal.Connection::portalIndex).toList();
    equal(List.of(16, 17, 18, 120, 121, 122, 123, 124), eight, "open policy dynamic eight-way junction");
    final WorldMapPortal portal = definition.portal(0);
    final WorldMapRuntime standard = new WorldMapRuntime(definition, new WorldMapRules.Builder().build());
    standard.resolve(progression);
    equal(WorldMapAccess.Code.STORY_LOCKED, standard.access(portal.id(), WorldMapAction.SEE, portal.continent()).code(), "default story denial");
    equal(WorldMapAccess.Code.NO_PATH, open.access(definition.portal(79).id(), WorldMapAction.ENTER, Continent.NONE_8).code(), "open keeps no-path denial");
    equal(WorldMapAccess.Code.WRONG_CONTINENT, open.access(portal.id(), WorldMapAction.TRAVERSE, Continent.NONE_8).code(), "open keeps continent denial");
    for(final WorldMapTravel.Capability capability : WorldMapTravel.Capability.values()) {
      check(!standard.view().hasCapability(capability), "default capability locked " + capability);
      check(!open.view().hasCapability(capability), "open does not grant capability " + capability);
    }
    final WorldMapRules rules = new WorldMapRules.Builder().policy(WorldMapPolicy.OPEN).portal(portal.id(), (p, action, snapshot) -> action == WorldMapAction.SEE ? WorldMapAccess.ALLOWED : WorldMapAccess.denied("blocked " + action)).build();
    final WorldMapRuntime custom = new WorldMapRuntime(definition, rules);
    custom.resolve(progression);
    check(custom.access(portal.id(), WorldMapAction.SEE, portal.continent()).allowed(), "independent SEE");
    for(final WorldMapAction action : new WorldMapAction[]{WorldMapAction.TRAVERSE, WorldMapAction.ENTER}) {
      final WorldMapAccess access = custom.access(portal.id(), action, portal.continent());
      equal(WorldMapAccess.Code.RULE_LOCKED, access.code(), "independent explicit denial " + action);
      equal("blocked " + action, access.reason(), "denial explanation " + action);
    }
    for(final WorldMapAction granted : WorldMapAction.values()) {
      final WorldMapRuntime isolated = new WorldMapRuntime(definition, new WorldMapRules.Builder().portal(portal.id(), (p, action, snapshot) -> action == granted ? WorldMapAccess.ALLOWED : WorldMapAccess.denied("independent " + action)).build());
      isolated.resolve(progression);
      for(final WorldMapAction action : WorldMapAction.values()) equal(action == granted, isolated.access(portal.id(), action, portal.continent()).allowed(), "only " + granted + " grants " + action);
    }
    final WorldMapView old = standard.view();
    locations.set(0, true);
    story.set(0x97, true);
    story.set(0x15a, true);
    check(!old.progression().locationEnabled(0), "snapshot detached location flags");
    check(!old.progression().storyFlag(0x97), "snapshot detached story flags");
    check(standard.needsRefresh(story, locations), "changed flags require refresh");
    standard.resolve(new WorldMapProgression.Builder(story, locations).build());
    check(standard.access(portal.id(), WorldMapAction.ENTER, portal.continent()).allowed(), "new snapshot sees location flag");
    check(!old.access(0, WorldMapAction.ENTER).allowed(), "old resolved access immutable");
    for(final WorldMapTravel.Capability capability : WorldMapTravel.Capability.values()) check(standard.view().hasCapability(capability), "explicit story capability " + capability);
    check(!standard.validateIntent(portal.id(), WorldMapAction.ENTER, portal.continent(), old.version()).allowed(), "stale intent denied");
    check(standard.validateIntent(portal.id(), WorldMapAction.ENTER, portal.continent(), standard.view().version()).allowed(), "fresh intent accepted");
    check(!standard.needsRefresh(story, locations), "resolved flags current");
    standard.invalidate();
    check(standard.needsRefresh(story, locations), "explicit invalidation");
  }

  private static void invalidDefinitions(final WorldMapDefinition definition) {
    final int[] lengths = definition.pathLengths();
    lengths[0]++;
    rejects(() -> LegacyWorldMap.importDefinition(definition.locationData(), definition.placeData(), definition.directionalPathData(), definition.pathData(), lengths), "mismatched geometry length");
    final Vector3f[][] nonFinite = definition.pathData();
    nonFinite[0][0].x = Float.NaN;
    rejects(() -> LegacyWorldMap.importDefinition(definition.locationData(), definition.placeData(), definition.directionalPathData(), nonFinite, definition.pathLengths()), "non-finite geometry");
    final DirectionalPathSegmentData08[] routes = definition.directionalPathData();
    routes[0] = routes[routes.length - 1];
    rejects(() -> LegacyWorldMap.importDefinition(definition.locationData(), definition.placeData(), routes, definition.pathData(), definition.pathLengths()), "early route sentinel");
    final WorldMapRoute route = definition.route(0);
    rejects(() -> definition.toBuilder().replaceRoute(new WorldMapRoute(route.id(), route.legacyIndex(), route.start(), route.end(), route.segmentIndex(), 0, route.encounterRate(), route.battleStage(), route.encounterIndex(), route.modelIndex())).build(), "invalid route direction");
    rejects(() -> new WorldMapRuntime(definition, new WorldMapRules.Builder().portal(new RegistryId("test", "missing"), (p, a, s) -> WorldMapAccess.ALLOWED).build()), "unknown portal rule");
  }

  private static void positionEqual(final GameState52c expected, final GameState52c actual, final String context) {
    equal(expected.pathIndex_4d8, actual.pathIndex_4d8, context + " path");
    equal(expected.directionalPathIndex_4de, actual.directionalPathIndex_4de, context + " direction");
    equal(expected.dotIndex_4da, actual.dotIndex_4da, context + " dot");
    equal(expected.dotOffset_4dc, actual.dotOffset_4dc, context + " offset");
    equal(expected.facing_4dd, actual.facing_4dd, context + " facing");
  }

  private static void saves(final WorldMapDefinition definition) {
    final GameState52c source = new GameState52c();
    final GameState52c restored = new GameState52c();
    for(final WorldMapRoute route : definition.routes()) {
      source.pathIndex_4d8 = route.segmentIndex();
      source.directionalPathIndex_4de = route.legacyIndex();
      source.dotIndex_4da = definition.geometry().get(route.segmentIndex()).size() - 2;
      source.dotOffset_4dc = 3.75f;
      source.facing_4dd = route.direction();
      final MapTag legacy = WorldMapSave.write(source, null).asMap();
      check(!legacy.has("schemaVersion") && !legacy.has("routeId"), "legacy save remains unversioned " + route.id());
      equal(null, WorldMapSave.read(restored, legacy), "legacy save has no route ID " + route.id());
      positionEqual(source, restored, "legacy round trip " + route.id());
      final MapTag tagged = WorldMapSave.write(source, definition).asMap();
      equal(1, tagged.get("schemaVersion").asInt().get(), "tagged schema " + route.id());
      equal(route.id(), WorldMapSave.read(restored, tagged), "stable route metadata " + route.id());
      WorldMapSave.restoreRoute(restored, definition, route.id());
      positionEqual(source, restored, "tagged round trip " + route.id());
    }
    equal(null, WorldMapSave.read(restored, null), "missing world map save");
    equal(null, WorldMapSave.read(restored, new MapTag()), "empty world map save");

    source.pathIndex_4d8 = 0;
    source.directionalPathIndex_4de = 0;
    source.dotIndex_4da = 1;
    source.dotOffset_4dc = 1.25f;
    source.facing_4dd = -1;
    final MapTag tagged = WorldMapSave.write(source, definition).asMap();
    final List<WorldMapRoute> routes = new ArrayList<>(definition.routes());
    final WorldMapRoute first = routes.get(0);
    final WorldMapRoute third = routes.get(2);
    routes.set(0, withId(first, third.id()));
    routes.set(2, withId(third, first.id()));
    final WorldMapDefinition remapped = new WorldMapDefinition(definition.portals(), routes, definition.places(), definition.nodes(), definition.geometry());
    final RegistryId savedId = WorldMapSave.read(restored, tagged);
    WorldMapSave.restoreRoute(restored, remapped, savedId);
    equal(2, restored.directionalPathIndex_4de, "stable ID remaps route slot");
    equal(third.segmentIndex(), restored.pathIndex_4d8, "stable ID remaps geometry slot");
    equal(source.dotIndex_4da, restored.dotIndex_4da, "remap preserves dot");
    equal(source.dotOffset_4dc, restored.dotOffset_4dc, "remap preserves offset");
    equal(source.facing_4dd, restored.facing_4dd, "remap preserves facing");

    final MapTag unsupported = tagged.clone();
    unsupported.set("schemaVersion", new IntTag(2));
    rejects(() -> WorldMapSave.read(restored, unsupported), "unsupported save schema");
    final MapTag missingVersion = WorldMapSave.write(source, null).asMap();
    missingVersion.set("routeId", new RegistryIdTag(first.id()));
    rejects(() -> WorldMapSave.read(restored, missingVersion), "route metadata without schema");
    rejects(() -> WorldMapSave.restoreRoute(restored, definition, new RegistryId("test", "missing_route")), "unknown saved stable route");
    for(final int badDot : new int[]{-1, definition.geometry().get(0).size() - 1}) {
      restored.dotIndex_4da = badDot;
      rejects(() -> WorldMapSave.restoreRoute(restored, definition, first.id()), "invalid saved dot " + badDot);
    }
    restored.dotIndex_4da = 0;
    for(final float badOffset : new float[]{-0.01f, 4.0f, Float.NaN, Float.POSITIVE_INFINITY}) {
      restored.dotOffset_4dc = badOffset;
      rejects(() -> WorldMapSave.restoreRoute(restored, definition, first.id()), "invalid saved offset " + badOffset);
    }
  }

  private static WorldMapRoute withId(final WorldMapRoute route, final RegistryId id) {
    return new WorldMapRoute(id, route.legacyIndex(), route.start(), route.end(), route.segmentIndex(), route.direction(), route.encounterRate(), route.battleStage(), route.encounterIndex(), route.modelIndex());
  }

  private static void travel(final WorldMapDefinition definition) {
    for(int bits = 0; bits < 4; bits++) {
      final int enabled = bits;
      final java.util.function.IntPredicate flags = bit -> bit == 0x8f && (enabled & 1) != 0 || bit == 0x90 && (enabled & 2) != 0;
      final WorldMapTravel.Departure expected = (bits & 2) != 0 ? WorldMapTravel.Departure.LATER_QUEEN_FURY : (bits & 1) != 0 ? WorldMapTravel.Departure.FIRST_QUEEN_FURY : WorldMapTravel.Departure.NONE;
      equal(expected, WorldMapTravel.departure(new SubmapEndpoint(242, 3), flags), "Queen Fury departure flags " + bits);
      equal(WorldMapTravel.Departure.NONE, WorldMapTravel.departure(new SubmapEndpoint(241, 3), flags), "departure wrong cut " + bits);
      equal(WorldMapTravel.Departure.NONE, WorldMapTravel.departure(new SubmapEndpoint(242, 4), flags), "departure wrong scene " + bits);
    }
    final List<SubmapEndpoint> teleports = List.of(new SubmapEndpoint(528, 13), new SubmapEndpoint(528, 14), new SubmapEndpoint(528, 15), new SubmapEndpoint(540, 19), new SubmapEndpoint(572, 23));
    for(final SubmapEndpoint endpoint : teleports) check(WorldMapTravel.isTeleportOrigin(endpoint), "teleport origin " + endpoint);
    for(final SubmapEndpoint endpoint : List.of(new SubmapEndpoint(528, 12), new SubmapEndpoint(528, 16), new SubmapEndpoint(540, 18), new SubmapEndpoint(572, 24), new SubmapEndpoint(529, 13))) check(!WorldMapTravel.isTeleportOrigin(endpoint), "not teleport origin " + endpoint);
    check(WorldMapTravel.isCoolonOrigin(new SubmapEndpoint(529, 41)), "Coolon origin");
    check(!WorldMapTravel.isCoolonOrigin(new SubmapEndpoint(529, 40)), "Coolon wrong scene");
    check(!WorldMapTravel.isCoolonOrigin(new SubmapEndpoint(528, 41)), "Coolon wrong cut");
    for(final WorldMapPortal portal : definition.portals()) {
      equal(portal.from(), WorldMapTravel.destination(portal, true), "arrival destination " + portal.legacyIndex());
      equal(portal.to(), WorldMapTravel.destination(portal, false), "entry destination " + portal.legacyIndex());
    }
    for(final WorldMapTravel.Capability capability : WorldMapTravel.Capability.values()) {
      final int ownFlag = capability == WorldMapTravel.Capability.QUEEN_FURY_BOARDING ? 0x97 : 0x15a;
      check(WorldMapTravel.hasCapability(capability, bit -> bit == ownFlag), "transport own flag " + capability);
      check(!WorldMapTravel.hasCapability(capability, bit -> bit != ownFlag), "transport unrelated flags " + capability);
    }
    final Flags locations = new Flags(8);
    final WorldMapRuntime runtime = new WorldMapRuntime(definition, new WorldMapRules.Builder().build());
    runtime.resolve(new WorldMapProgression.Builder(new Flags(256), locations).build());
    check(!runtime.arrivalAllowed(93), "Queen Fury slot 93 story locked");
    locations.set(93, true);
    runtime.resolve(new WorldMapProgression.Builder(new Flags(256), locations).build());
    check(runtime.arrivalAllowed(93), "Queen Fury slot 93 arrival allowed without route");
    equal(WorldMapAccess.Code.NO_PATH, runtime.access(definition.portal(93).id(), WorldMapAction.TRAVERSE, definition.portal(93).continent()).code(), "slot 93 arrival does not create traversable route");
    final WorldMapRuntime denied = new WorldMapRuntime(definition, new WorldMapRules.Builder().policy(WorldMapPolicy.OPEN).portal(definition.portal(93).id(), (p, a, s) -> WorldMapAccess.denied("arrival denied")).build());
    denied.resolve(new WorldMapProgression.Builder(new Flags(256), locations).build());
    check(!denied.arrivalAllowed(93), "open policy respects explicit arrival denial");
  }

  private static void objectives(final WorldMapDefinition definition) {
    for(int preset = -1; preset < 49; preset++) {
      final Flags story = new Flags(256);
      if(preset >= 0) story.set(WmapStatics.wmapDestinationMarkers_800f5a6c[preset].packedFlag_00, true);
      final int selected = legacySelect(story);
      final WMapDestinationMarker2c marker = selected <= 0 ? null : WmapStatics.wmapDestinationMarkers_800f5a6c[selected];
      final WorldMapObjective expected = marker == null ? null : new WorldMapObjective(marker.x_24, marker.y_26, WmapStatics.places_800f0234[marker.placeIndex_28].name_00);
      equal(expected, WorldMapObjective.legacy(story, definition), "legacy objective preset " + preset);
    }
    final Flags story = new Flags(256);
    final Flags locations = new Flags(8);
    locations.set(0, true);
    final WorldMapObjective custom = new WorldMapObjective(17, 29, "Independent objective");
    final WorldMapProgression.Builder builder = new WorldMapProgression.Builder(story, locations).objective(custom);
    final WorldMapProgression shown = builder.build();
    final WorldMapProgression hidden = builder.objective(null).build();
    equal(custom, shown.objective(), "objective override snapshot");
    equal(null, hidden.objective(), "objective can be hidden");
    check(!shown.equals(hidden), "objective contributes to snapshot equality");
    final WorldMapRuntime runtime = new WorldMapRuntime(definition, new WorldMapRules.Builder().build());
    runtime.resolve(shown);
    final WorldMapView old = runtime.view();
    runtime.resolve(hidden);
    for(int i = 0; i < definition.portals().size(); i++) {
      for(final WorldMapAction action : WorldMapAction.values()) equal(old.access(i, action), runtime.view().access(i, action), "objective independent of access " + i + '/' + action);
    }
    equal(custom, old.progression().objective(), "old objective view immutable");
    equal(null, runtime.view().progression().objective(), "new view hides objective");
  }

  private static WorldMapPoint translated(final WorldMapPoint point) {
    return new WorldMapPoint(point.x() + 100, point.y() + 20, point.z() - 70);
  }

  private static void replacementGeometry(final WorldMapDefinition definition) {
    final WorldMapPortal original = definition.portal(0);
    final WorldMapPortal removed = new WorldMapPortal(original.id(), original.legacyIndex(), null, null, original.from(), original.to(), original.junctionIndex(), original.continent(), original.fullBrightness(), original.effectFlags());
    final WorldMapDefinition withoutPortal = definition.toBuilder().replacePortal(removed).build();
    equal(-1, withoutPortal.locationData()[0].directionalPathIndex_00, "removed route binding projects as absent");
    equal(-1, withoutPortal.locationData()[0].placeIndex_02, "removed place binding projects as absent");
    final WorldMapDefinition.Builder builder = definition.toBuilder();
    for(final WorldMapNode node : definition.nodes()) builder.node(new WorldMapNode(node.id(), translated(node.position())));
    for(int i = 0; i < definition.geometry().size(); i++) builder.geometry(i, definition.geometry().get(i).stream().map(WorldMapCompatibilityCheck::translated).toList());
    final List<WorldMapPoint> extended = new ArrayList<>(definition.geometry().get(0).stream().map(WorldMapCompatibilityCheck::translated).toList());
    extended.add(1, new WorldMapPoint(800, 14, 550));
    builder.geometry(0, extended);
    final WorldMapDefinition changed = builder.build();
    extended.clear();
    equal(definition.geometry().get(0).size() + 1, changed.pathLengths()[0], "replacement geometry recalculates path length");
    equal(translated(definition.geometry().get(0).getFirst()), changed.geometry().get(0).getFirst(), "replacement endpoint moved");
    for(final WorldMapNode node : definition.nodes()) equal(translated(node.position()), changed.node(node.id()).position(), "replacement node " + node.id());
    equal(WmapStatics.pathDotPosArr_800f591c[0][0], definition.pathData()[0][0], "builder leaves source geometry immutable");
    final WorldMapRuntime runtime = new WorldMapRuntime(changed, new WorldMapRules.Builder().policy(WorldMapPolicy.OPEN).build());
    runtime.resolve(new WorldMapProgression.Builder(new Flags(256), new Flags(8)).build());
    final WorldMapPoint endpoint = changed.geometry().get(0).getFirst();
    final List<WorldMapTraversal.Connection> connections = runtime.traversal().connections(new Vector3f(endpoint.x(), endpoint.y(), endpoint.z()), Continent.SOUTH_SERDIO_0, 0, runtime.view());
    check(connections.stream().anyMatch(c -> c.routeIndex() == 0 && c.nextPoint().equals(changed.geometry().get(0).get(1))), "rebuilt adjacency uses replacement interior point");
    final List<WorldMapPoint> mismatch = new ArrayList<>(definition.geometry().get(0));
    mismatch.set(0, translated(mismatch.getFirst()));
    rejects(() -> definition.toBuilder().geometry(0, mismatch).build(), "geometry endpoint mismatch");
    rejects(() -> definition.toBuilder().geometry(0, List.of(definition.geometry().get(0).getFirst())).build(), "one-point replacement geometry");
    final WorldMapNode first = definition.nodes().getFirst();
    rejects(() -> definition.toBuilder().node(new WorldMapNode(first.id(), new WorldMapPoint(Float.POSITIVE_INFINITY, 0, 0))).build(), "nonfinite replacement node");
    rejects(() -> definition.toBuilder().node(new WorldMapNode(new RegistryId("test", "missing_node"), first.position())), "unknown replacement node");
    final List<WorldMapPoint> nonFinite = new ArrayList<>(definition.geometry().get(0));
    nonFinite.set(1, new WorldMapPoint(0, Float.NaN, 0));
    rejects(() -> definition.toBuilder().geometry(0, nonFinite).build(), "nonfinite replacement geometry");
  }

  private static void dynamicAvailability(final WorldMapDefinition definition) {
    final RegistryId unlocked = new RegistryId("test", "route_unlocked");
    final WorldMapPortal portal = definition.portal(0);
    final WorldMapRules rules = new WorldMapRules.Builder().portal(portal.id(), (p, action, snapshot) -> snapshot.hasFact(unlocked) ? WorldMapAccess.ALLOWED : WorldMapAccess.denied("missing route fact")).build();
    final WorldMapRuntime runtime = new WorldMapRuntime(definition, rules);
    final WorldMapProgression.Builder builder = new WorldMapProgression.Builder(new Flags(256), new Flags(8)).fact(unlocked, false);
    runtime.resolve(builder.build());
    final WorldMapView locked = runtime.view();
    final WorldMapRoute route = definition.route(portal.route());
    final WorldMapPoint point = definition.node(route.start()).position();
    final Vector3f endpoint = new Vector3f(point.x(), point.y(), point.z());
    equal(-1, runtime.findRoutePortal(route.legacyIndex(), portal.continent()), "unavailable route returns no portal");
    check(runtime.traversal().connections(endpoint, portal.continent(), 0, locked).isEmpty(), "zero-candidate junction");
    builder.fact(unlocked, true);
    check(!locked.progression().hasFact(unlocked), "fact builder cannot mutate existing snapshot");
    runtime.resolve(builder.build());
    equal(0, runtime.findRoutePortal(route.legacyIndex(), portal.continent()), "dynamic rule enables route portal");
    equal(List.of(0), runtime.traversal().connections(endpoint, portal.continent(), 0, runtime.view()).stream().map(WorldMapTraversal.Connection::portalIndex).toList(), "dynamic rule enables ordered candidate");
    check(runtime.traversal().connections(endpoint, portal.continent(), 0, locked).isEmpty(), "old view retains zero candidates");
    runtime.resolve(builder.fact(unlocked, false).build());
    equal(-1, runtime.findRoutePortal(route.legacyIndex(), portal.continent()), "dynamic relock removes route portal");
    check(runtime.traversal().connections(endpoint, portal.continent(), 0, runtime.view()).isEmpty(), "dynamic relock removes junction candidate");
  }
}
