package legend.game;

import legend.game.saves.ConfigCollection;
import legend.game.types.GameState52c;

import static legend.core.GameEngine.SAVES;
import static legend.core.GameEngine.SCRIPTS;
import static legend.game.Scus94491BpeSegment_8004.engineStateOnceLoaded_8004dd24;
import static legend.game.Scus94491BpeSegment_8004.simpleRandSeed_8004dd44;
import static legend.game.Scus94491BpeSegment_800b.battleStage_800bb0f4;
import static legend.game.Scus94491BpeSegment_800b.encounterId_800bb0f8;
import static legend.game.combat.Battle.seed_800fa754;

public final class Harness {
  private Harness() { }

  public static void injectGameState(final GameState52c state, final ConfigCollection config, final boolean fullBoot) {
    SAVES.loadGameState(state, config, fullBoot);
  }

  public static void transitionToEngineState(final EngineStateEnum state) {
    SCRIPTS.stop();
    SCRIPTS.clear();
    engineStateOnceLoaded_8004dd24 = state;
  }

  public static void startBattle(final int encounterId, final int stageId) {
    encounterId_800bb0f8 = encounterId;
    battleStage_800bb0f4 = stageId;
    Harness.transitionToEngineState(EngineStateEnum.COMBAT_06);
    Wait.waitForEngineState(EngineStateEnum.COMBAT_06);
  }

  public static void setSimpleRandSeed(final int seed) {
    simpleRandSeed_8004dd44 = seed;
  }

  public static void setMersenneTwisterSeed(final long seed) {
    seed_800fa754.setSeed(seed);
  }
}
