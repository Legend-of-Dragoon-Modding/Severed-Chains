package legend.game;

import legend.core.DebugHelper;
import legend.game.combat.Battle;
import legend.game.combat.bent.BattleEntity27c;
import legend.game.combat.bent.PlayerBattleEntity;
import legend.game.fmv.Fmv;
import legend.game.scripting.ScriptState;

import java.util.function.BooleanSupplier;
import java.util.function.Predicate;

import static legend.game.EngineStates.currentEngineState_8004dd04;

public final class Wait {
  private Wait() { }

  public static void waitFor(final BooleanSupplier condition) {
    while(!condition.getAsBoolean()) {
      DebugHelper.sleep(10);
    }
  }

  public static void waitForEngineState(final Class<? extends EngineState> state) {
    waitFor(() -> currentEngineState_8004dd04 != null && currentEngineState_8004dd04.getClass() == state);
  }

  public static void waitForFmvToStart() {
    waitFor(() -> Fmv.isPlaying);
  }

  public static void waitForFmvToStop() {
    waitFor(() -> !Fmv.isPlaying);
  }

  public static ScriptState<? extends BattleEntity27c> waitForBentTurn(final Predicate<BattleEntity27c> predicate) {
    final Battle battle = (Battle)currentEngineState_8004dd04;
    waitFor(() -> battle.currentTurnBent_800c66c8 != null && predicate.test(battle.currentTurnBent_800c66c8.innerStruct_00));
    return battle.currentTurnBent_800c66c8;
  }

  public static ScriptState<PlayerBattleEntity> waitForPlayerTurn() {
    final Battle battle = (Battle)currentEngineState_8004dd04;
    //noinspection unchecked
    return (ScriptState<PlayerBattleEntity>)waitForBentTurn(bent -> bent instanceof PlayerBattleEntity && (battle.hud.battleMenu_800c6c34.highlightState_02 & 0x2) != 0);
  }
}
