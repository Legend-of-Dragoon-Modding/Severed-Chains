package legend.lodmod.items;

import legend.game.combat.bent.BattleEntity27c;
import legend.game.inventory.ItemIcon;
import legend.game.scripting.ScriptState;

public class EnemyHealingPotionItem extends RecoverHpItem {
  public EnemyHealingPotionItem() {
    super(ItemIcon.SACK, 5, false, 30);
  }

  @Override
  protected int getUseItemScriptEntrypoint() {
    return 2;
  }

  @Override
  protected void useItemScriptLoaded(final ScriptState<BattleEntity27c> user, final int targetBentIndex) {
    user.setStor(8, 0xd7fff5); // Colour
    user.setStor(28, targetBentIndex);
    user.setStor(30, user.index);
  }
}
