package legend.game;

import legend.core.DebugHelper;
import legend.game.combat.Battle;
import legend.game.combat.bent.BattleEntity27c;
import legend.game.combat.bent.PlayerBattleEntity;
import legend.game.fmv.Fmv;
import legend.game.scripting.ScriptState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.BooleanSupplier;
import java.util.function.Predicate;

import static legend.game.EngineStates.currentEngineState_8004dd04;

public final class Wait {
  private static final Logger LOGGER = LogManager.getFormatterLogger(Wait.class);
  private static final long DEFAULT_TIMEOUT_MS = 30_000;

  private Wait() { }

  public static void waitFor(final BooleanSupplier condition) {
    while(!condition.getAsBoolean()) {
      DebugHelper.sleep(10);
    }
  }

  public static void waitFor(final BooleanSupplier condition, final long timeoutMs, final String description) {
    LOGGER.info("[E2E] Waiting for: %s (timeout %ds)", description, timeoutMs / 1000);
    final long start = System.currentTimeMillis();
    while(!condition.getAsBoolean()) {
      final long elapsed = System.currentTimeMillis() - start;
      if(elapsed > timeoutMs) {
        throw new AssertionError("Timed out after " + (elapsed / 1000) + "s waiting for: " + description);
      }
      DebugHelper.sleep(10);
    }
    LOGGER.info("[E2E] Wait completed: %s (%.1fs)", description, (System.currentTimeMillis() - start) / 1000.0);
  }

  public static void waitForEngineState(final Class<? extends EngineState> state) {
    waitFor(() -> currentEngineState_8004dd04 != null && currentEngineState_8004dd04.getClass() == state,
      DEFAULT_TIMEOUT_MS, "engine state: " + state.getSimpleName());
  }

  public static void waitForFmvToStart() {
    waitFor(() -> Fmv.isPlaying, DEFAULT_TIMEOUT_MS, "FMV to start");
  }

  public static void waitForFmvToStop() {
    waitFor(() -> !Fmv.isPlaying, DEFAULT_TIMEOUT_MS, "FMV to stop");
  }

  public static ScriptState<? extends BattleEntity27c> waitForBentTurn(final Predicate<BattleEntity27c> predicate, final long timeoutMs, final String description) {
    final Battle battle = (Battle)currentEngineState_8004dd04;
    waitFor(() -> battle.currentTurnBent_800c66c8 != null && predicate.test(battle.currentTurnBent_800c66c8.innerStruct_00),
      timeoutMs, description);
    return battle.currentTurnBent_800c66c8;
  }

  public static ScriptState<PlayerBattleEntity> waitForPlayerTurn() {
    final Battle battle = (Battle)currentEngineState_8004dd04;
    //noinspection unchecked
    return (ScriptState<PlayerBattleEntity>)waitForBentTurn(
      bent -> bent instanceof PlayerBattleEntity
        && (battle.hud.battleMenu_800c6c34.highlightState_02 & 0x2) != 0,
      60_000, "player turn (menu highlight ready)");
  }
}
