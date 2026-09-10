package legend.game.wmap.world;

import legend.game.wmap.Continent;
import legend.game.wmap.WmapStatics;
import legend.game.types.Flags;
import legend.lodmod.LodEncounters;
import legend.lodmod.LodMod;
import legend.game.wmap.registries.RegisterWorldMapNodesEvent;
import legend.game.wmap.registries.WorldMapDataEntry;
import legend.game.wmap.registries.WorldMapNodeEntry;
import legend.game.wmap.registries.WorldMapNodeRegistry;
import org.junit.jupiter.api.Test;
import org.legendofdragoon.modloader.events.Event;
import org.legendofdragoon.modloader.events.EventManager;
import org.legendofdragoon.modloader.registries.MutableRegistry;
import org.legendofdragoon.modloader.registries.Registrar;
import org.legendofdragoon.modloader.registries.Registries;
import org.legendofdragoon.modloader.registries.Registry;
import org.legendofdragoon.modloader.registries.RegistryDelegate;
import org.legendofdragoon.modloader.registries.RegistryId;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WorldMapRegistryTest {
  private static final class RetailRegistries extends legend.core.Registries {
    private RetailRegistries(final EventManager events, final java.util.function.Consumer<Access> access) {
      super(events, access);
    }
  }

  private static final class RetailEvents extends EventManager {
    private final java.util.function.Consumer<Event> extra;

    private RetailEvents(final java.util.function.Consumer<Event> extra) {
      super(access -> { }, (mod, error) -> { throw new AssertionError(error); });
      this.extra = extra;
    }

    @Override
    public <T extends Event> T postEvent(final T event) {
      // Call the exact public retail forwarder, without scanning mods or booting the engine.
      final var handlers = java.util.Arrays.stream(LodMod.class.getDeclaredMethods()).filter(method -> method.getName().startsWith("register") && method.getParameterCount() == 1 && method.getParameterTypes()[0] == event.getClass()).toList();
      assertEquals(1, handlers.size(), "one retail registration forwarder for " + event.getClass());
      try {
        handlers.getFirst().invoke(null, event);
      } catch(final ReflectiveOperationException exception) {
        throw new AssertionError("Retail registration failed for " + event.getClass(), exception);
      }
      this.extra.accept(event);
      return event;
    }
  }

  private static RetailRegistries bootstrap(final java.util.function.Consumer<Event> extra) {
    final AtomicReference<Registries.Access> access = new AtomicReference<>();
    final RetailRegistries registries = new RetailRegistries(new RetailEvents(extra), access::set);
    for(final Registry<?> registry : List.of(registries.encounters, registries.worldMapNodes, registries.worldMapGeometry, registries.worldMapRoutes, registries.worldMapPlaces, registries.worldMapPortals, registries.worldMapStoryPresets, registries.worldMapCoolonDestinations, registries.worldMapTeleportLinks, registries.worldMapEncounterPools, registries.worldMapPresentationProfiles, registries.worldMapBehaviours)) access.get().initialize(registry);
    return registries;
  }

  @Test
  void bootstrapsRetailSnapshotWithoutGraphicsAndDetachesLegacySources() throws IllegalAccessException {
    final RetailRegistries registries = bootstrap(event -> { });
    final WorldMapDefinition legacy = LegacyWorldMap.importDefinition();
    final WorldMapRegistrySnapshot snapshot = WorldMapRegistrySnapshot.read(registries);
    final WorldMapDefinition actual = snapshot.definition();
    final WorldMapDefinition.Builder definitionBuilder = actual.toBuilder();
    final WorldMapRules.Builder ruleBuilder = new WorldMapRules.Builder();
    snapshot.configureBehaviours(registries, definitionBuilder, ruleBuilder);
    final WorldMapRuntime runtime = new WorldMapRuntime(definitionBuilder.build(), ruleBuilder.build());
    runtime.resolve(new WorldMapProgression.Builder(new Flags(256), new Flags(8)).build());
    final List<SubmapEndpoint> origins = new ArrayList<>(actual.portals().stream().map(WorldMapPortal::from).toList());
    origins.addAll(List.of(new SubmapEndpoint(528, 12), new SubmapEndpoint(528, 16), new SubmapEndpoint(529, 40), new SubmapEndpoint(539, 0)));
    for(final SubmapEndpoint origin : origins) {
      final WorldMapTravel.Arrival expected = WorldMapTravel.isTeleportOrigin(origin) ? WorldMapTravel.Arrival.TELEPORT : WorldMapTravel.isCoolonOrigin(origin) ? WorldMapTravel.Arrival.COOLON : WorldMapTravel.Arrival.NORMAL;
      assertEquals(expected, snapshot.arrival(origin), "registered arrival " + origin);
      assertEquals(expected, runtime.arrival(origin), "configured arrival " + origin);
    }
    assertEquals(WorldMapTravel.Arrival.NORMAL, snapshot.arrival(actual.portal(74).from()), "source 74 is a link without auto-start");
    for(int bits = 0; bits < 16; bits++) {
      final Flags story = new Flags(256);
      story.set(0x8f, (bits & 1) != 0);
      story.set(0x90, (bits & 2) != 0);
      story.set(0x97, (bits & 4) != 0);
      story.set(0x15a, (bits & 8) != 0);
      runtime.resolve(new WorldMapProgression.Builder(story, new Flags(8)).build());
      assertEquals(WorldMapTravel.departure(new SubmapEndpoint(242, 3), story::get), runtime.departure(new SubmapEndpoint(242, 3)));
      assertEquals(WorldMapTravel.Departure.NONE, runtime.departure(new SubmapEndpoint(242, 4)));
      for(final WorldMapTravel.Capability capability : WorldMapTravel.Capability.values()) assertEquals(WorldMapTravel.hasCapability(capability, story::get), runtime.view().hasCapability(capability));
    }
    assertEquals(legacy.portals(), actual.portals());
    assertEquals(legacy.routes(), actual.routes());
    assertEquals(legacy.places(), actual.places());
    assertEquals(legacy.geometry(), actual.geometry());
    for(final WorldMapNode node : legacy.nodes()) assertEquals(node, actual.node(node.id()));
    assertArrayEquals(legacy.pathLengths(), actual.pathLengths());
    projectionFields(legacy.locationData(), actual.locationData());
    projectionFields(legacy.placeData(), actual.placeData());
    projectionFields(legacy.directionalPathData(), actual.directionalPathData());
    for(int preset = 0; preset < 49; preset++) {
      final Flags story = new Flags(256);
      story.set(WmapStatics.wmapDestinationMarkers_800f5a6c[preset].packedFlag_00, true);
      int selected = -1;
      for(int row = 0; row < 49; row++) {
        if(story.get(WmapStatics.wmapDestinationMarkers_800f5a6c[row].packedFlag_00)) selected = row;
      }
      final Flags locations = new Flags(8);
      snapshot.applyStory(story, locations, actual);
      for(int word = 0; word < 8; word++) assertEquals(WmapStatics.wmapDestinationMarkers_800f5a6c[selected].flags_04[word], locations.getRaw(word), "story " + preset + " word " + word);
      assertEquals(WorldMapObjective.legacy(story, legacy), snapshot.objective(story, actual));
    }
    projectionFields(WmapStatics.coolonWarpDest_800ef228, snapshot.coolonData(actual));
    assertFalse(snapshot.coolon().get(8).data().worldMapArrival());
    final int[][] endpoints = snapshot.teleportEndpoints(actual);
    assertEquals(WmapStatics.teleportationEndpointIndices_800ef698.length, endpoints.length);
    final var positions = snapshot.teleportLocations(actual);
    for(int i = 0; i < endpoints.length; i++) {
      assertArrayEquals(WmapStatics.teleportationEndpointIndices_800ef698[i], endpoints[i]);
      final int source = endpoints[i][0];
      final var expected = java.util.Arrays.stream(WmapStatics.teleportationLocations_800ef6c8).filter(position -> position.locationIndex_00 == source).findFirst().orElseThrow();
      assertEquals(source, positions[i].locationIndex_00);
      assertEquals(expected.translation_04, positions[i].translation_04);
    }
    for(int pool = 0; pool < WmapStatics.encounterIds_800ef364.length; pool++) {
      for(final int roll : new int[]{0, 34, 35, 69, 70, 89, 90, 99}) {
        final int slot = roll < 35 ? 0 : roll < 70 ? 1 : roll < 90 ? 2 : 3;
        assertEquals(new RegistryId("lod", LodEncounters.LEGACY[WmapStatics.encounterIds_800ef364[pool][slot]]), snapshot.encounter(pool, roll), "pool=" + pool + " roll=" + roll);
      }
    }
    assertEquals(new RegistryId("lod", LodEncounters.LEGACY[WmapStatics.encounterIds_800ef364[0][0]]), snapshot.encounter(-1, 99), "sentinel pool forces first choice");

    final String name = WmapStatics.places_800f0234[0].name_00;
    final int cut = WmapStatics.locations_800f0e34[0].submapCutTo_08;
    final org.joml.Vector3f point = new org.joml.Vector3f(WmapStatics.pathDotPosArr_800f591c[0][0]);
    final int storyWord = WmapStatics.wmapDestinationMarkers_800f5a6c[0].flags_04[0];
    final int encounter = WmapStatics.encounterIds_800ef364[0][0];
    final org.joml.Vector3f coolon = new org.joml.Vector3f(WmapStatics.coolonWarpDest_800ef228[0].destPosition_00);
    final org.joml.Vector3i teleport = new org.joml.Vector3i(WmapStatics.teleportationLocations_800ef6c8[0].translation_04);
    final org.joml.Vector3i camera = new org.joml.Vector3i(WmapStatics.mapPositions_800ef1a8[0]);
    final String region = WmapStatics.regions_800f01ec[0];
    final String service = WmapStatics.services_800f01cc[0];
    final int water = WmapStatics.waterClutYs_800ef348[0];
    final int avatar = WmapStatics.playerAvatarVramSlots_800ef694[0];
    final var uv = WmapStatics.tmdUvAdjustmentMetrics_800eee48[0];
    try {
      WmapStatics.places_800f0234[0].name_00 = "mutated after registration";
      WmapStatics.locations_800f0e34[0].submapCutTo_08 = 9999;
      WmapStatics.pathDotPosArr_800f591c[0][0].zero();
      WmapStatics.wmapDestinationMarkers_800f5a6c[0].flags_04[0] = 0;
      WmapStatics.encounterIds_800ef364[0][0] = 0;
      WmapStatics.coolonWarpDest_800ef228[0].destPosition_00.zero();
      WmapStatics.teleportationLocations_800ef6c8[0].translation_04.zero();
      WmapStatics.mapPositions_800ef1a8[0].zero();
      WmapStatics.regions_800f01ec[0] = "changed region";
      WmapStatics.services_800f01cc[0] = "changed service";
      WmapStatics.waterClutYs_800ef348[0] = -1;
      WmapStatics.playerAvatarVramSlots_800ef694[0] = -1;
      WmapStatics.tmdUvAdjustmentMetrics_800eee48[0] = new legend.game.tmd.UvAdjustmentMetrics14(0, 0, 0, 0, 0);
      final WorldMapRegistrySnapshot rebuilt = WorldMapRegistrySnapshot.read(registries);
      assertEquals(actual.places(), rebuilt.definition().places());
      assertEquals(actual.portals(), rebuilt.definition().portals());
      assertEquals(actual.geometry(), rebuilt.definition().geometry());
      assertEquals(snapshot.story(), rebuilt.story());
      assertEquals(snapshot.coolon(), rebuilt.coolon());
      projectionFields(snapshot.teleportLocations(actual), rebuilt.teleportLocations(rebuilt.definition()));
      assertEquals(snapshot.encounter(0, 0), rebuilt.encounter(0, 0));
      assertEquals(snapshot.presentation(), rebuilt.presentation(), "registered presentation detached from all source arrays");
    } finally {
      WmapStatics.places_800f0234[0].name_00 = name;
      WmapStatics.locations_800f0e34[0].submapCutTo_08 = cut;
      WmapStatics.pathDotPosArr_800f591c[0][0].set(point);
      WmapStatics.wmapDestinationMarkers_800f5a6c[0].flags_04[0] = storyWord;
      WmapStatics.encounterIds_800ef364[0][0] = encounter;
      WmapStatics.coolonWarpDest_800ef228[0].destPosition_00.set(coolon);
      WmapStatics.teleportationLocations_800ef6c8[0].translation_04.set(teleport);
      WmapStatics.mapPositions_800ef1a8[0].set(camera);
      WmapStatics.regions_800f01ec[0] = region;
      WmapStatics.services_800f01cc[0] = service;
      WmapStatics.waterClutYs_800ef348[0] = water;
      WmapStatics.playerAvatarVramSlots_800ef694[0] = avatar;
      WmapStatics.tmdUvAdjustmentMetrics_800eee48[0] = uv;
    }
  }

  private static void projectionFields(final Object[] expected, final Object[] actual) throws IllegalAccessException {
    assertEquals(expected.length, actual.length);
    for(int i = 0; i < expected.length; i++) {
      for(final var field : expected[i].getClass().getFields()) {
        if(java.lang.reflect.Modifier.isStatic(field.getModifiers())) continue;
        final Object a = field.get(expected[i]);
        final Object b = field.get(actual[i]);
        if(a instanceof final int[] array) assertArrayEquals(array, (int[])b, "row " + i + " field " + field.getName());
        else assertEquals(a, b, "row " + i + " field " + field.getName());
      }
    }
  }

  @Test
  void registeredBehaviourAndStableRouteReferencesConfigureRuntime() {
    final WorldMapDefinition vanilla = LegacyWorldMap.importDefinition();
    final WorldMapRoute original = vanilla.route(0);
    final RegistryId geometryId = id("extra_geometry");
    final RegistryId poolId = id("extra_pool");
    final RegistryId routeId = id("extra_route");
    final RegistryId portalId = vanilla.portal(159).id();
    final RegistryId encounterId = new RegistryId("lod", LodEncounters.LEGACY[0]);
    final RetailRegistries registries = bootstrap(event -> {
      if(event instanceof final legend.game.wmap.registries.RegisterWorldMapGeometryEvent geometry) {
        geometry.register(geometryId, new legend.game.wmap.registries.WorldMapGeometryEntry(effective -> new WorldMapGeometry(vanilla.geometry().size(), vanilla.geometry().get(0))));
      }
      if(event instanceof final legend.game.wmap.registries.RegisterWorldMapEncounterPoolsEvent pools) {
        pools.register(poolId, new legend.game.wmap.registries.WorldMapEncounterPoolEntry(effective -> new WorldMapEncounterPool(WmapStatics.encounterIds_800ef364.length, List.of(encounterId, encounterId, encounterId, encounterId))));
      }
      if(event instanceof final legend.game.wmap.registries.RegisterWorldMapRoutesEvent routes) {
        routes.register(routeId, new legend.game.wmap.registries.WorldMapRouteEntry(effective -> new WorldMapRouteData(vanilla.routes().size(), original.start(), original.end(), geometryId, 1, 1, original.battleStage(), poolId, 0)));
      }
      if(event instanceof final legend.game.wmap.registries.RegisterWorldMapPortalsEvent portals) {
        portals.register(id("portal_replacement"), new legend.game.wmap.registries.WorldMapPortalEntry(portalId, 20, effective -> new WorldMapPortal(effective, 159, routeId, vanilla.places().getFirst().id(), new SubmapEndpoint(999, 1), new SubmapEndpoint(999, 2), 159, Continent.SOUTH_SERDIO_0, false, 0)));
      }
      if(event instanceof final legend.game.wmap.registries.RegisterWorldMapBehavioursEvent behaviours) {
        behaviours.register(id("custom_behaviour"), new WorldMapBehaviour(50, (definition, rules) -> {
          rules.arrival((origin, progression) -> WorldMapTravel.Arrival.COOLON);
          rules.departure((origin, progression) -> WorldMapTravel.Departure.LATER_QUEEN_FURY);
          rules.capability(WorldMapTravel.Capability.COOLON, progression -> true);
          rules.capability(WorldMapTravel.Capability.QUEEN_FURY_BOARDING, progression -> true);
          rules.policy(WorldMapPolicy.OPEN);
          rules.portal(portalId, (portal, action, progression) -> action == WorldMapAction.TRAVERSE ? WorldMapAccess.ALLOWED : WorldMapAccess.denied("custom " + action));
          definition.replacePlace(vanilla.places().getFirst().withName("Registered behaviour place"));
        }));
      }
    });
    final WorldMapRegistrySnapshot snapshot = WorldMapRegistrySnapshot.read(registries);
    final WorldMapRoute compiled = snapshot.definition().route(routeId);
    assertEquals(vanilla.geometry().size(), compiled.segmentIndex(), "stable geometry reference compiled to adapter index");
    assertEquals(WmapStatics.encounterIds_800ef364.length, compiled.encounterIndex(), "stable pool reference compiled to adapter index");
    assertEquals(routeId, snapshot.definition().portal(159).route());
    assertEquals(encounterId, snapshot.encounter(compiled.encounterIndex(), 99));
    final WorldMapDefinition.Builder definition = snapshot.definition().toBuilder();
    final WorldMapRules.Builder rules = new WorldMapRules.Builder();
    snapshot.configureBehaviours(registries, definition, rules);
    final WorldMapRuntime runtime = new WorldMapRuntime(definition.build(), rules.build());
    runtime.resolve(new WorldMapProgression.Builder(new Flags(256), new Flags(8)).build());
    assertEquals("Registered behaviour place", runtime.definition().places().getFirst().name());
    assertEquals(WorldMapTravel.Arrival.COOLON, runtime.arrival(new SubmapEndpoint(999, 1)));
    assertEquals(WorldMapTravel.Departure.LATER_QUEEN_FURY, runtime.departure(new SubmapEndpoint(999, 1)));
    for(final WorldMapTravel.Capability capability : WorldMapTravel.Capability.values()) assertTrue(runtime.view().hasCapability(capability));
    for(final WorldMapAction action : WorldMapAction.values()) {
      final WorldMapAccess result = runtime.access(portalId, action, Continent.SOUTH_SERDIO_0);
      assertEquals(action == WorldMapAction.TRAVERSE, result.allowed());
      if(action != WorldMapAction.TRAVERSE) assertEquals("custom " + action, result.reason());
    }
    assertTrue(runtime.access(vanilla.portal(0).id(), WorldMapAction.ENTER, vanilla.portal(0).continent()).allowed(), "registered OPEN policy applies to unoverridden portal");
  }

  @Test
  void coolonFallbackFollowsRegisteredOriginAfterReordering() {
    final WorldMapRegistrySnapshot vanilla = WorldMapRegistrySnapshot.read(bootstrap(event -> { }));
    final WorldMapRegistrySnapshot.Value<WorldMapCoolonDestination> menuOrigin = vanilla.coolon().get(8);
    final WorldMapCoolonDestination value = menuOrigin.data();
    final SubmapEndpoint origin = vanilla.definition().portal(value.portal()).from();
    assertEquals(8, vanilla.coolonOriginFallback(origin, vanilla.definition()));
    final WorldMapRegistrySnapshot reordered = WorldMapRegistrySnapshot.read(bootstrap(event -> {
      if(event instanceof final legend.game.wmap.registries.RegisterWorldMapCoolonDestinationsEvent destinations) {
        destinations.register(id("first_menu_origin"), new legend.game.wmap.registries.WorldMapCoolonDestinationEntry(menuOrigin.id(), 20, effective -> new WorldMapCoolonDestination(-1, value.portal(), value.defaultDestination(), value.position(), value.x(), value.y(), value.label(), value.worldMapArrival(), true)));
      }
    }));
    assertEquals(0, reordered.coolonOriginFallback(origin, reordered.definition()));
    assertEquals(menuOrigin.id(), reordered.coolon().getFirst().id());
    assertEquals(0, reordered.coolonOriginFallback(new SubmapEndpoint(-1, -1), reordered.definition()));
  }

  @Test
  void revalidatesDefinitionAfterBehaviourConfiguration() {
    final WorldMapRegistrySnapshot snapshot = WorldMapRegistrySnapshot.read(bootstrap(event -> { }));
    final WorldMapDefinition original = snapshot.definition();
    snapshot.validateDefinition(original);
    final List<WorldMapPortal> tooMany = new ArrayList<>(original.portals());
    tooMany.add(new WorldMapPortal(id("overflow_portal"), 256, null, null, new SubmapEndpoint(0, 0), new SubmapEndpoint(0, 0), -1, Continent.NONE_8, false, 0));
    final WorldMapDefinition overflow = new WorldMapDefinition(tooMany, original.routes(), original.places(), original.nodes(), original.geometry());
    assertThrows(IllegalArgumentException.class, () -> snapshot.validateDefinition(overflow), "configured graph must still fit the 256-bit location save ABI");

    final RegistryId objectiveId = snapshot.story().stream().map(WorldMapStoryPreset::place).filter(java.util.Objects::nonNull).findFirst().orElseThrow();
    final WorldMapPlace objective = original.places().stream().filter(place -> place.id().equals(objectiveId)).findFirst().orElseThrow();
    final RegistryId renamedId = id("renamed_objective_place");
    final WorldMapDefinition.Builder renamed = original.toBuilder().replacePlace(new WorldMapPlace(renamedId, objective.legacyIndex(), objective.name(), objective.thumbnail(), objective.services(), objective.sounds()));
    for(final WorldMapPortal portal : original.portals()) {
      if(objectiveId.equals(portal.place())) renamed.replacePortal(portal.withPlace(renamedId));
    }
    final WorldMapDefinition missingObjective = renamed.build();
    assertThrows(IllegalArgumentException.class, () -> snapshot.validateDefinition(missingObjective), "configured graph must retain registered objective place references");

    final WorldMapRoute route = original.route(0);
    final WorldMapDefinition badPool = original.toBuilder().replaceRoute(new WorldMapRoute(route.id(), route.legacyIndex(), route.start(), route.end(), route.segmentIndex(), route.direction(), 1, route.battleStage(), Integer.MAX_VALUE, route.modelIndex())).build();
    assertThrows(IllegalArgumentException.class, () -> snapshot.validateDefinition(badPool), "configured encounter-bearing route must resolve its pool");
  }

  private static RegistryId id(final String name) {
    return new RegistryId("test", name);
  }

  private static WorldMapNodeEntry node(final int x) {
    return new WorldMapNodeEntry(effective -> new WorldMapNode(effective, new WorldMapPoint(x, 0, 0)));
  }

  @Test
  void resolvesScrambledRegistrationAndOverlaysByStableIdentity() {
    final RegistryId alpha = new RegistryId("lod", "alpha");
    final RegistryId zeta = new RegistryId("other_mod", "zeta");
    for(final boolean reversed : new boolean[]{false, true}) {
      final WorldMapNodeRegistry registry = new WorldMapNodeRegistry();
      final List<Runnable> registrations = new ArrayList<>();
      registrations.add(() -> registry.register(zeta, node(3)));
      registrations.add(() -> registry.register(id("low"), new WorldMapNodeEntry(alpha, -5, effective -> new WorldMapNode(effective, new WorldMapPoint(5, 0, 0)))));
      registrations.add(() -> registry.register(id("high"), new WorldMapNodeEntry(alpha, 9, effective -> new WorldMapNode(effective, new WorldMapPoint(9, 0, 0)))));
      registrations.add(() -> registry.register(alpha, node(1)));
      if(reversed) java.util.Collections.reverse(registrations);
      registrations.forEach(Runnable::run);
      final List<WorldMapRegistrySnapshot.Value<WorldMapNode>> values = WorldMapRegistrySnapshot.resolve(registry);
      assertEquals(List.of(alpha, zeta), values.stream().map(WorldMapRegistrySnapshot.Value::id).toList());
      assertEquals(alpha, values.getFirst().data().id(), "replacement inherits target identity, not contributing mod identity");
      assertEquals(new WorldMapPoint(9, 0, 0), values.getFirst().data().position());
      assertEquals(new WorldMapPoint(3, 0, 0), values.get(1).data().position());
      assertEquals(4, registry.size(), "resolution does not mutate registered recipes");
    }
  }

  @Test
  void rejectsMissingTargetsOverlayChainsAndAnyEqualPriorityConflict() {
    final WorldMapNodeRegistry missing = new WorldMapNodeRegistry();
    missing.register(id("overlay"), new WorldMapNodeEntry(id("absent"), 1, effective -> new WorldMapNode(effective, new WorldMapPoint(0, 0, 0))));
    assertThrows(IllegalArgumentException.class, () -> WorldMapRegistrySnapshot.resolve(missing));
    final WorldMapNodeRegistry chain = new WorldMapNodeRegistry();
    chain.register(id("base"), node(0));
    chain.register(id("overlay"), new WorldMapNodeEntry(id("base"), 1, effective -> new WorldMapNode(effective, new WorldMapPoint(1, 0, 0))));
    chain.register(id("chain"), new WorldMapNodeEntry(id("overlay"), 2, effective -> new WorldMapNode(effective, new WorldMapPoint(2, 0, 0))));
    assertThrows(IllegalArgumentException.class, () -> WorldMapRegistrySnapshot.resolve(chain));
    final WorldMapNodeRegistry conflict = new WorldMapNodeRegistry();
    conflict.register(id("base"), node(0));
    for(final String name : List.of("first", "second", "higher")) {
      conflict.register(id(name), new WorldMapNodeEntry(id("base"), name.equals("higher") ? 100 : 1, effective -> new WorldMapNode(effective, new WorldMapPoint(1, 0, 0))));
    }
    assertThrows(IllegalArgumentException.class, () -> WorldMapRegistrySnapshot.resolve(conflict), "a higher winner does not hide lower-priority ambiguity");
    assertThrows(NullPointerException.class, () -> new WorldMapDataEntry<Object>(effective -> null).create(id("null_data")));
    assertThrows(IllegalArgumentException.class, () -> new RegistryId("test", "wmap/invalid"));
  }

  /** Dispatch is isolated from mod scanning; registration, locking, delegates, and reset are real loader APIs. */
  private static final class TestEvents extends EventManager {
    private Registrar<WorldMapNodeEntry, RegisterWorldMapNodesEvent> registrar;

    private TestEvents() {
      super(access -> { }, (mod, error) -> { throw new AssertionError(error); });
    }

    @Override
    public <T extends Event> T postEvent(final T event) {
      if(event instanceof final RegisterWorldMapNodesEvent registration) this.registrar.registryEvent(registration);
      return event;
    }
  }

  private static final class TestRegistries extends Registries {
    private final Registry<WorldMapNodeEntry> nodes = this.addRegistry(new WorldMapNodeRegistry(), RegisterWorldMapNodesEvent::new);

    private TestRegistries(final EventManager events, final java.util.function.Consumer<Access> access) {
      super(events, access);
    }
  }

  @Test
  void realLifecycleLocksAndRefreshesStaticDelegatesAfterReset() {
    final TestEvents events = new TestEvents();
    final AtomicReference<Registries.Access> access = new AtomicReference<>();
    final TestRegistries registries = new TestRegistries(events, access::set);
    final Registrar<WorldMapNodeEntry, RegisterWorldMapNodesEvent> registrar = new Registrar<>(registries.nodes, "test");
    events.registrar = registrar;
    final AtomicInteger generation = new AtomicInteger();
    final RegistryDelegate<WorldMapNodeEntry> delegate = registrar.register("node", () -> node(generation.incrementAndGet()));
    assertFalse(delegate.isValid());
    assertThrows(RuntimeException.class, delegate::get);
    access.get().initialize(registries.nodes);
    assertTrue(delegate.isValid());
    final WorldMapNodeEntry old = delegate.get();
    assertEquals(1.0f, old.create(delegate.getId()).position().x());
    assertThrows(IllegalStateException.class, () -> access.get().initialize(registries.nodes));
    assertThrows(RuntimeException.class, () -> ((WorldMapNodeRegistry)registries.nodes).register(id("late"), node(2)));
    access.get().reset();
    assertFalse(delegate.isValid());
    assertThrows(RuntimeException.class, delegate::get);
    access.get().initializeRemaining();
    assertNotSame(old, delegate.get());
    assertEquals(2.0f, delegate.get().create(delegate.getId()).position().x());
    assertEquals(1.0f, old.create(delegate.getId()).position().x(), "prior immutable recipe remains detached");
    assertEquals(id("node"), delegate.get().getRegistryId());
  }

  private static <T> List<T> registered(final String name, final List<T> source, final java.util.function.Function<T, RegistryId> identity) {
    final MutableRegistry<WorldMapDataEntry<T>> registry = new MutableRegistry<>(id(name));
    for(int i = source.size() - 1; i >= 0; i--) {
      final T value = source.get(i);
      registry.register(identity.apply(value), new WorldMapDataEntry<>(effective -> value));
    }
    return WorldMapRegistrySnapshot.resolve(registry).stream().map(WorldMapRegistrySnapshot.Value::data).toList();
  }

  @Test
  void registeredModGeometryAndRouteCanFillUnusedLegacyPortal() {
    final WorldMapDefinition vanilla = LegacyWorldMap.importDefinition();
    final List<WorldMapNode> nodes = new ArrayList<>(vanilla.nodes());
    final WorldMapNode start = new WorldMapNode(id("new_start"), new WorldMapPoint(9000, 0, 0));
    final WorldMapNode end = new WorldMapNode(id("new_end"), new WorldMapPoint(9010, 0, 0));
    nodes.add(start);
    nodes.add(end);
    final List<WorldMapGeometry> geometry = new ArrayList<>();
    for(int i = 0; i < vanilla.geometry().size(); i++) geometry.add(new WorldMapGeometry(i, vanilla.geometry().get(i)));
    geometry.add(new WorldMapGeometry(geometry.size(), List.of(start.position(), end.position())));
    final List<WorldMapRoute> routes = new ArrayList<>(vanilla.routes());
    final WorldMapRoute route = new WorldMapRoute(id("new_route"), routes.size(), start.id(), end.id(), geometry.size() - 1, 1, 0, 0, 0, 0);
    routes.add(route);
    final List<WorldMapPortal> portals = new ArrayList<>(vanilla.portals());
    portals.set(159, new WorldMapPortal(id("new_portal"), 159, route.id(), vanilla.places().getFirst().id(), new SubmapEndpoint(999, 1), new SubmapEndpoint(999, 2), 159, Continent.SOUTH_SERDIO_0, false, 0));
    final List<WorldMapGeometry> resolvedGeometry = registered("geometry", geometry, g -> id("geometry_" + g.legacyIndex())).stream().sorted(Comparator.comparingInt(WorldMapGeometry::legacyIndex)).toList();
    final WorldMapDefinition extended = new WorldMapDefinition(
      registered("portals", portals, WorldMapPortal::id).stream().sorted(Comparator.comparingInt(WorldMapPortal::legacyIndex)).toList(),
      registered("routes", routes, WorldMapRoute::id).stream().sorted(Comparator.comparingInt(WorldMapRoute::legacyIndex)).toList(),
      registered("places", vanilla.places(), WorldMapPlace::id).stream().sorted(Comparator.comparingInt(WorldMapPlace::legacyIndex)).toList(),
      registered("nodes", nodes, WorldMapNode::id),
      resolvedGeometry.stream().map(WorldMapGeometry::points).toList());
    assertEquals(256, extended.portals().size());
    assertEquals(route.id(), extended.portal(159).route());
    assertEquals(vanilla.routes().size() + 1, extended.routes().size());
    assertEquals(2, extended.pathLengths()[route.segmentIndex()]);
    assertEquals(route.legacyIndex(), extended.locationData()[159].directionalPathIndex_00);
    assertEquals(route.segmentIndex() + 1, extended.directionalPathData()[route.legacyIndex()].pathSegmentIndexAndDirection_00);
    assertEquals(null, vanilla.portal(159).route(), "source definition remains unchanged");
  }
}
