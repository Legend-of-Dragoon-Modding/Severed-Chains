# WMap validation — 2026-09-14

The user authorized builds, runtime validation, and lint after the implementation review. These results supersede the earlier source-review-only notes.

## Results

| Check | Result |
| --- | --- |
| SC production compilation and assembly | Passed |
| Java helper contracts | 14 tests passed |
| Actual SDL/OpenGL engine scenarios | 4 tests passed |
| DragoonMods production build | Passed, with five component CSS budget warnings |
| DragoonMods WMap tests | 9 files, 55 tests passed |
| WMap TypeScript and template ESLint | Passed |
| Full website ESLint | Failed: 98 errors and 2 warnings outside WMap |
| Java `-Xlint:all` compilation | Passed with 574 production warnings and 2 existing test-helper warnings |

The engine scenarios boot a fresh uniquely named campaign, load native encounter/stage 0, consume Guard and wait for the following player turn, then use the normal battle fade/deallocation path to enter the retail WMap. They check native graph/region loading, progression-based denial, typed travel to 37% route distance, immediate busy rejection, saving blocked during travel, completed position accuracy, and in-memory WMap save-tag restoration. Engine mutations run on the renderer thread. No existing save is loaded or overwritten.

The helper tests cover progression revisions/snapshot isolation, persistent portal overrides and slot rebinding, uneven/duplicate-point traversal endpoints, reverse activation compensation, attributed rule conflicts, raw-tag and expandable-buffer bounds, immutable package bytes, unknown-field retention with known-field clearing, and typed destination XML roundtrips/limits.

The browser loaded `/world-map-editor` with 94 retail nodes, 132 routes, zero diagnostics, and no console errors. Standalone selection, starting-portal input, and typed map-data creation were exercised. Extended edit/import roundtrip verification was inconclusive because browser automation failed intermittently and the editor reset unsaved edits.

## Fixes and changed files

| File | Reason |
| --- | --- |
| `src/main/java/legend/game/wmap/WMap.java` | Fix five compile failures by qualifying `java.lang.Math.nextDown` rather than resolving to JOML Math |
| `src/main/java/legend/game/wmap/world/WorldMapDependencyException.java` | Declare `serialVersionUID` for compiler lint |
| `src/main/java/legend/game/saves/serializers/V10Serializer.java` | Remove an unused character-template import |
| `build.gradle` | Honor documented `-PrunTests`, add native VM flags, and package main test runtime classes/resources together for mod discovery |
| `src/test/java/legend/game/Bootstrapper.java` | Initialize engine statics on the engine thread and wait for the actual renderer window |
| `src/test/java/legend/game/Input.java` | Target the renderer window and inject an explicit SDL focus event before keyboard input |
| `src/test/java/legend/game/Harness.java` | Dispatch engine mutations, use unique campaign names, and wait for selected menu icons to finish animating |
| `src/test/java/legend/game/EngineBootTest.java` | Create campaign on engine thread, require Guard consumption, run WMap scenario, and close the window during teardown |
| `src/test/java/legend/game/WorldMapRuntimeChecks.java` | Exercise native battle return, travel, access, and save-position restoration |
| `src/test/java/legend/game/wmap/world/WorldMapContractsTest.java` | Add six progression/traversal/activation/rule tests |
| `src/test/java/legend/game/saves/SaveContractsTest.java` | Add five save bounds/retention/immutability tests |
| `src/test/java/legend/game/wmap/preset/WorldMapDestinationTagCodecTest.java` | Add three typed destination roundtrip/limit tests |
| `src/test/E2E_TESTING.md` | Document corrected runtime setup and fourth engine scenario |
| `docs/world-map-hardening.md` | Link the subsequent validation evidence |
| `docs/world-map-validation.md` | Record commands, results, changed files, and coverage limits |

Representative production fix:

```java
// Before: WMap imports org.joml.Math, which has no nextDown method
Math.min(offset, Math.nextDown(4.0f))
// After: preserve the intended endpoint clamp
Math.min(offset, java.lang.Math.nextDown(4.0f))
```

Representative harness fixes:

```java
// Before: registries can allocate OpenGL objects on the JUnit thread
Harness.createFreshGameState();
// After: execute on the thread owning the rendering context
Harness.onEngineThread(() -> {
  Harness.createFreshGameState();
  return null;
});
```

```groovy
// Before: even explicitly requested tests were excluded
exclude '**/*'
// After: default builds still omit tests; explicit requests run them
if(!project.hasProperty('runTests')) {
  exclude '**/*'
}
```

## Reproduction

Run from `D:/java/sclocal` with local unpacked game assets and an available graphics/audio device:

```powershell
rtk proxy .\gradlew.bat assemble test -PrunTests --tests legend.game.EngineBootTest --tests legend.game.wmap.world.WorldMapContractsTest --tests legend.game.saves.SaveContractsTest --tests legend.game.wmap.preset.WorldMapDestinationTagCodecTest --console=plain
```

Keep the explicit test selections: `ExampleTest` is a manual sandbox that loads an existing save. Default builds still exclude tests. Add `--rerun-tasks` when deliberately rerunning unchanged inputs.

SC has no Checkstyle, Spotless, or other configured Java style-lint task. Compiler lint was run with a temporary Gradle init script adding `-Xlint:all` and `-Xmaxwarns 10000` to `JavaCompile` tasks. The initial pass reported 575 production warnings. After fixing the actionable new missing-serial-version warning, the final forced compile reported 574 production warnings and two existing warnings in `Wait.java`. Remaining production warnings on changed lines concern intentional closeable scope guards and the existing registry initialization pattern. Warning rules were not disabled or weakened.

The temporary init script contents were:

```groovy
allprojects {
  tasks.withType(JavaCompile).configureEach {
    options.compilerArgs += ['-Xlint:all', '-Xmaxwarns', '10000']
  }
}
```

Final compiler-lint command:

```powershell
rtk proxy .\gradlew.bat compileJava compileTestJava -I "$env:TEMP\sc-wmap-lint.init.gradle" --rerun-tasks --console=plain
```

Run from `C:/webprojects/lodtools/web`:

```powershell
rtk npm run build
rtk npm test -- --watch=false --include="src/app/components/world-map-editor/**/*.spec.ts"
rtk proxy node node_modules/eslint/bin/eslint.js src/app/components/world-map-editor --ext .ts,.html
rtk npm run lint
```

The full-site lint failures occur in unrelated game-data, graph/grid display, Irongoon, directives, and services. They include CRLF template lines, explicit `any`, selector/lifecycle rules, unused variables, `prefer-const`, and switch declarations. CSS warnings concern four WMap components and the asset viewer. Existing unrelated package/asset-viewer edits were preserved; no website source changes were needed.

## Limits

- In-memory WMap tag restoration is verified; this is not an exhaustive old-save corpus migration or full campaign file roundtrip
- Missing mod records are covered at the retention-helper level; uninstall/reinstall of actual external mods and every legacy reader were not exercised in the engine
- Retail resources and native battle return are verified; custom standalone GPU/audio bundles, external submap providers, and provider timeout/failure recovery were not exercised end to end
- Preset replacement and missing-package recovery were not exercised in the live engine
- This evidence does not establish compatibility with arbitrary future executable versions or unframed legacy custom-character payloads
- All commits remain local; nothing was pushed
