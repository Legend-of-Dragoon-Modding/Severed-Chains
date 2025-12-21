package legend.lodmod.battleactions;

import legend.game.combat.Battle;
import legend.game.combat.bent.PlayerBattleEntity;
import legend.game.combat.ui.BattleActionFlowControl;

import static legend.game.Scus94491BpeSegment_8004.CHARACTER_ADDITIONS;

public class ChangeAdditionBattleAction extends SeveredBattleAction {
  public ChangeAdditionBattleAction() {
    super(0);
  }

  @Override
  public BattleActionFlowControl use(final Battle battle, final PlayerBattleEntity player) {
    if(CHARACTER_ADDITIONS[battle.currentTurnBent_800c66c8.innerStruct_00.charId_272].length == 0) {
      return BattleActionFlowControl.FAIL;
    }

    battle.hud.initListMenu((PlayerBattleEntity)battle.currentTurnBent_800c66c8.innerStruct_00, 2);
    return BattleActionFlowControl.PAUSE_SCRIPT;
  }
}
