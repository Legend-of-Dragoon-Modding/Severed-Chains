package legend.lodmod.items;

import legend.game.combat.bent.BattleEntity27c;
import legend.game.scripting.ScriptState;

public class EnemyHealingPotionItem extends RecoverHpItem {
  public EnemyHealingPotionItem() {
    super(46, 5, false, 30);
  }

  public EnemyHealingPotionItem(final int icon, final int price, final boolean targetAll, final int percentage) { super(icon, price, false, percentage); }

  @Override
  protected int getUseItemScriptEntrypoint() {
    return 2;
  }

  @Override
  protected void useItemScriptLoaded(final ScriptState<BattleEntity27c> user, final int targetBentIndex) {
    user.storage_44[8] = 0xd7fff5; // Colour
    user.storage_44[28] = targetBentIndex;
    user.storage_44[30] = user.index;
  }
}
