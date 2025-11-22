package legend.lodmod.items;

import legend.game.combat.bent.BattleEntity27c;
import legend.game.inventory.ItemIcon;
import legend.game.scripting.ScriptState;

public class HealingBreezeItem extends RecoverHpItem {
  public HealingBreezeItem() {
    super(ItemIcon.BLUE_POTION, 25, true, 50);
  }

  public HealingBreezeItem(final ItemIcon icon, final int price, final boolean targetAll, final int percentage) {
    super(icon, price, targetAll, percentage);
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
