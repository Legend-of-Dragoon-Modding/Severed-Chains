package legend.game.modding.events.battle;

import legend.game.combat.bent.BattleEvent;
import legend.game.combat.environment.StageData2c;

public class BattleMusicEvent extends BattleEvent {
  public int victoryType;
  public int musicIndex;
  public final StageData2c stageData;

  public BattleMusicEvent(final int victoryType, final int musicIndex, final StageData2c stageData) {
    this.victoryType = victoryType;
    this.musicIndex = musicIndex;
    this.stageData = new StageData2c(
      stageData.unused_00,
      stageData.musicIndex_04,
      stageData.escapeChance_08,
      stageData.postCombatSubmapScene_0c,
      stageData.playerOpeningCamera_10,
      stageData.monsterOpeningCamera_14,
      stageData.cameraPosIndices_18[0],
      stageData.cameraPosIndices_18[1],
      stageData.cameraPosIndices_18[2],
      stageData.cameraPosIndices_18[3],
      stageData.postCombatSubmapCut_28
    );
  }
}
