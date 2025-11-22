package legend.lodmod.items;

import legend.game.combat.bent.BattleEntity27c;
import legend.game.inventory.ItemIcon;
import legend.game.scripting.ScriptState;

public class MoonSerenadeItem extends RecoverMpItem {
  public MoonSerenadeItem() {
    super(ItemIcon.YELLOW_POTION, 100, true, 100);
  }

  public MoonSerenadeItem(final ItemIcon icon, final int price, final boolean targetAll, final int percentage) {
    super(icon, price, targetAll, percentage);
  }

  @Override
  protected int getUseItemScriptEntrypoint() {
    return 2;
  }

  @Override
  protected void useItemScriptLoaded(final ScriptState<BattleEntity27c> user, final int targetBentIndex) {
    user.setStor(8, 0x780078); // Colour
    user.setStor(28, targetBentIndex);
    user.setStor(30, user.index);
  }
}
