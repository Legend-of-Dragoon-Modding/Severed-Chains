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
    for(int spellIndex = 0; spellIndex < 8; spellIndex++) {
      if(battle.dragoonSpells_800c6960[player.charSlot_276].spellIndex_01[spellIndex] != -1) {
        return super.use(battle, player);
      }
    }

    //LAB_800f681c
    return BattleActionFlowControl.FAIL;
  }
}
