package legend.game;

import legend.game.characters.VitalsStat;
import legend.core.GameEngine;
import legend.game.combat.BattleTransitionMode;
import legend.game.combat.bent.MonsterBattleEntity;
import legend.game.combat.effects.TransformationMode;
import legend.game.scripting.ScriptState;
import legend.game.title.Ttle;
import legend.game.types.GameState52c;
import legend.lodmod.LodMod;
import legend.game.modding.coremod.CoreMod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.Timeout;
import legend.core.platform.input.InputKey;

import java.nio.file.Files;
import java.nio.file.Path;

import static legend.core.GameEngine.CONFIG;
import static legend.game.Scus94491BpeSegment_8006.battleState_8006e398;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.TestConfig.*;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.MethodName.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class EngineBootTest {

  private static final Logger LOGGER = LogManager.getFormatterLogger(EngineBootTest.class);
  private Thread engine;

  @BeforeAll
  void bootEngine() {
    Assumptions.assumeTrue(
      Files.isDirectory(Path.of("files")),
      "Skipped: game files not unpacked. Place ISOs in isos/ and run the game once."
    );

    LOGGER.info("[E2E] Engine boot starting");
    engine = Bootstrapper.loadEngine();

    // Disable Discord causes crash when testing
    GameEngine.DISCORD.destroy();

    // Mute all audio BEFORE any key press so FMV/music never plays
    CONFIG.setConfig(CoreMod.MASTER_VOLUME_CONFIG.get(), 0.0f);
    CONFIG.setConfig(CoreMod.MUSIC_VOLUME_CONFIG.get(), 0.0f);
    CONFIG.setConfig(CoreMod.SFX_VOLUME_CONFIG.get(), 0.0f);
    CONFIG.setConfig(CoreMod.FMV_VOLUME_CONFIG.get(), 0.0f);
    LOGGER.info("[E2E] Audio muted");

    // skip the intro splash 
    legend.core.DebugHelper.sleep(1000);
    Input.sendKeyPress(InputKey.RETURN);

    Wait.waitForFmvToStart();
    Input.sendKeyPress(InputKey.RETURN);
    Input.sendKeyPress(InputKey.RETURN);
    Wait.waitForFmvToStop();

    Wait.waitForEngineState(Ttle.class);
    Input.sendKeyPress(InputKey.RETURN);

    Harness.createFreshGameState();

    CONFIG.setConfig(CoreMod.AUTO_TEXT_CONFIG.get(), true);
    CONFIG.setConfig(CoreMod.AUTO_TEXT_DELAY_CONFIG.get(), 0.0f);
    CONFIG.setConfig(CoreMod.BATTLE_TRANSITION_MODE_CONFIG.get(), BattleTransitionMode.INSTANT);
    CONFIG.setConfig(CoreMod.TRANSFORMATION_MODE_CONFIG.get(), TransformationMode.SHORT);

    Harness.setSimpleRandSeed(RNG_SEED);
    Harness.setMersenneTwisterSeed(MT_SEED);

    LOGGER.info("[E2E] Fresh game state created - gold=%d, party=%d, chapter=%d",
      gameState_800babc8.gold_94,
      gameState_800babc8.charIds_88.size(),
      gameState_800babc8.chapterIndex_98
    );
  }

  @Test
  @Timeout(60)
  void test1_freshStateIsValid() {
    final GameState52c gs = gameState_800babc8;
    assertNotNull(gs, "Game state should exist");
    assertEquals(20, gs.gold_94, "Gold should be 20");
    assertEquals(1, gs.charIds_88.size(), "Party should have 1 character");
    assertEquals(0, gs.chapterIndex_98, "Chapter should be 0");
    LOGGER.info("[E2E] PASS: freshStateIsValid");
  }

  @Test
  @Timeout(120)
  void test2_battleStartsAndMonsterHasHp() {
    LOGGER.info("[E2E] Starting battle encounter=%d stage=%d", ENCOUNTER_ID, STAGE_ID);
    Harness.startBattle(ENCOUNTER_ID, STAGE_ID);
    LOGGER.info("[E2E] Battle loaded, checking monster state");

    final ScriptState<MonsterBattleEntity> monster = battleState_8006e398.monsterBents_e50[0];
    assertNotNull(monster, "First monster slot should be populated");

    final VitalsStat hp = monster.innerStruct_00.stats.getStat(LodMod.HP_STAT.get());
    assertTrue(hp.getCurrent() > 0, "Monster HP should be > 0");
    assertEquals(hp.getMaxRaw(), hp.getCurrent(), "Monster HP should be full");

    LOGGER.info("[E2E] PASS: battleStartsAndMonsterHasHp - HP=%d/%d", hp.getCurrent(), hp.getMaxRaw());
  }

  @Test
  @Timeout(120)
  void test3_guardDoesNotChangeMonsterHp() {
    assertNotNull(battleState_8006e398, "Battle state must exist (did test2 pass?)");

    final var monsterSlot = battleState_8006e398.monsterBents_e50[0];
    assertNotNull(monsterSlot, "Monster slot 0 should be populated");

    final VitalsStat hp = monsterSlot.innerStruct_00.stats.getStat(LodMod.HP_STAT.get());
    final int hpBefore = hp.getCurrent();
    LOGGER.info("[E2E] Monster HP before Guard = %d / %d", hpBefore, hp.getMaxRaw());

    Wait.waitForPlayerTurn();
    Harness.selectBattleMenuIcon(1);
    Input.sendKeyPress(InputKey.RETURN);

    Wait.waitForPlayerTurn();

    final int hpAfter = hp.getCurrent();
    assertEquals(hpBefore, hpAfter, "Monster HP should be unchanged after Guard");
    LOGGER.info("[E2E] PASS: guardDoesNotChangeMonsterHp - HP=%d (unchanged)", hpAfter);
  }

  @AfterAll
  void shutdownEngine() {
    if(engine != null) {
      LOGGER.info("[E2E] Shutting down engine thread...");
      engine.interrupt();
      try {
        engine.join(5000);
        if(engine.isAlive()) {
          LOGGER.warn("[E2E] Engine thread did not terminate within 5000ms and may still be running");
        } else {
          LOGGER.info("[E2E] Engine thread shut down");
        }
      } catch(final InterruptedException e) {
        Thread.currentThread().interrupt();
        LOGGER.warn("[E2E] Interrupted while waiting for engine thread to shut down", e);
      }
    }
  }
}
