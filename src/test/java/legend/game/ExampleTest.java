package legend.game;

import legend.core.platform.input.InputKey;
import legend.game.combat.BattleTransitionMode;
import legend.game.combat.effects.TransformationMode;
import legend.game.modding.coremod.CoreMod;
import legend.game.saves.SavedGame;
import org.junit.jupiter.api.Test;

import static legend.core.GameEngine.CONFIG;
import static legend.core.GameEngine.SAVES;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ExampleTest {
  @Test
  void test() {
    // Bootstrap engine
    final Thread engine = Bootstrapper.loadEngine();

    // Skip intro FMV
    Wait.waitForFmvToStart();
    Input.sendKeyPress(InputKey.RETURN);
    Input.sendKeyPress(InputKey.RETURN);
    Wait.waitForFmvToStop();

    // Wait for title screen to load
    Wait.waitForEngineState(EngineStateEnum.TITLE_02);
    Input.sendKeyPress(InputKey.RETURN);

    // Inject most recent save
    final SavedGame save = SAVES.loadAllCampaigns().getFirst().loadAllSaves().getFirst();
    Harness.injectGameState(save.state, save.config, true);

    // Init some configs
    CONFIG.setConfig(CoreMod.AUTO_TEXT_CONFIG.get(), true);
    CONFIG.setConfig(CoreMod.AUTO_TEXT_DELAY_CONFIG.get(), 0.0f);
    CONFIG.setConfig(CoreMod.BATTLE_TRANSITION_MODE_CONFIG.get(), BattleTransitionMode.INSTANT);
    CONFIG.setConfig(CoreMod.TRANSFORMATION_MODE_CONFIG.get(), TransformationMode.SHORT);

    // Set RNG to known values
    Harness.setSimpleRandSeed(0);
    Harness.setMersenneTwisterSeed(0L);

    // Start battle
    Harness.startBattle(0, 0);

    try {
      engine.join();
    } catch(final InterruptedException e) {
      e.printStackTrace();
    }

    assertTrue(true);
  }
}
