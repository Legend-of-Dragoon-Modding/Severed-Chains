package legend.lodmod.items;

import legend.game.combat.bent.BattleEntity27c;
import legend.game.scripting.ScriptState;

public class HealingPotionItem extends RecoverHpItem {
  public HealingPotionItem() {
    super(33, 5, false, 50);
  }

  public HealingPotionItem(final int icon, final int price, final boolean targetAll, final int percentage) {
    super(icon, price, targetAll, percentage);
  }

  @Override
  protected int getUseItemScriptEntrypoint() {
    return 2;
  }

  @Override
  protected int getDeffEntrypoint() {
    return 1;
  }

  @Override
  protected void useItemScriptLoaded(final ScriptState<BattleEntity27c> user, final int targetBentIndex) {
    user.storage_44[8] = 0xd7fff5; // Colour
    user.storage_44[28] = targetBentIndex;
    user.storage_44[30] = user.index;
  }
}
