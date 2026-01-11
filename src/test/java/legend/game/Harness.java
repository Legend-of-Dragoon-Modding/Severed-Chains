package legend.game;

import legend.core.DebugHelper;
import legend.core.platform.input.InputKey;
import legend.game.combat.Battle;
import legend.game.saves.ConfigCollection;
import legend.game.types.GameState52c;
import legend.lodmod.LodEngineStateTypes;

import static legend.core.GameEngine.SAVES;
import static legend.core.GameEngine.SCRIPTS;
import static legend.game.EngineStates.currentEngineState_8004dd04;
import static legend.game.EngineStates.engineStateOnceLoaded_8004dd24;
import static legend.game.Scus94491BpeSegment_8004.simpleRandSeed_8004dd44;
import static legend.game.Scus94491BpeSegment_800b.battleStage_800bb0f4;
import static legend.game.Scus94491BpeSegment_800b.encounterId_800bb0f8;
import static legend.game.combat.Battle.seed_800fa754;

public final class Harness {
  private Harness() { }

  public static void injectGameState(final GameState52c state, final ConfigCollection config, final boolean fullBoot) {
    SAVES.loadGameState(state, config, fullBoot);
  }

  public static void transitionToEngineState(final EngineStateType<?> state) {
    SCRIPTS.stop();
    SCRIPTS.clear();
    engineStateOnceLoaded_8004dd24 = state;
  }

  public static void startBattle(final int encounterId, final int stageId) {
    encounterId_800bb0f8 = encounterId;
    battleStage_800bb0f4 = stageId;
    Harness.transitionToEngineState(LodEngineStateTypes.BATTLE.get());
    Wait.waitForEngineState(Battle.class);
  }

  public static void selectBattleMenuIcon(final int iconIndex) {
    final Battle battle = (Battle)currentEngineState_8004dd04;

    while(battle.hud.battleMenu_800c6c34.selectedIcon_22 != iconIndex) {
      Wait.waitFor(() -> battle.hud.battleMenu_800c6c34.state_00 == 2);

      final InputKey key;
      if(battle.hud.battleMenu_800c6c34.selectedIcon_22 > iconIndex) {
        key = InputKey.LEFT;
      } else if(battle.hud.battleMenu_800c6c34.selectedIcon_22 < iconIndex) {
        key = InputKey.RIGHT;
      } else {
        key = null;
      }

      if(key != null) {
        Input.sendKeyDown(key);
        Wait.waitFor(() -> battle.hud.battleMenu_800c6c34.state_00 != 2);
        Input.sendKeyUp(key);
      } else {
        DebugHelper.sleep(10);
      }
    }
  }

  public static void setSimpleRandSeed(final int seed) {
    simpleRandSeed_8004dd44 = seed;
  }

  public static void setMersenneTwisterSeed(final long seed) {
    seed_800fa754.setSeed(seed);
  }
}
