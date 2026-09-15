# World map and SC contract changes

The subsequent [world-map hardening pass](world-map-hardening.md) supersedes the preparation, stage-loading, recovery, resource, and save-preservation contracts described here. Use its migration notes for current APIs.

Implementation on `wmap-new-system`, based on `1a65d2c31`.

## Commit map

| Requested change | Feature commit | Result |
| --- | --- | --- |
| Internal 1 | `920639d` | Explicit travel phases, delayed preset activation, previous-world recovery |
| Internal 2 | `1448412` | One progression resolver for active and candidate maps; revision sources |
| Internal 3 | `00585f8` | Distance-based authored movement and markers with retail interval compatibility |
| Internal 4 | `e2421de` | Standalone presets, registry-ID arrivals, retail slot adapter |
| Internal 5 | `9fbe744` | Attributed replacement, composition, conflict detection, final vetoes |
| External 1 | `bb1710c` | Typed engine destinations and persisted map return payloads |
| External 2 | `3aeb1d9` | Campaign-owned named facts, optional save tags, retail script adapters |
| External 3 | `3f68d0e` | Region-owned scene transforms and independent map providers |
| External 4 | `62cb7fa` | Combat stage registry/providers, battle requests, post-battle return context |
| External 5 | `94681a3` | Portable package tags, retained unresolved data, native recovery |

A subsequent integration commit contains this document and corrections spanning those contracts: defaults for omitted preset primitive fields, exact typed return positions, standalone/native portal-state preservation, unrelated-topology preset arrivals, and native renderer compatibility.

## API migration examples

### Travel lifecycle and progression

Before, travel state was distributed across queued/arriving/completed fields and preset admission built progression without the progression event.

After, `WorldMapTransition` owns admission, queueing, fading, loading, activation and failure. Activation commits the preset token after the first playable destination frame. A failed runtime travel reloads the previous compiled map and position; a failure during recovery propagates rather than repeatedly retrying.

`WorldMapProgressionEvent` now exposes `definition`, `presetId` and `candidate`. Listeners must use that supplied definition when resolving candidate facts. Candidates are not published as the active view.

```java
// Before: a mod had to invalidate after each external progression mutation
map.invalidateWorldMap();

// After: a changing revision supplies a refresh contract
map.watchWorldMapProgression(modId, state::revision);

// Campaign facts need no separate watch or manual invalidation
state.campaignProgression.setFact(bridgeRepairedId, true);
```

### Movement and standalone worlds

```java
// Before / preserved retail behavior
new WorldMapGeometry(nativeSlot, points);

// After: authored geometry measures travel and markers by physical distance
new WorldMapGeometry(points);
new WorldMapGeometry(-1, points, WorldMapGeometry.Motion.DISTANCE, unitsPerStep);

// Standalone preset builder: graph and travel data come from this document
builder.standalone(true).startingPortal(startId).recoveryPortal(safeId);
```

XML geometry may specify `motion="DISTANCE"` and `unitsPerStep="1.0"`. Omitted fields in older XML retain retail interval behavior. Existing slot-taking constructors retain that behavior too. The no-slot Java constructor selects distance movement.

Standalone presets do not import the retail graph. They may select existing providers explicitly. New standalone portal identities begin enabled; saved explicit disabled values and authored story/rule restrictions are retained. Starting and recovery portals must have a route. Native layout checks and the historical Hellena/slot-5 fallback remain in `WorldMapLegacyAdapter`.

A preset switch preserves a geometrically compatible current route where possible. A disjoint topology uses the destination map's starting portal and still passes through the warp event and access checks.

### Rule composition

```java
// Before / preserved legacy last-call replacement
rules.portal(portalId, rule);

// After: attributed composition
try(final var ignored = rules.source(modId, priority)) {
  rules.portal(portalId, WorldMapRuleComposition.VETO, rule);
}
```

Portal and capability rules support `REPLACE`, `REQUIRE_ALL`, `ALLOW_ANY` and `VETO`. Vetoes run after other contributions and policy. Equal-priority replacements from distinct attributed sources fail with their source IDs. Unscoped legacy calls retain final call-order overriding. Diagnostic attribution lists are immutable. Departure, arrival and policy retain ordered replacement semantics, with attribution.

### Typed engine destinations

```java
// Before / still supported for retail scripts
smap.mapTransition(cut, scene);

// After: explicit destination type and spawn data
smap.requestTravel(EngineDestination.worldMap(new WorldMapTravelTarget.Portal(portalId)));
map.requestTravel(EngineDestination.submap(new SubmapEndpoint(cut, scene)));
```

`requestTravel` returns false when the source state is busy. It uses existing fade/unload phases. `EngineDestination` uses the engine-state registry ID and cloned tags. Explicit submap travel retains a stable world-map return target, and submaps persist that target across saves. Existing numeric sentinel transitions remain adapters.

### Independent region rendering

```java
// Before / retained for native-compatible regions
new WorldMapRegion(continent, provider, camera, presentation);

// After: no native continent or map TMD is required
WorldMapRegion.independent(provider, camera, presentation, WorldMapScene.identity());
```

Renderer-only providers can return `new WorldMapModelAssets(null, textures, renderer)`. Independent regions use their scene anchor for path, label, indicator and renderer transforms. Native regions still obtain a native transform anchor when their provider requires one. XML region definitions may omit `legacyTemplate`; the scene defaults to identity.

Shared SC UI and avatar assets still use their existing systems. This change removes the independent region's native map/continent prerequisite; it is not a replacement of all shared game resources.

### Combat requests

```java
// Before / retained native-stage adapter
SBtld.startEncounter(encounter, nativeStageIndex);

// After: registered environment and explicit default return context
SBtld.startEncounter(new BattleRequest(
  encounter,
  REGISTRIES.battleStages.getEntry(stageId).get(),
  BattleReturnContext.capture(engineState, gameState)
));
```

`BattleStageDefinition` owns asynchronous asset loading, ambiance, Dragoon ambiance and effect metadata. `NativeBattleStageDefinition` adapts the retail directories and tables, including Dragoon/Melbu interpretation. Native entries are `lod:stage_N`. Custom providers complete their loading future only after their assets are usable; failures propagate. `Battle.loadStageModel` accepts model/animation data without a synthetic retail directory.

Existing encounter-event integer stage edits, script variables 47/97, and debugger overrides remain supported. Encounter events can select a provider with `setBattleStage`; a later integer edit wins. Existing registry-backed post-battle actions can override the request's default destination, and an unrelated default payload is discarded.

### Save compatibility and fallbacks

Before, a preset-backed save required the campaign's external `worldmaps/<digest>` directory. After, `worldMapPackage` embeds the exact manifest and asset data in versioned tags. Existing content-addressed campaign storage remains the runtime cache. Transferring a newly saved `.dsav` can restore that cache.

The existing retail and V1–V10 readers are unchanged. V5 still uses `LegacySerializer.fromV2To7`. No V11/V12 format or claim of support for a future serializer was introduced. Older-save loading in newer code is preserved; this does not promise that older executables understand new content.

Missing WMap dependencies trigger a native runtime fallback. The package identity, opaque package data, named facts, unknown top-level tags and unresolved original position are retained so missing content is not erased merely by saving the fallback state. Missing combat-stage providers use their authored native fallback. The general item/character save-resolution system was not redesigned.

Tradeoff: embedded packages make save files larger. File/count/size limits and relative-path checks also apply when restoring embedded data. Existing saves that have already lost their sidecar cannot reconstruct its missing bytes, but can load through native recovery.

## Evidence and execution limits

Source and integration diffs were inspected. No build, compilation, tests, lint, or runtime validation was run, following the user's instruction. One delegated rule implementation received a whitespace-only `git diff --check`; this was not a code or runtime check. Behavior above describes the implemented contracts, not demonstrated runtime results. No remotes were pushed or changed.

## Files changed and ownership

Paths below are relative to the repository root. The feature commits contain the complete before/after diffs.

- `src/main/java/legend/core/Registries.java` — Own the combat stage registry
- `src/main/java/legend/game/EngineDestination.java` — Carry typed destination and cloned spawn tags
- `src/main/java/legend/game/EngineState.java` — Expose state-specific typed travel admission
- `src/main/java/legend/game/Scus94491BpeSegment.java` — Route legacy script flag writes through campaign progression
- `src/main/java/legend/game/combat/Battle.java` — Use providers for loading/effects and honor typed return context
- `src/main/java/legend/game/combat/BattleRequest.java` — Combine encounter, stage provider and return context
- `src/main/java/legend/game/combat/BattleReturnContext.java` — Capture the default engine return payload
- `src/main/java/legend/game/combat/SBtld.java` — Adapt old encounter APIs and consume typed battle requests
- `src/main/java/legend/game/combat/SEffe.java` — Delegate stage-specific effect interpretation
- `src/main/java/legend/game/combat/environment/BattleStageDefinition.java` — Define provider-owned environment loading and effects
- `src/main/java/legend/game/combat/environment/BattleStageRegistry.java` — Register combat environment definitions
- `src/main/java/legend/game/combat/environment/NativeBattleStageDefinition.java` — Adapt all native stage conventions
- `src/main/java/legend/game/combat/environment/RegisterBattleStagesEvent.java` — Expose stage registration to mods
- `src/main/java/legend/game/modding/events/gamestate/EncounterEvent.java` — Select typed stages while retaining integer listener overrides
- `src/main/java/legend/game/modding/events/worldmap/WorldMapProgressionEvent.java` — Expose candidate definition and preset context
- `src/main/java/legend/game/progression/CampaignProgression.java` — Own named campaign facts and retail flag adapters
- `src/main/java/legend/game/saves/SaveManager.java` — Use region-aware location names
- `src/main/java/legend/game/saves/SeveredSavedGame.java` — Restore named facts, package data and retained tags
- `src/main/java/legend/game/saves/serializers/V10Serializer.java` — Read/write optional compatible tags and portable packages
- `src/main/java/legend/game/scripting/GameVarParam.java` — Preserve numeric stage writes through adapters
- `src/main/java/legend/game/submap/RetailSubmap.java` — Propagate typed encounter stage selection and debugger overrides
- `src/main/java/legend/game/submap/SMap.java` — Consume typed travel and persist world-map return targets
- `src/main/java/legend/game/types/GameState52c.java` — Own progression and retained portable save data
- `src/main/java/legend/game/wmap/WMap.java` — Integrate lifecycle, progression, movement, typed travel, scenes, saves and combat
- `src/main/java/legend/game/wmap/preset/WorldMapPreset.java` — Describe standalone graphs and preserve old constructors
- `src/main/java/legend/game/wmap/preset/WorldMapPresetCodec.java` — Read optional new fields with compatible defaults
- `src/main/java/legend/game/wmap/preset/WorldMapPresetManager.java` — Embed/restore packages and recover missing WMap content
- `src/main/java/legend/game/wmap/world/WorldMapBattleStage.java` — Reference combat providers with native fallback
- `src/main/java/legend/game/wmap/world/WorldMapDefinition.java` — Expose geometry identity mapping and support standalone removal
- `src/main/java/legend/game/wmap/world/WorldMapGeometry.java` — Declare movement metric and retain compatibility constructors
- `src/main/java/legend/game/wmap/world/WorldMapLegacyAdapter.java` — Own reserved native layout and arrival conventions
- `src/main/java/legend/game/wmap/world/WorldMapModelAssets.java` — Document renderer-only scene ownership
- `src/main/java/legend/game/wmap/world/WorldMapPortalState.java` — Preserve portal identity state across standalone/native layouts
- `src/main/java/legend/game/wmap/world/WorldMapProgressionResolver.java` — Resolve active/candidate facts and revision changes
- `src/main/java/legend/game/wmap/world/WorldMapRegion.java` — Support regions without a native continent template
- `src/main/java/legend/game/wmap/world/WorldMapRegionRenderer.java` — Adopt independent assets and preserve native anchor fallback
- `src/main/java/legend/game/wmap/world/WorldMapRegistrySnapshot.java` — Resolve standalone datasets, stable movement identities and combat stages
- `src/main/java/legend/game/wmap/world/WorldMapRenderContext.java` — Prevent shared transform mutation
- `src/main/java/legend/game/wmap/world/WorldMapRouteMetric.java` — Convert physical distance to the existing interpolation cursor
- `src/main/java/legend/game/wmap/world/WorldMapRuleAttribution.java` — Expose immutable rule provenance
- `src/main/java/legend/game/wmap/world/WorldMapRuleComposition.java` — Declare supported rule combination operations
- `src/main/java/legend/game/wmap/world/WorldMapRules.java` — Compose and attribute rule contributions
- `src/main/java/legend/game/wmap/world/WorldMapRuntime.java` — Expose the immutable rules for recovery snapshots
- `src/main/java/legend/game/wmap/world/WorldMapScene.java` — Supply a defensively copied scene transform
- `src/main/java/legend/game/wmap/world/WorldMapTransition.java` — Own travel phases and request lifetime
- `src/main/java/legend/game/wmap/world/WorldMapTraversalProfile.java` — Describe metric-dependent marker positions
- `src/main/java/legend/lodmod/LodBattleStages.java` — Register native stages and numeric fallbacks
- `src/main/java/legend/lodmod/LodMod.java` — Publish native stage registrations
- `docs/world-map-contracts.md` — Record API migration, commit mapping, compatibility and execution limits
