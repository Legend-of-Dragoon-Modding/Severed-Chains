package legend.lodmod.battleactions;

import legend.game.combat.Battle;
import legend.game.combat.bent.PlayerBattleEntity;
import legend.game.combat.ui.BattleActionFlowControl;

public class SpellBattleAction extends RetailBattleAction {
  public SpellBattleAction() {
    super(3);
  }

  @Override
  public BattleActionFlowControl use(final Battle battle, final PlayerBattleEntity player) {
    //LAB_800f67b8
    //LAB_800f67d8
    //LAB_800f67f4
    if(!battle.dragoonSpells_800c6960.get(player.charSlot_276).spellIndices_01.isEmpty()) {
      return super.use(battle, player);
    }

    //LAB_800f681c
    return BattleActionFlowControl.FAIL;
  }
}
