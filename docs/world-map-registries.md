# World-map registries

World-map registry data is collected during registry initialization, resolved once when `WMap` configures, then projected into the existing per-instance renderer and movement arrays. Registry entries are immutable recipes, not live map state. A later registry reset creates fresh recipes and requires a new map configuration to affect a `WMap` instance.

## Registry catalog

`Registries` owns eleven world-map registries. Each has a matching registration event posted by the normal registry lifecycle.

The registry objects themselves use the core IDs `lod_core:world_map_nodes`, `lod_core:world_map_geometry`, `lod_core:world_map_routes`, `lod_core:world_map_places`, `lod_core:world_map_portals`, `lod_core:world_map_story_presets`, `lod_core:world_map_coolon_destinations`, `lod_core:world_map_teleport_links`, `lod_core:world_map_encounter_pools`, `lod_core:world_map_presentation_profiles`, and `lod_core:world_map_behaviours`. These identify registry types. They are separate from the entry IDs shown below.

| Registry field | Entry data | Registration event | Retail ID pattern |
| --- | --- | --- | --- |
| `worldMapNodes` | Physical route endpoint | `RegisterWorldMapNodesEvent` | `lod:wmap_node_<n>` |
| `worldMapGeometry` | Ordered path points | `RegisterWorldMapGeometryEvent` | `lod:wmap_geometry_<n>` |
| `worldMapRoutes` | Directed route, geometry and encounter reference | `RegisterWorldMapRoutesEvent` | `lod:wmap_route_<n>` |
| `worldMapPlaces` | Name, thumbnail, services and sounds | `RegisterWorldMapPlacesEvent` | `lod:wmap_place_<n>` |
| `worldMapPortals` | Route/place binding and submap endpoints | `RegisterWorldMapPortalsEvent` | `lod:wmap_location_<n>` |
| `worldMapStoryPresets` | Story flag, enabled portals and objective | `RegisterWorldMapStoryPresetsEvent` | `lod:wmap_story_preset_<n>` |
| `worldMapCoolonDestinations` | Coolon portal, default destination and label | `RegisterWorldMapCoolonDestinationsEvent` | `lod:wmap_coolon_destination_<n>` |
| `worldMapTeleportLinks` | Directed portal pair and translation | `RegisterWorldMapTeleportLinksEvent` | `lod:wmap_teleport_link_<n>` |
| `worldMapEncounterPools` | Four weighted encounter registry IDs | `RegisterWorldMapEncounterPoolsEvent` | `lod:wmap_encounter_pool_<n>` |
| `worldMapPresentationProfiles` | Camera, textures, services and renderer presentation tables | `RegisterWorldMapPresentationProfilesEvent` | `lod:wmap_presentation` |
| `worldMapBehaviours` | Default rules/configuration callback | `RegisterWorldMapBehavioursEvent` | `lod:wmap_behaviour` |

The first five registries describe the graph. Story presets, Coolon destinations, teleport links and encounter pools provide travel and encounter data. The presentation profile supplies renderer tables. Behaviours configure the definition/rules before external `WorldMapConfigureEvent` listeners run.

## Registration and replacement

Use a `WorldMapDataEntry<T>` subclass for every registry except behaviours. Its factory receives the effective registry ID when the snapshot materializes the entry. Factories for portals, places and nodes must return that ID in the created record; the snapshot rejects a changed identity.

To replace retail data, register an entry with `replaces` set to the retail ID. The replacement retains the replaced ID as its effective identity, so references made by other entries and saves continue to resolve. The replacement target must be a base entry. Replacement chains are invalid, and two candidates with the same replacement priority fail instead of depending on mod load order. The greatest priority wins.

Do not look up a replacement by the registering entry's own ID. For example, a replacement registered as `example:alternate_route_7` with `replaces = lod:wmap_route_7` produces the effective route `lod:wmap_route_7`.

### Portal replacement example

This is the typical static registrar/delegate/listener shape for a portal replacement. The registration belongs to the mod namespace; `replaces` targets Lod's stable entry ID; the factory receives that target as `effectiveId`.

```java
private static final RegistryId PORTAL_0 = new RegistryId("lod", "wmap_location_0");
private static final Registrar<WorldMapPortalEntry, RegisterWorldMapPortalsEvent> PORTALS =
  new Registrar<>(GameEngine.REGISTRIES.worldMapPortals, MOD_ID);

public static final RegistryDelegate<WorldMapPortalEntry> ALTERNATE_PORTAL_0 =
  PORTALS.register("alternate_location_0", () -> {
    final WorldMapPortal retail = LegacyWorldMap.importDefinition().portal(0);
    return new WorldMapPortalEntry(PORTAL_0, 100, effectiveId -> new WorldMapPortal(
      effectiveId, retail.legacyIndex(), retail.route(), retail.place(), retail.from(),
      new SubmapEndpoint(13, 17), retail.junctionIndex(), retail.continent(),
      retail.fullBrightness(), retail.effectFlags()
    ));
  });

@EventListener
public static void registerWorldMapPortals(final RegisterWorldMapPortalsEvent event) {
  PORTALS.registryEvent(event);
}
```

The resulting portal is still `lod:wmap_location_0`, even though the delegate itself is registered as `example:alternate_location_0`. Do not store the delegate ID in routes, saves, Coolon destinations, teleport links, or rules.

## Snapshot lifecycle

These registries use the normal SC lock/reset lifecycle. Register through the matching event while the registry is writable. Keep `RegistryDelegate` handles for future lookups rather than retaining raw entries across resets: reset invalidates cached delegate values and the next initialization recreates entries. A configured map owns its immutable snapshot until it is configured again. Factories should capture authored values during registration and remain free of runtime state changes.

At world-map configuration, `WorldMapRegistrySnapshot.read(REGISTRIES)` resolves each data registry. It first resolves portal, geometry and encounter-pool entries, builds ID-to-legacy-index mappings for geometry and pools, then resolves routes. A route therefore names geometry and encounter pools by `RegistryId`; the runtime route contains only their validated legacy indices.

The snapshot then resolves places and nodes, constructs an immutable `WorldMapDefinition`, resolves story, Coolon and teleport data, selects the single presentation profile, and validates all references. `WMap` passes the definition and rule builder through registered behaviours, then through `WorldMapConfigureEvent`, validates the configured definition again, builds `WorldMapRuntime`, and creates its instance-local `Location14`, route, geometry, Coolon and teleport arrays from the final definition/snapshot.

Story presets are ordered by `order` and select the last matching story flag. Coolon destinations and teleport links are ordered by `order`; a Coolon default destination is another destination's effective registry ID. Encounter pools are ordered by `legacyIndex`; each pool must contain exactly four existing encounter IDs.

## Extending the graph

New nodes do not have a numeric slot. New geometry, routes, places and encounter pools may be appended only at the next dense legacy index. A new route must reference registered node IDs and a geometry ID. With a nonzero encounter rate, it must also name a registered encounter-pool ID. Its directed geometry endpoints must exactly match the referenced node coordinates.

`WorldMapRouteData.encounterPool = null` retains the imported `legacyEncounterPlaceholder`; it is not an extension lookup mechanism. `-1` selects the first encounter in pool 0 without consuming a random roll. Retail zero-rate routes retain inactive nonnegative placeholders for table parity. Give an active custom route a registered pool ID unless that legacy sentinel behavior is intended.

Portals are different: the renderer, flags and save projection use exactly 256 portal slots. A mod cannot append a 257th portal. Reuse an unused slot by replacing its `lod:wmap_location_<n>` entry, retaining that slot's legacy index. Routes and places also use dense indices once projected, so do not leave gaps or renumber existing entries.

Coolon destinations may be added with a unique `order`, a valid portal ID and a valid default-destination ID. Teleport sources must be unique and translations must use integral coordinates. The snapshot rejects unresolved IDs, duplicate orders, non-finite coordinates, invalid geometry, missing presentation tables, and unknown encounter entries before `WMap` begins map play.

## Behaviour registration

`worldMapBehaviours` entries are not snapshot overlays. They are loaded directly, sorted by ascending `priority` and then registry ID, and invoked with `WorldMapDefinition.Builder` and `WorldMapRules.Builder`. They run before `WorldMapConfigureEvent`, allowing the event listener to make the final configuration adjustments.

```java
private static final Registrar<WorldMapBehaviour, RegisterWorldMapBehavioursEvent> BEHAVIOURS =
  new Registrar<>(GameEngine.REGISTRIES.worldMapBehaviours, MOD_ID);

public static final RegistryDelegate<WorldMapBehaviour> RULES =
  BEHAVIOURS.register("alternate_rules", () -> new WorldMapBehaviour(100, (definition, rules) -> {
    final RegistryId portal = new RegistryId("lod", "wmap_location_0");
    rules.portal(portal, (value, action, progression) -> WorldMapAccess.ALLOWED);
    rules.capability(WorldMapTravel.Capability.COOLON, progression -> progression.storyFlag(0x15a));
    rules.departure((origin, progression) -> WorldMapTravel.Departure.NONE);
    rules.arrival((origin, progression, map) -> WorldMapTravel.Arrival.NORMAL);
  }));

@EventListener
public static void registerWorldMapBehaviours(final RegisterWorldMapBehavioursEvent event) {
  BEHAVIOURS.registryEvent(event);
}
```

The behaviour controls portal access, capabilities, Queen Fury departure classification, and arrival classification through the actual rule-builder methods. It does not mutate a live `WMap`; its callback runs once for each map configuration.

The definition builder is constrained by the same legacy projection: it replaces existing portal, route, place, node or geometry slots and cannot add slots. Add graph data through the registries; use behaviours or `WorldMapConfigureEvent` to adjust the already-resolved definition and rules for one map configuration.

## Retail import events

`LodWorldMap` supplies retail nodes, geometry, routes, places and portals. `LodWorldMapTravelData` supplies retail story presets, Coolon destinations, teleport links and encounter pools. `LodWorldMapPresentationData` supplies the retail presentation profile and default behaviour. The eleven event listeners are registered by `LodMod`, so other mods use the same events and registries rather than editing `WmapStatics`.

`WmapStatics` is mutable legacy bootstrap input only. Lod reads it while its registry listeners create retail entry recipes. The former active consumer read the tables directly:

```java
private Location14[] locations_800f0e34 = WmapStatics.locations_800f0e34;
```

After registry initialization, the active consumer is the snapshot projection in `WMap`:

```java
this.worldMapData = WorldMapRegistrySnapshot.read(REGISTRIES);
final WorldMapDefinition definition = event.definition.build();
this.locations_800f0e34 = definition.locationData();
this.coolonWarpDest_800ef228 = this.worldMapData.coolonData(definition);
```

Changing `WmapStatics` after registration is unsupported: it does not update the snapshot, runtime definition, or these instance arrays. Register/replace data before the registry lifecycle completes, then use behaviours or `WorldMapConfigureEvent` for configuration-time changes.

## Runtime events

The existing configure, progression and travel events remain the configuration and transition seams. Four runtime events expose the resolved pipeline at narrower points:

| Event | When it fires | Listener contract |
| --- | --- | --- |
| `WorldMapArrivalEvent` | After first-match/retail-fallback arrival selection and before position initialization | May replace mutable `origin` or `portal` with non-null values; a changed portal must be known and arrival-allowed |
| `WorldMapEnterEvent` | After entrance access passes and before fade/prompt teardown | Final portal/destination fields identify the accepted entry; set `cancelled` to retain the entrance prompt without accepting travel |
| `WorldMapJunctionEvent` | At initial endpoint discovery and each intersection-choice refresh | May filter or reorder mutable `connections`; the engine requires a multiset subset of immutable `available` candidates for `viewVersion` |
| `WorldMapResolvedEvent` | After progression resolution and the resolving guard clears | Observational immutable view; queries return that published snapshot and invalidation waits for the next caller |

Arrival keeps the original retail fallback when its portal is unchanged. This preserves the no-route Queen Fury arrival row while requiring an alternate portal selected by a listener to pass arrival validation. Enter applies to normal and region acceptance. Junction listeners cannot add a connection, including a duplicate, that was not in the original available-candidate multiset. Resolved listeners must not recursively resolve the world map.

## Changed-file inventory

This inventory covers the registry, runtime, Coolon correction, and event commits after `28cb4fe10`. It names every changed file, grouped by responsibility.

| Area | Files | Why |
| --- | --- | --- |
| Build and registry wiring | `build.gradle`, `src/main/java/legend/core/Registries.java`, `src/main/java/legend/lodmod/LodMod.java` | Adds registry/event initialization and the world-map validation task wiring |
| Runtime projection | `src/main/java/legend/game/wmap/WMap.java`, `src/main/java/legend/game/wmap/WmapStatics.java` | Builds per-instance map arrays from the resolved registry snapshot instead of authoritative static tables |
| Registry base and all registry types | `src/main/java/legend/game/wmap/registries/WorldMapDataEntry.java`, `WorldMapBehaviourRegistry.java`, `WorldMapCoolonDestinationEntry.java`, `WorldMapCoolonDestinationRegistry.java`, `WorldMapEncounterPoolEntry.java`, `WorldMapEncounterPoolRegistry.java`, `WorldMapGeometryEntry.java`, `WorldMapGeometryRegistry.java`, `WorldMapNodeEntry.java`, `WorldMapNodeRegistry.java`, `WorldMapPlaceEntry.java`, `WorldMapPlaceRegistry.java`, `WorldMapPortalEntry.java`, `WorldMapPortalRegistry.java`, `WorldMapPresentationProfileEntry.java`, `WorldMapPresentationProfileRegistry.java`, `WorldMapRouteEntry.java`, `WorldMapRouteRegistry.java`, `WorldMapStoryPresetEntry.java`, `WorldMapStoryPresetRegistry.java`, `WorldMapTeleportLinkEntry.java`, `WorldMapTeleportLinkRegistry.java` | Defines entry recipes, overlays, and registry storage for every world-map data category |
| Registry event classes | `src/main/java/legend/game/wmap/registries/RegisterWorldMapBehavioursEvent.java`, `RegisterWorldMapCoolonDestinationsEvent.java`, `RegisterWorldMapEncounterPoolsEvent.java`, `RegisterWorldMapGeometryEvent.java`, `RegisterWorldMapNodesEvent.java`, `RegisterWorldMapPlacesEvent.java`, `RegisterWorldMapPortalsEvent.java`, `RegisterWorldMapPresentationProfilesEvent.java`, `RegisterWorldMapRoutesEvent.java`, `RegisterWorldMapStoryPresetsEvent.java`, `RegisterWorldMapTeleportLinksEvent.java` | Posts the eleven normal registration seams |
| Registry model and runtime | `src/main/java/legend/game/wmap/world/WorldMapArrivalRule.java`, `WorldMapBehaviour.java`, `WorldMapCoolonDestination.java`, `WorldMapEncounterPool.java`, `WorldMapGeometry.java`, `WorldMapPresentationProfile.java`, `WorldMapRegistrySnapshot.java`, `WorldMapRouteData.java`, `WorldMapRules.java`, `WorldMapRuntime.java`, `WorldMapStoryPreset.java`, `WorldMapTeleportLink.java`, `WorldMapTravel.java` | Holds registry payloads, validation, rule wiring, snapshot resolution and runtime projection contracts |
| Retail import data | `src/main/java/legend/lodmod/LodWorldMap.java`, `LodWorldMapData.java`, `LodWorldMapPresentationData.java`, `LodWorldMapTravelData.java` | Registers the retail graph, travel, encounter and presentation data |
| Runtime event seams | `src/main/java/legend/game/modding/events/worldmap/WorldMapArrivalEvent.java`, `WorldMapEnterEvent.java`, `WorldMapJunctionEvent.java`, `WorldMapResolvedEvent.java` | Exposes validated arrival, entry, junction and view-resolution hooks |
| Tests | `src/test/java/legend/game/wmap/world/WorldMapRegistryTest.java`, `src/test/java/legend/game/wmap/world/WorldMapEventsTest.java` | Verifies retail import parity, overlay priority, lifecycle, registry extension projection, and the runtime-event contracts |
| Documentation | `docs/world-map.md`, `docs/world-map-registries.md` | Documents runtime behavior plus registry and extension contracts |

Validation: `gradlew.bat wmapCompatibilityTest assemble` passed all 13 JUnit tests, including 885,957 compatibility assertion evaluations. Registry tests cover actual registration/reset, replacement conflicts, dense graph extension, story masks, transport parity, configured behaviours, detached data, and reordered Coolon origins. Event tests cover arrival validation and junction filtering/reordering.

The isolated runtime smoke reached WMAP `PLAY` and player `RENDER_5`, applied a registered presentation replacement, executed a registered behaviour, and moved from path 1 dot 0 to dot 4 over 259 ticks. It observed one arrival event, 279 junction events, and one resolved event. Evidence is local at `C:/temp/wmap-runtime-smoke/launch-registry-events.log`. Entry cancellation, cross-path transitions, and a full retail playthrough have not been exercised by that smoke test.
