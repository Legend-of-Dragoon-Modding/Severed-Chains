package legend.lodmod.items;

import legend.game.combat.bent.BattleEntity27c;
import legend.game.inventory.ItemIcon;
import legend.game.scripting.ScriptState;

public class HealingRainItem extends RecoverHpItem {
  public HealingRainItem() {
    super(ItemIcon.BLUE_POTION, 60, true, 100);
  }

  public HealingRainItem(final int icon, final int price, final boolean targetAll, final int percentage) { super(icon, price, targetAll, percentage); }

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
