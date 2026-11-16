package legend.lodmod.battleactions;

import legend.game.combat.Battle;
import legend.game.combat.bent.PlayerBattleEntity;
import legend.game.combat.ui.BattleActionUseFlowControl;

public class SpellBattleAction extends RetailBattleAction {
  public SpellBattleAction() {
    super(3);
  }

  @Override
  public BattleActionUseFlowControl use(final Battle battle, final PlayerBattleEntity player) {
    if(player.spells.isEmpty()) {
      return BattleActionUseFlowControl.FAIL;
    }

    return super.use(battle, player);
  }
}
