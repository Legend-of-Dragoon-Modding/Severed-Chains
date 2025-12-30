package legend.lodmod.battleactions;

import legend.game.combat.Battle;
import legend.game.combat.bent.PlayerBattleEntity;
import legend.game.combat.ui.BattleActionFlowControl;
import legend.game.inventory.Item;

import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;

public class ItemBattleAction extends RetailBattleAction {
  public ItemBattleAction() {
    super(5);
  }

  @Override
  public BattleActionFlowControl use(final Battle battle, final PlayerBattleEntity player) {
    for(int i = 0; i < gameState_800babc8.items_2e9.getSize(); i++) {
      if(gameState_800babc8.items_2e9.get(i).canBeUsed(Item.UsageLocation.BATTLE)) {
        return super.use(battle, player);
      }
    }

    return BattleActionFlowControl.FAIL;
  }
}
