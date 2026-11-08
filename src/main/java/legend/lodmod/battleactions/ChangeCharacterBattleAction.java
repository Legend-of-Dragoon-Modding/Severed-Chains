package legend.lodmod.battleactions;

import legend.game.combat.Battle;
import legend.game.combat.bent.PlayerBattleEntity;
import legend.game.combat.ui.BattleActionUseFlowControl;
import legend.game.combat.ui.CharSwapListMenu;

public class ChangeCharacterBattleAction extends SeveredBattleAction {
  public ChangeCharacterBattleAction() {
    super(2, 3);
  }

  @Override
  public BattleActionUseFlowControl use(final Battle battle, final PlayerBattleEntity player) {
    if(!CharSwapListMenu.canUseCharSwap(player)) {
      return BattleActionUseFlowControl.FAIL;
    }

    battle.hud.initListMenu(player, 3);
    return BattleActionUseFlowControl.PAUSE_SCRIPT;
  }
}
