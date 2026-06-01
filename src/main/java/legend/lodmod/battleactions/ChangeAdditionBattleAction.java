package legend.lodmod.battleactions;

import legend.game.combat.Battle;
import legend.game.combat.bent.PlayerBattleEntity;
import legend.game.combat.ui.BattleActionUseFlowControl;

public class ChangeAdditionBattleAction extends SeveredBattleAction {
  public ChangeAdditionBattleAction() {
    super(0, 3);
  }

  @Override
  public BattleActionUseFlowControl use(final Battle battle, final PlayerBattleEntity player) {
    if(player.character.getUnlockedAdditions().isEmpty()) {
      return BattleActionUseFlowControl.FAIL;
    }

    battle.hud.initListMenu(player, 2);
    return BattleActionUseFlowControl.PAUSE_SCRIPT;
  }
}
