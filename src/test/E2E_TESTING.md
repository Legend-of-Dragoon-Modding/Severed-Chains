# E2E Testing - Severed Chains

End-to-end tests that boot the full game engine and exercise gameplay scenarios (battle, menus) inside a real LWJGL/SDL window.

Tests are not headless. A display is required (or a virtual framebuffer on CI).

## Prerequisites

- Java 25 - bundled JDK at `jdk25.0.0_36/` is used automatically by gradlew
- Unpacked game files - the `files/` directory must exist at the repo root. Place ISOs in `isos/` and run the game once to unpack. If `files/` is missing the tests are skipped not failed
- Windows tested. Linux/macOS should work but untested

## Running from IntelliJ

### One time project setup

1. Open the `Severed-Chains/` directory in IntelliJ
2. Set the Project SDK to the bundled JDK: File > Project Structure > Project > SDK > Add SDK > JDK and select `jdk25.0.0_36/`
3. Set Gradle JVM to the same JDK: File > Settings > Build Execution Deployment > Build Tools > Gradle > Gradle JVM

## Running from Command Line

### Windows PowerShell

```powershell
$env:JAVA_HOME = "$PWD\jdk25.0.0_36"
.\gradlew.bat test --tests "legend.game.EngineBootTest" -PrunTests --rerun-tasks
```

### Linux/macOS

```bash
export JAVA_HOME="$PWD/jdk25.0.0_36"
./gradlew test --tests "legend.game.EngineBootTest" -PrunTests --rerun-tasks
```

`-PrunTests` is required to override the default test exclusion in build.gradle. `--rerun-tasks` forces re-run even if inputs have not changed.

## Test Overview

Tests are ordered by method name and share a single engine instance (`@TestInstance(PER_CLASS)`).

- `test1_freshStateIsValid` new game state has correct gold, party size and chapter
- `test2_battleStartsAndMonsterHasHp` encounter 0 (Berserk Mouse) loads and monster has full HP
- `test3_guardDoesNotChangeMonsterHp` selecting Guard does not damage the monster

## Architecture

```
src/test/java/legend/game/
  EngineBootTest.java - JUnit 5 test class (3 tests)
  ExampleTest.java    - sandbox for manual testing and experimentation
  Bootstrapper.java   - boots engine on a background thread
  Harness.java        - state injection, battle control, game state setup
  Wait.java           - condition polling with timeouts
  Input.java          - SDL keyboard event injection
  TestConfig.java     - encounter ID, stage, RNG seeds

src/test/resources/
  log4j2-test.xml     - routes E2E logs to e2e-test.log
```

### Design notes

The script runtime must stay alive during state transitions. `Harness.transitionToEngineState()` only sets `engineStateOnceLoaded_8004dd24` and lets the main loop handle the rest. No calls to `SCRIPTS.stop()` or `SCRIPTS.clear()`.

`battleInitialCameraMovementFinished_800c66a8` is forced to true to skip the intro camera pan and save about 5 seconds per battle.

`Harness.startBattle()` waits for the battle's internal `loadingStage` (read via reflection) to reach 21 or higher which is when `battleTick` begins and the combat HUD is interactive.

All volume configs are set to 0 in `@BeforeAll` before any key press so FMVs and music never produce sound.

## Logs

- `e2e-test.log` - all [E2E] tagged log output from test classes
- `debug.log` - full engine log same as a normal game run

Both files are created in the repo root.

## Troubleshooting

- Tests skipped: `files/` directory is missing. Unpack game ISOs first
- Gradle skips tests: you forgot `-PrunTests`
- Timeout on engine loading: first run may be slow while shaders compile. Increase timeout in `Bootstrapper.java`
- Timed out waiting for player turn: battle may not have loaded fully. Check `e2e-test.log` for the last loading stage
- SDL window does not appear: ensure a display is available. On headless CI use `xvfb-run` on Linux
