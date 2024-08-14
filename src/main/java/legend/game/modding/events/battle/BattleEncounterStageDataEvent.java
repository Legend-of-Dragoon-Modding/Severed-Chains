package legend.game.modding.events.battle;

import legend.game.combat.bent.BattleEvent;
import legend.game.combat.environment.StageData2c;

public class BattleEncounterStageDataEvent extends BattleEvent {
  public final StageData2c stageData;

  public BattleEncounterStageDataEvent(final StageData2c stageData) {
    this.stageData = new StageData2c(
      stageData._00,
      stageData.musicIndex_04,
      stageData._08,
      stageData.postCombatSubmapScene_0c,
      stageData._10,
      stageData._14,
      stageData.cameraPosIndices_18[0],
      stageData.cameraPosIndices_18[1],
      stageData.cameraPosIndices_18[2],
      stageData.cameraPosIndices_18[3],
      stageData.postCombatSubmapCut_28
    );
  }
}
