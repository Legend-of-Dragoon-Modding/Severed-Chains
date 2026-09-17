# World-map hardening after the second risk assessment

This work follows `ae5f45bf7` on `wmap-new-system`. It implements the second assessment's five internal and five external work items. The older implementation notes in `world-map-contracts.md` describe the first pass; this document describes the subsequent contracts and supersedes conflicting examples there.

## Compatibility and review scope

- All existing save readers remain available; version numbers in the request were examples, not a request for a new numbered format
- Older saves loading in newer SC is the supported compatibility direction
- Tagged saves retain unknown top-level fields and unresolved registry records; this cannot teach an older executable the meaning of a future binary tag type
- An old binary custom-character payload without a length cannot be skipped safely when its decoder is missing; those saves still need their provider or a migration decoder
- Numeric world-map/submap transitions, native assets, retail interval movement, and script stage aliases remain available through adapters
- Initial implementation was source-reviewed only; subsequent authorized build, runtime, and lint results are in [world-map-validation.md](world-map-validation.md)
- No remote operations were performed

## Internal changes

### I1. Preparation and activation ownership

Before, preflight callbacks could change campaign progression before travel was accepted, and recovery restored only portal/visited state. Preparation now takes a detached snapshot of WMap-owned campaign state and rejects/restores mutations. The snapshot includes story/script flags, portal enable/visited state and identities, named facts, script data, and preset identity.

Travel effects belong to activation and must supply compensation:

```java
// Before: this runs during admission, even if later loading fails
state.campaignProgression.setFact(questId, true);

// After: register owned activation and rollback operations
final boolean previous = state.campaignProgression.hasFact(questId);
event.onActivate(
  () -> state.campaignProgression.setFact(questId, true),
  () -> state.campaignProgression.setFact(questId, previous)
);
```

Preparation is a contract, not a Java sandbox. Arbitrary inventory changes, external I/O, global state, or resources owned by a mod cannot be inferred and undone automatically. Such work must be deferred with complete compensation. A rollback must tolerate an effect that only partly applied.

### I2. Explicit recovery history

Before, an unresolved saved position took precedence over the player's current position on a subsequent load. Active saves now keep the current position, and recovery history is retained separately. Native fallback identity travels through save/return payloads. Recovery retains the original package identity and available package bytes so an explicit preset switch does not erase the earlier recovery option.

```java
// Explicit user/mod choice, through normal admission and access checks
wmap.requestWorldMapRecovery();
wmap.discardWorldMapRecovery();
```

A safe arrival must have a route and pass arrival/traversal access. If no such destination exists, the engine reports the invalid recovery configuration instead of silently bypassing access. Missing external dependencies have a distinct exception; arbitrary configuration or callback defects must not be reported as missing mods.

### I3. Availability layers

Retail story replacement establishes the baseline. Authored `ENABLE` and `DISABLE` story contributions layer over it. Persistent identity-based overrides then apply before access rules and vetoes.

```java
// Before: an enabled bit could be replaced by story resolution
state.wmapFlags_15c.set(slot, true);

// After: an explicit persistent override survives story baseline changes
state.worldMapPortalState.setEnabled(portalId, true);
state.worldMapPortalState.clearEnabledOverride(portalId);
```

Old story constructors and XML default to `REPLACE`. Candidate and active snapshots use the same story/override pipeline. Raw numeric script flags retain their compatibility role.

### I4. Attributed decision resolution

Unscoped APIs retain their legacy order semantics but appear as `lod:wmap_legacy_rules` in diagnostics. Attributed departure/arrival decisions choose the highest applicable priority, permit abstention, and reject incompatible equal-priority results.

```java
rules.departureDecision(modId, 100, (origin, progression) ->
  progression.fact(questId) ? WorldMapTravel.Departure.NONE : null
);
```

`null` means abstain only for the decision APIs. See `world-map-rule-resolution.md` for complete migration examples and legacy precedence rules.

### I5. Distance-owned traversal

Before, authored movement reconstructed its position from a float dot cursor every step. `WorldMapTraversalPosition` now owns a route identity and double distance; the dot cursor is an adapter for existing rendering/scripts/saves. Explicit legacy cursor writes and route changes import through that adapter. Endpoint decisions use the retained distance.

```java
// Before
metric.move(dotIndex, previousOffset, movement);

// After
position.advance(movement);
```

Distance lookup uses a cumulative-distance binary search. Repeated vertices do not create divide-by-zero intervals. Retail interval movement remains on its existing path. Existing save cursors and typed return progress still use floats; this change does not promise arbitrary sub-float persistence precision.

## External changes

### E1. Save preservation and package storage

Unavailable tagged inventory/character/stat records are retained instead of requiring immediate registry resolution. Available records deliberately removed from current state must not be resurrected from retained source data. Character slots remain stable for legacy script indices; unavailable slots are excluded when loading/saving the active party and retain their original records.

New embedded packages use binary payload tags instead of Base64 strings. Existing Base64 packages remain readable. Immutable payload bytes can be shared across tag snapshots without exposing mutable arrays. Saves are still portable without their original campaign folder.

Package limits remain 256 MiB aggregate, 64 MiB per asset, and 4096 assets. Save writing is bounded at 512 MiB, including sliced writers, and publishes a completed temporary file rather than truncating an existing save before the write succeeds.

Unknown fields are retained as data. Their future semantics cannot be inferred, and opaque mod fields need their owning mod to define deletion/migration behavior.

### E2. Battle-stage resource ownership

```java
// Before: asynchronous provider receives the mutable battle
CompletableFuture<?> load(Battle battle);

// After: prepare owned resources; adopt on the render thread
CompletableFuture<? extends PreparedBattleStage> prepare();
```

Each request has a generation and a bounded lifetime. Superseded or destroyed requests cannot adopt a late result; unadopted resources are closed. The default timeout is 60 seconds and provider overrides are capped at one hour. Native stages prepare CPU data and adopt GPU/model resources on the render thread.

Providers must return promptly, release partial resources on preparation failure, and transfer ownership correctly during adoption. Failures report a terminal stage error; arbitrary partially applied GPU work is not silently replaced with a guessed native stage. The newly introduced `load(Battle)` provider API requires migration to `prepare()`.

Explicit script stage writes preserve native intent even when the written integer equals a custom stage's alias. Battle return payload access is defensive.

### E3. Provider-driven submap destinations

```java
// Before
EngineDestination.submap(new SubmapEndpoint(cut, scene));

// After: provider-owned data with an authored retail fallback
EngineDestination.submap(providerId, payload, new SubmapEndpoint(cut, scene));
```

`SubmapProvider` constructs the submap using `SubmapLoadingContext`. Submaps own destination data and post-loading spawn handling. The registered retail provider preserves cut/scene behavior. Optional foreground operations are virtual capabilities rather than unconditional `RetailSubmap` casts.

Save/menu/battle return paths retain provider payloads. A missing provider keeps its unresolved payload while using the authored retail fallback; it does not automatically teleport the player when the provider returns. Custom encounters must attach `smap.battleReturnContext()`.

SMap still provides shared NEWROOT, effects, and collision infrastructure. This provider boundary does not replace the entire retail engine or script VM.

### E4. Observable campaign storage

`Flags.set`, `setRaw`, and bulk copies update a revision when values change. Campaign progression consumes those storage revisions, so callers cannot bypass change detection by using the legacy ABI. Detached campaign snapshots give consumers stable reads.

WMap compares source revisions and explicit invalidation rather than comparing derived availability with raw flags; persistent overrides therefore do not cause endless re-resolution.

### E5. World presentation resources

Regions own a `WorldMapResourceBundle` for common UI textures, transport textures/models, leader models, background, music, and optional layout. Retail defaults remain available. Presets can supply packaged resource paths and scene transforms; provider destinations can carry bounded typed XML tag data.

Replacing the resources does not replace every native UI/rendering algorithm. Authored assets must satisfy the declared model/texture layout contract. Custom renderers and presentation controllers remain the extension points for different visual behavior.

## Commit and file inventory

| Assessment item | Primary commit |
| --- | --- |
| I1 Preparation/activation | `406db2673` |
| I2 Explicit recovery | `700585c90` |
| I3 Story/override layers | `6d608ee3b` |
| I4 Rule decisions | `d82960964` |
| I5 Distance ownership | `85b3f0ca7` |
| E1 Save resilience | `abebc52e6` |
| E2 Battle stages | `91c4b3a6e` |
| E3 Submap providers | `9264a6f6c` |
| E4 Campaign revisions | `7963bad32` |
| E5 Presentation resources and integration | `ff2a098` |

Follow-up commits preserve character indices, fix menu-load provider retention, complete authored destinations, protect asynchronous destruction, deduplicate packages, and correct integration defects found during source review.

The DragoonMods editor changes are in `C:/webprojects/lodtools/web`: `51f37ab` adds the schema controls, `ef1e759` releases retail graph constraints for standalone worlds, and `b44cdc4` preserves required region-provider fields. Existing unrelated asset-viewer/package changes were left untouched. Subsequent website build, test, lint, and browser results are recorded in [world-map-validation.md](world-map-validation.md).

### Editor files

| File | Why |
| --- | --- |
| `src/app/components/world-map-editor/world-map-document.ts` | Schema templates, references, diagnostics, resource/payload fields and required-provider parity |
| `src/app/components/world-map-editor/world-map-field-metadata.ts` | Labels and numeric precision handling for authored fields |
| `src/app/components/world-map-editor/world-map-fields.component.ts` | Enum selectors, optional resource children, typed data editing and standalone slot editing |
| `src/app/components/world-map-editor/world-map-inspector.component.html` | Region guidance reflecting independent scenes and resources |
| `src/app/components/world-map-editor/world-map-editor.component.ts` | Standalone removal and graph reference suggestions |

### SCLocal files

The table includes every changed file relative to `ae5f45bf7`; the reason identifies the most recent focused change to that file. The sections above explain the combined behavioral and migration implications.

| File | Latest change |
| --- | --- |
| docs/world-map-contracts.md | Document current contracts, migration examples, limitations and complete file inventory |
| docs/world-map-hardening.md | Document current contracts, migration examples, limitations and complete file inventory |
| docs/world-map-rule-resolution.md | fix WMap resolve attributed travel decisions and diagnose legacy overrides |
| src/main/java/legend/core/Registries.java | add Submap provider destinations with retail fallback and saved spawn data |
| src/main/java/legend/core/tags/EnumTag.java | add WMap provider destinations with bounded typed preset payloads |
| src/main/java/legend/core/tags/ImmutableRawTag.java | fix saves preserve unavailable registry records and embed shared binary world packages |
| src/main/java/legend/core/tags/MapTag.java | fix saves preserve unavailable registry records and embed shared binary world packages |
| src/main/java/legend/core/tags/RawTag.java | fix saves preserve unavailable registry records and embed shared binary world packages |
| src/main/java/legend/game/characters/StatCollection.java | fix saves preserve unavailable registry records and embed shared binary world packages |
| src/main/java/legend/game/characters/StatType.java | fix saves preserve unavailable registry records and embed shared binary world packages |
| src/main/java/legend/game/combat/Battle.java | fix Battle cancel pending stage adoption on engine destruction |
| src/main/java/legend/game/combat/BattleReturnContext.java | fix Battle cancel pending stage adoption on engine destruction |
| src/main/java/legend/game/combat/environment/BattleStageDefinition.java | fix Battle own asynchronous stage preparation and bounded adoption |
| src/main/java/legend/game/combat/environment/NativeBattleStageDefinition.java | fix Battle own asynchronous stage preparation and bounded adoption |
| src/main/java/legend/game/combat/environment/PreparedBattleStage.java | fix Battle own asynchronous stage preparation and bounded adoption |
| src/main/java/legend/game/EngineDestination.java | add Submap provider destinations with retail fallback and saved spawn data |
| src/main/java/legend/game/modding/events/worldmap/WorldMapWarpEvent.java | fix WMap isolate preparation and compensate failed activation |
| src/main/java/legend/game/progression/CampaignProgression.java | fix CampaignProgression observe legacy flag mutations at storage boundary |
| src/main/java/legend/game/saves/SaveManager.java | fix saves preserve cleared fields and replace completed bounded save files |
| src/main/java/legend/game/saves/SaveRegistryData.java | fix saves preserve cleared fields and replace completed bounded save files |
| src/main/java/legend/game/saves/serializers/V10Serializer.java | fix saves deduplicate active and recovery world packages |
| src/main/java/legend/game/saves/SeveredSavedCharacterV2.java | fix saves preserve unavailable registry records and embed shared binary world packages |
| src/main/java/legend/game/saves/SeveredSavedGame.java | fix saves deduplicate active and recovery world packages |
| src/main/java/legend/game/saves/UnavailableSavedCharacter.java | fix saves retain missing character slots across load and serialization |
| src/main/java/legend/game/scripting/GameVarParam.java | fix Battle own asynchronous stage preparation and bounded adoption |
| src/main/java/legend/game/submap/RegisterSubmapProvidersEvent.java | add Submap provider destinations with retail fallback and saved spawn data |
| src/main/java/legend/game/submap/RetailSubmap.java | add Submap provider destinations with retail fallback and saved spawn data |
| src/main/java/legend/game/submap/RetailSubmapProvider.java | add Submap provider destinations with retail fallback and saved spawn data |
| src/main/java/legend/game/submap/SMap.java | fix SMap preserve provider payload when loading saves from menu |
| src/main/java/legend/game/submap/Submap.java | add Submap provider destinations with retail fallback and saved spawn data |
| src/main/java/legend/game/submap/SubmapLoadingContext.java | add Submap provider destinations with retail fallback and saved spawn data |
| src/main/java/legend/game/submap/SubmapProvider.java | add Submap provider destinations with retail fallback and saved spawn data |
| src/main/java/legend/game/submap/SubmapProviderRegistry.java | add Submap provider destinations with retail fallback and saved spawn data |
| src/main/java/legend/game/types/Flags.java | fix CampaignProgression observe legacy flag mutations at storage boundary |
| src/main/java/legend/game/types/GameState52c.java | fix saves retain missing character slots across load and serialization |
| src/main/java/legend/game/unpacker/ExpandableFileData.java | fix saves preserve cleared fields and replace completed bounded save files |
| src/main/java/legend/game/wmap/preset/PresetWorldMapResourceBundle.java | add WMap owned presentation resources and recovery-safe destinations |
| src/main/java/legend/game/wmap/preset/WorldMapDestinationTagCodec.java | add WMap provider destinations with bounded typed preset payloads |
| src/main/java/legend/game/wmap/preset/WorldMapPreset.java | add WMap owned presentation resources and recovery-safe destinations |
| src/main/java/legend/game/wmap/preset/WorldMapPresetCodec.java | add WMap owned presentation resources and recovery-safe destinations |
| src/main/java/legend/game/wmap/preset/WorldMapPresetManager.java | add WMap owned presentation resources and recovery-safe destinations |
| src/main/java/legend/game/wmap/WMap.java | add WMap owned presentation resources and recovery-safe destinations |
| src/main/java/legend/game/wmap/WMapModelAndAnimData258.java | add WMap owned presentation resources and recovery-safe destinations |
| src/main/java/legend/game/wmap/world/RetailWorldMapResourceBundle.java | add WMap owned presentation resources and recovery-safe destinations |
| src/main/java/legend/game/wmap/world/WorldMapActivation.java | fix WMap isolate preparation and compensate failed activation |
| src/main/java/legend/game/wmap/world/WorldMapCampaignSnapshot.java | fix WMap refresh derived progression from source revisions |
| src/main/java/legend/game/wmap/world/WorldMapDependencyException.java | fix WMap preserve active fallback and require explicit position recovery |
| src/main/java/legend/game/wmap/world/WorldMapPortalState.java | fix WMap retain portal overrides before first world binding |
| src/main/java/legend/game/wmap/world/WorldMapPresentationProfile.java | add WMap owned presentation resources and recovery-safe destinations |
| src/main/java/legend/game/wmap/world/WorldMapProgressionResolver.java | refactor WMap retain route distance as authored traversal state |
| src/main/java/legend/game/wmap/world/WorldMapRecovery.java | fix saves deduplicate active and recovery world packages |
| src/main/java/legend/game/wmap/world/WorldMapRegion.java | add WMap owned presentation resources and recovery-safe destinations |
| src/main/java/legend/game/wmap/world/WorldMapRegistrySnapshot.java | add WMap owned presentation resources and recovery-safe destinations |
| src/main/java/legend/game/wmap/world/WorldMapResourceBundle.java | add WMap owned presentation resources and recovery-safe destinations |
| src/main/java/legend/game/wmap/world/WorldMapResourceLoader.java | add WMap owned presentation resources and recovery-safe destinations |
| src/main/java/legend/game/wmap/world/WorldMapRouteMetric.java | refactor WMap retain route distance as authored traversal state |
| src/main/java/legend/game/wmap/world/WorldMapRuleAttribution.java | fix WMap resolve attributed travel decisions and diagnose legacy overrides |
| src/main/java/legend/game/wmap/world/WorldMapRules.java | fix WMap resolve attributed travel decisions and diagnose legacy overrides |
| src/main/java/legend/game/wmap/world/WorldMapRuntime.java | fix WMap refresh derived progression from source revisions |
| src/main/java/legend/game/wmap/world/WorldMapStoryPreset.java | fix WMap layer story defaults and persistent portal overrides |
| src/main/java/legend/game/wmap/world/WorldMapSubmapDestination.java | add WMap provider destinations with bounded typed preset payloads |
| src/main/java/legend/game/wmap/world/WorldMapTravelPosition.java | refactor WMap retain route distance as authored traversal state |
| src/main/java/legend/game/wmap/world/WorldMapTraversalPosition.java | refactor WMap retain route distance as authored traversal state |
| src/main/java/legend/lodmod/LodMod.java | add Submap provider destinations with retail fallback and saved spawn data |
| src/main/java/legend/lodmod/LodSubmapProviders.java | add Submap provider destinations with retail fallback and saved spawn data |
## Follow-up implementation

See [ownership, presentation, and save migrations](world-map-lifetimes-and-migrations.md) for the subsequent I1/I4/I5 and E1/E2/E4 implementation and validation
