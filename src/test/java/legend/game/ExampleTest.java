package legend.game;

import legend.core.DebugHelper;
import legend.core.platform.input.InputKey;
import legend.game.characters.StatCollection;
import legend.game.combat.BattleTransitionMode;
import legend.game.combat.bent.MonsterBattleEntity;
import legend.game.combat.effects.TransformationMode;
import legend.game.modding.coremod.CoreMod;
import legend.game.saves.SavedGame;
import legend.game.scripting.ScriptState;
import legend.game.title.Ttle;
import legend.lodmod.LodMod;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;

import static legend.core.GameEngine.CONFIG;
import static legend.core.GameEngine.SAVES;
import static legend.game.Scus94491BpeSegment_8006.battleState_8006e398;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ExampleTest {
  @Test
  void test() throws ExecutionException, InterruptedException {
    // Bootstrap engine
    final Thread engine = Bootstrapper.loadEngine();

    // Skip intro FMV
    Wait.waitForFmvToStart();
    Input.sendKeyPress(InputKey.RETURN);
    Input.sendKeyPress(InputKey.RETURN);
    Wait.waitForFmvToStop();

    // Wait for title screen to load
    Wait.waitForEngineState(Ttle.class);
    Input.sendKeyPress(InputKey.RETURN);

    // Inject most recent save
    final SavedGame save = SAVES.loadAllCampaigns().getFirst().loadAllSaves().getFirst().get();
    Harness.injectGameState(save.gameState, save.config, true);

    // Init some configs
    CONFIG.setConfig(CoreMod.AUTO_TEXT_CONFIG.get(), true);
    CONFIG.setConfig(CoreMod.AUTO_TEXT_DELAY_CONFIG.get(), 0.0f);
    CONFIG.setConfig(CoreMod.BATTLE_TRANSITION_MODE_CONFIG.get(), BattleTransitionMode.INSTANT);
    CONFIG.setConfig(CoreMod.TRANSFORMATION_MODE_CONFIG.get(), TransformationMode.SHORT);

    // Set RNG to known values
    Harness.setSimpleRandSeed(0);
    Harness.setMersenneTwisterSeed(0L);

    // Start battle
    Harness.startBattle(443, 0);

    // Wait for first player turn
    Wait.waitForPlayerTurn();

    // Set Melbu health to 50%
    final ScriptState<MonsterBattleEntity> melbu = battleState_8006e398.monsterBents_e50[0];
    final StatCollection melbuStats = melbu.innerStruct_00.stats;
    melbuStats.getStat(LodMod.HP_STAT.get()).setCurrent(melbuStats.getStat(LodMod.HP_STAT.get()).getMaxRaw() / 2);

    // Guard forever
    while(true) {
      Wait.waitForPlayerTurn();
      Harness.selectBattleMenuIcon(1);
      Input.sendKeyPress(InputKey.RETURN);
      DebugHelper.sleep(10);

      if(false)break;
    }

    try {
      engine.join();
    } catch(final InterruptedException e) {
      e.printStackTrace();
    }

    assertTrue(true);
  }
}
