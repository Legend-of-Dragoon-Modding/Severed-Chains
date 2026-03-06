package legend.lodmod.battleactions;

import legend.game.combat.Battle;
import legend.game.combat.bent.PlayerBattleEntity;
import legend.game.combat.ui.BattleActionFlowControl;

public class ChangeAdditionBattleAction extends SeveredBattleAction {
  public ChangeAdditionBattleAction() {
    super(0, 3);
  }

  @Override
  public BattleActionFlowControl use(final Battle battle, final PlayerBattleEntity player) {
    if(player.character.getUnlockedAdditions().isEmpty()) {
      return BattleActionFlowControl.FAIL;
    }

    battle.hud.initListMenu(player, 2);
    return BattleActionFlowControl.PAUSE_SCRIPT;
  }
}
