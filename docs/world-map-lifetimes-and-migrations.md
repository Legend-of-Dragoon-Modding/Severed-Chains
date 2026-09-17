# World map ownership, presentation, and save compatibility

This pass implements internal risks I1, I4, I5 and external risks E1, E2, E4 from the WMap assessment. It builds on [world-map-hardening.md](world-map-hardening.md)

## Runtime contracts

| Area | Before | After |
| --- | --- | --- |
| I1 travel ownership | WMap held separate candidate, activation, and recovery fields | `WorldMapTravelOperation` owns admission, loading resources, activation effects, campaign recovery, and cancellation |
| I4 presentation | Every profile supplied the fixed retail table layout | Named elements and textures support authored profiles; explicit capabilities control retail labels, water, and avatar fallback |
| I5 position | Saves and return destinations rounded through float cursor data | Authored distance routes retain double precision through optional save metadata and typed destinations |
| E1 submap loading | Loader callbacks could outlive their submap; bootstrap waited on global loader activity | Per-submap scopes bound waits and adopt results on the engine thread |
| E2 saves | Payload authors managed versions and renamed IDs themselves | Save-schema registration defines domain aliases, incremental payload migration, retention policies, and explicit deletion |
| E4 state lifetime | State replacement and destination globals carried implicit ownership | Engine scopes close on replacement; typed transitions carry an optional return destination through existing fades |

## Travel and precise position

Travel remains gated by the existing retail loading/fade state machines. The operation publishes activation only once WMap is playable. Failed activation compensates applied effects and restores the captured campaign before one recovery attempt. Owner shutdown clears pending loading and compensates unfinished travel

Existing calls remain valid:

```java
WorldMapTravelTarget.atRouteDistance(routeId, 0.37f);
```

Authored distance routes can now preserve greater precision:

```java
WorldMapTravelTarget.atRouteDistance(routeId, 0.370000000123);
```

`routeDistance` is a hexadecimal double string and `routeGeometry` fingerprints direction and all geometry points. Restoration uses these only when the route, geometry, and legacy cursor still agree. Otherwise legacy/topology restoration remains authoritative. A mod editing the legacy cursor or destination float therefore overrides stale precision metadata. Native interval traversal keeps its retail behavior

## Authored presentation and editor

The original six-list profile constructor retains retail validation. Supplying capabilities opts into authored profiles, which may omit retail arrays. `legacyLayout()` pads only the compatibility view; `WMap.getWorldMapPresentation()` exposes the original declarations

```xml
<presentationProfile>
  <capabilities retailLabels="false" retailWater="false" retailAvatars="true"/>
  <namedElements>
    <item id="example:caption" label="My world">
      <position x="0" y="0" z="0"/>
    </item>
  </namedElements>
</presentationProfile>
```

Named declarations are data for custom presentation controllers; they do not automatically instantiate renderers. Disabling retail capabilities suppresses those rendering paths while custom avatar rendering remains available. Compatibility resources may still load

DragoonMods now edits these declarations and capability switches, validates duplicate identities and texture references, and updates local references when a named texture is renamed. Legacy XML remains valid with absent optional fields

## Lifetimes and typed destinations

Before, callers published engine destination globals directly. New integrations can request:

```java
state.requestTravel(new EngineTransition(destination, returnDestination));
```

Existing `requestTravel(EngineDestination)` and retail global transitions remain supported. An explicit null return destination is respected. A legacy overwrite displaces stale queued typed context

`state.lifetime().own(resource)` registers reverse-order cleanup. Child scopes own each submap resource generation. `await(label, future, adopt)` defaults to a 60-second bound; its full overload accepts a timeout and thread-safe disposal of late CPU results. Engine-thread polling performs adoption. Closing the owner prevents stale adoption and releases captured adoption closures

Custom submap providers may opt out of retail bootstrap using `retailBootstrap()`. Their loading context then has no native NEWROOT. The existing callback-based `loadAssets` API is adapted to a bounded wait; providers can override `loadAssetsAsync()` to propagate asynchronous failures explicitly

## Save-schema registration

Register `SaveSchema` entries through `RegisterSaveSchemasEvent`. Schema declarations are available during save deserialization without renderer startup

```java
new SaveSchema.Builder()
  .alias(SaveSchema.Domain.ITEM, oldItemId, currentItemId)
  .payload(payloadId, 2, Map.of(0, migrateZeroToOne, 1, migrateOneToTwo))
  .build();
```

Missing versions mean zero. Each migration advances one version on a defensive copy. Versions are not tied to SC release numbers: any nonnegative integer is accepted. A migration attempt executes at most 256 steps; longer chains remain opaque rather than partially publishing data

Missing mods, unavailable steps, failed migrations, and newer unsupported payloads retain their original data. Unsupported retained payloads take precedence over an older writer. Omitting a payload preserves it; `WriteSaveDataEvent.remove(id)` explicitly deletes it and keeps it deleted on subsequent saves

Aliases apply only to known engine-owned ID fields in their declared domain. Stat modifier type IDs can migrate without rewriting modifier instance IDs. Arbitrary nested mod data, item extra data, and engine-state data are not recursively guessed or rewritten

Old-save-to-new-engine compatibility remains the objective. This does not promise that every older engine understands every future payload. Unknown retained data preserves the opportunity to restore it when its mod/schema becomes available again

## Validation

The SC baseline for this pass is `527340cc6` on `wmap-new-system`. Website changes are committed as `de00c05`

| Check | Result |
| --- | --- |
| SC contract tests | 44 passed across eight classes |
| Live SDL/OpenGL engine checks | Six passed: fresh state, battle loading, Guard, WMap travel/save, native submap round trip, authored preset switch |
| SC assembly | Passed |
| Java compiler lint | `-Xlint:all -Xmaxwarns 10000` completed; 579 warnings remain |
| DragoonMods production build | Passed with component CSS budget warnings |
| DragoonMods WMap tests | 60 passed across ten files |
| Changed editor TypeScript ESLint | Passed |

Java warning attribution used changed lines relative to `527340cc6`. One warning intersects an added line: `Registries.saveSchemas` uses the existing overridable `addRegistry` initialization pattern and triggers `this-escape`. Two additional constructor warnings discovered during this pass were removed by registering WMap/SMap cleanup in `init()`. This is changed-line attribution, not a claim that every other warning has existed for a particular duration

Reproduction commands (Windows; prefix with RTK where available):

```powershell
.\gradlew.bat test -PrunTests --tests legend.game.EngineBootTest --tests legend.game.EngineStateLifetimeTest --tests legend.game.saves.SaveContractsTest --tests legend.game.saves.SaveSchemaMigrationTest --tests legend.game.wmap.preset.WorldMapDestinationTagCodecTest --tests legend.game.wmap.preset.WorldMapPresentationCodecTest --tests legend.game.wmap.world.WorldMapContractsTest --tests legend.game.wmap.world.WorldMapOperationTest --tests legend.game.wmap.world.WorldMapPositionSaveTest assemble --console=plain
.\gradlew.bat compileJava compileTestJava -I $env:TEMP\sc-wmap-lint.init.gradle --rerun-tasks --console=plain
```

The temporary lint init script adds `['-Xlint:all', '-Xmaxwarns', '10000']` to each `JavaCompile` task. The final combined run passed all 50 tests and assembly. Gradle reports are under `build/reports/tests/test`, with per-class XML under `build/test-results/test`. Engine-only runs also write `e2e-test.log`; subsequent isolated test classes can reset that log

Runtime validation caught a fixed-layout snapshot check that rejected named-only presentation; the adapter boundary and regression fixture were corrected. The submap fixture was also corrected to observe the stable render state rather than a per-frame flag and to grant a temporary unlocked return portal. One intermediate Guard input timed out; subsequent complete engine runs passed

Limits: these are selected engine scenarios and contract tests, not exhaustive coverage of all retail submaps, all mod payloads, or arbitrary historical save files. Missing/failed/newer payload handling is covered by contract tests. No new browser visual inspection or repository-wide website lint run is claimed for this pass

## Local commit map

| Commit | Purpose |
| --- | --- |
| `5034c6c` | E2 save aliases, migration, and retention contracts |
| `0841502` | I4 named presentation declarations and compatibility views |
| `7e1fd42` | E4 engine lifetimes and typed transition ownership |
| `b85d9fe` | E1 bounded submap loading and owner-thread adoption |
| `ecfa340` | I5 precise distance and legacy cursor adapters |
| `b4a9b15` | Presentation registry fixture correction |
| `f02584d` | I1 travel operation and WMap presentation/position integration |
| `d1ae93b` | Cleanup registration after construction |
| `797414a` | Runtime scenarios and independent test-class isolation |
| Website `de00c05` | Named presentation editor controls and tests |

No remote publication is part of this work

## Changed-file inventory

Each path is relative to SCLocal unless explicitly marked as a website path

| File | Reason |
| --- | --- |
| `build.gradle` | Isolate engine statics and native contexts between test classes |
| `docs/world-map-hardening.md` | Document contracts, compatibility, validation, and runtime scenarios |
| `docs/world-map-lifetimes-and-migrations.md` | Document contracts, compatibility, validation, and runtime scenarios |
| `docs/world-map-validation.md` | Document contracts, compatibility, validation, and runtime scenarios |
| `src/main/java/legend/core/GameEngine.java` | Register save schemas before deserialization and mod save events |
| `src/main/java/legend/core/Registries.java` | Register save schemas before deserialization and mod save events |
| `src/main/java/legend/game/EngineDestination.java` | Retain precise route distance while preserving legacy cursor and destination APIs |
| `src/main/java/legend/game/EngineState.java` | Own engine lifetimes and carry typed transition return context through state replacement |
| `src/main/java/legend/game/EngineStateLifetime.java` | Own engine lifetimes and carry typed transition return context through state replacement |
| `src/main/java/legend/game/EngineStates.java` | Own engine lifetimes and carry typed transition return context through state replacement |
| `src/main/java/legend/game/EngineTransition.java` | Own engine lifetimes and carry typed transition return context through state replacement |
| `src/main/java/legend/game/saves/ReadSaveDataEvent.java` | Apply save-schema aliases, version migration, retention, and explicit deletion |
| `src/main/java/legend/game/saves/RegisterSaveSchemasEvent.java` | Apply save-schema aliases, version migration, retention, and explicit deletion |
| `src/main/java/legend/game/saves/SaveRegistryData.java` | Apply save-schema aliases, version migration, retention, and explicit deletion |
| `src/main/java/legend/game/saves/SaveSchema.java` | Apply save-schema aliases, version migration, retention, and explicit deletion |
| `src/main/java/legend/game/saves/SaveSchemaCatalog.java` | Apply save-schema aliases, version migration, retention, and explicit deletion |
| `src/main/java/legend/game/saves/SaveSchemaRegistry.java` | Apply save-schema aliases, version migration, retention, and explicit deletion |
| `src/main/java/legend/game/saves/serializers/V10Serializer.java` | Apply save-schema aliases, version migration, retention, and explicit deletion |
| `src/main/java/legend/game/saves/WriteSaveDataEvent.java` | Apply save-schema aliases, version migration, retention, and explicit deletion |
| `src/main/java/legend/game/Scus94491BpeSegment.java` | Own engine lifetimes and carry typed transition return context through state replacement |
| `src/main/java/legend/game/submap/RetailSubmap.java` | Own asynchronous loads, adoption, provider bootstrap, and resource cleanup |
| `src/main/java/legend/game/submap/SMap.java` | Own asynchronous loads, adoption, provider bootstrap, and resource cleanup |
| `src/main/java/legend/game/submap/Submap.java` | Own asynchronous loads, adoption, provider bootstrap, and resource cleanup |
| `src/main/java/legend/game/submap/SubmapLoadingContext.java` | Own asynchronous loads, adoption, provider bootstrap, and resource cleanup |
| `src/main/java/legend/game/submap/SubmapProvider.java` | Own asynchronous loads, adoption, provider bootstrap, and resource cleanup |
| `src/main/java/legend/game/wmap/preset/WorldMapPreset.java` | Round-trip optional named presentation declarations and capabilities |
| `src/main/java/legend/game/wmap/preset/WorldMapPresetCodec.java` | Round-trip optional named presentation declarations and capabilities |
| `src/main/java/legend/game/wmap/WMap.java` | Centralize travel ownership and integrate typed return, capabilities, and precise position |
| `src/main/java/legend/game/wmap/world/WorldMapPositionSave.java` | Retain precise route distance while preserving legacy cursor and destination APIs |
| `src/main/java/legend/game/wmap/world/WorldMapPresentationCapabilities.java` | Separate authored presentation declarations from bounded retail compatibility tables |
| `src/main/java/legend/game/wmap/world/WorldMapPresentationElement.java` | Separate authored presentation declarations from bounded retail compatibility tables |
| `src/main/java/legend/game/wmap/world/WorldMapPresentationProfile.java` | Separate authored presentation declarations from bounded retail compatibility tables |
| `src/main/java/legend/game/wmap/world/WorldMapPresentationTexture.java` | Separate authored presentation declarations from bounded retail compatibility tables |
| `src/main/java/legend/game/wmap/world/WorldMapRegistrySnapshot.java` | Separate authored presentation declarations from bounded retail compatibility tables |
| `src/main/java/legend/game/wmap/world/WorldMapTransition.java` | Centralize travel ownership and integrate typed return, capabilities, and precise position |
| `src/main/java/legend/game/wmap/world/WorldMapTravelOperation.java` | Centralize travel ownership and integrate typed return, capabilities, and precise position |
| `src/main/java/legend/game/wmap/world/WorldMapTravelPosition.java` | Retain precise route distance while preserving legacy cursor and destination APIs |
| `src/main/java/legend/game/wmap/world/WorldMapTravelTarget.java` | Retain precise route distance while preserving legacy cursor and destination APIs |
| `src/main/java/legend/game/wmap/world/WorldMapTraversalPosition.java` | Retain precise route distance while preserving legacy cursor and destination APIs |
| `src/test/E2E_TESTING.md` | Document contracts, compatibility, validation, and runtime scenarios |
| `src/test/java/legend/game/EngineBootTest.java` | Exercise the corresponding save, presentation, lifetime, travel, or engine integration contract |
| `src/test/java/legend/game/EngineStateLifetimeTest.java` | Exercise the corresponding save, presentation, lifetime, travel, or engine integration contract |
| `src/test/java/legend/game/saves/SaveSchemaMigrationTest.java` | Exercise the corresponding save, presentation, lifetime, travel, or engine integration contract |
| `src/test/java/legend/game/SubmapRuntimeChecks.java` | Exercise the corresponding save, presentation, lifetime, travel, or engine integration contract |
| `src/test/java/legend/game/wmap/preset/WorldMapPresentationCodecTest.java` | Exercise the corresponding save, presentation, lifetime, travel, or engine integration contract |
| `src/test/java/legend/game/wmap/world/WorldMapOperationTest.java` | Exercise the corresponding save, presentation, lifetime, travel, or engine integration contract |
| `src/test/java/legend/game/wmap/world/WorldMapPositionSaveTest.java` | Exercise the corresponding save, presentation, lifetime, travel, or engine integration contract |
| `src/test/java/legend/game/WorldMapRuntimeChecks.java` | Exercise the corresponding save, presentation, lifetime, travel, or engine integration contract |
| Website `src/app/components/world-map-editor/world-map-document.ts` | Edit, validate, explain, and test named presentation declarations and retail capability controls |
| Website `src/app/components/world-map-editor/world-map-field-metadata.ts` | Edit, validate, explain, and test named presentation declarations and retail capability controls |
| Website `src/app/components/world-map-editor/world-map-fields.component.ts` | Edit, validate, explain, and test named presentation declarations and retail capability controls |
| Website `src/app/components/world-map-editor/world-map-inspector.component.html` | Edit, validate, explain, and test named presentation declarations and retail capability controls |
| Website `src/app/components/world-map-editor/world-map-presentation.spec.ts` | Edit, validate, explain, and test named presentation declarations and retail capability controls |
