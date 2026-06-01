package legend.game;

import legend.core.DebugHelper;
import legend.core.GameEngine;
import legend.core.platform.input.InputKey;
import legend.game.combat.Battle;
import legend.game.modding.events.gamestate.GameLoadedEvent;
import legend.game.modding.events.gamestate.NewGameEvent;
import legend.game.saves.Campaign;
import legend.game.saves.SavedGame;
import legend.game.types.GameState52c;
import legend.lodmod.LodEngineStateTypes;
import legend.lodmod.LodMod;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static legend.core.GameEngine.EVENTS;
import static legend.core.GameEngine.SAVES;
import static legend.game.EngineStates.currentEngineState_8004dd04;
import static legend.game.EngineStates.engineStateOnceLoaded_8004dd24;
import static legend.game.Scus94491BpeSegment_8004.simpleRandSeed_8004dd44;
import static legend.game.Scus94491BpeSegment_800b.campaignType;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.Scus94491BpeSegment_8006.battleState_8006e398;
import static legend.game.combat.Battle.seed_800fa754;
import static legend.game.combat.SBtld.startLegacyEncounter;

public final class Harness {
  private static final Logger LOGGER = LogManager.getFormatterLogger(Harness.class);
  private Harness() { }

  public static void injectGameState(final SavedGame save, final boolean fullBoot) {
    SAVES.loadGameState(save, fullBoot);
  }

  public static void transitionToEngineState(final EngineStateType<?> state) {
    //Removing stop and clear so testing actually runs. 
    engineStateOnceLoaded_8004dd24 = state;
  }

  public static void startBattle(final int encounterId, final int stageId) {
    LOGGER.info("[E2E] startBattle: encounterId=%d, stageId=%d", encounterId, stageId);
    startLegacyEncounter(encounterId, stageId);
    Harness.transitionToEngineState(LodEngineStateTypes.BATTLE.get());
    Wait.waitForEngineState(Battle.class);

    // Wait for battleState and monster data to be initialized
    Wait.waitFor(() -> battleState_8006e398 != null && battleState_8006e398.monsterBents_e50[0] != null,
      30_000, "battleState and monster data initialized");

    final Battle battle = (Battle)currentEngineState_8004dd04;

    // Skip the intro camera movement to speed up the test.
    battle.battleInitialCameraMovementFinished_800c66a8 = true;

    // Wait for loadingStage to advance past calculateInitialTurnValues into battleTick
    Wait.waitFor(() -> getLoadingStage(battle) >= 21, 30_000, "battle loadingStage >= 21 (battleTick)");
    LOGGER.info("[E2E] startBattle: battle fully ready");
  }

  public static void selectBattleMenuIcon(final int iconIndex) {
    final Battle battle = (Battle)currentEngineState_8004dd04;
    while(battle.hud.battleMenu_800c6c34.selectedIcon_22 != iconIndex) {
      Wait.waitFor(() -> battle.hud.battleMenu_800c6c34.state_00 == 2,
        30_000, "battle menu state == 2 (ready for input)");

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
        Wait.waitFor(() -> battle.hud.battleMenu_800c6c34.state_00 != 2,
          30_000, "battle menu state change from input");
        Input.sendKeyUp(key);
      } else {
        DebugHelper.sleep(10);
      }
    }
    LOGGER.info("[E2E] selectBattleMenuIcon: selected icon %d", iconIndex);
  }

  public static void createFreshGameState() {
    GameEngine.bootRegistries();
    Scus94491BpeSegment_800b.campaignType = LodMod.RETAIL_CAMPAIGN_TYPE;
    final GameState52c state = new GameState52c();
    state.campaign = Campaign.create(SAVES, "E2E-Test");
    campaignType.get().setUpNewCampaign(state);
    final NewGameEvent newGameEvent = EVENTS.postEvent(new NewGameEvent(state));
    final GameLoadedEvent gameLoadedEvent = EVENTS.postEvent(new GameLoadedEvent(newGameEvent.gameState));
    gameState_800babc8 = gameLoadedEvent.gameState;
  }

  public static int getLoadingStage(final Battle battle) {
    try {
      final var field = Battle.class.getDeclaredField("loadingStage");
      field.setAccessible(true);
      return field.getInt(battle);
    } catch(final ReflectiveOperationException e) {
      LOGGER.warn("[E2E] Failed to read Battle.loadingStage via reflection; returning -1", e);
      return -1;
    }
  }

  public static void setSimpleRandSeed(final int seed) {
    simpleRandSeed_8004dd44 = seed;
  }

  public static void setMersenneTwisterSeed(final long seed) {
    seed_800fa754.setSeed(seed);
  }
}
