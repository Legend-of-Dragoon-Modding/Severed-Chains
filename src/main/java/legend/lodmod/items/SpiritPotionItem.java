package legend.lodmod.items;

import legend.game.combat.bent.BattleEntity27c;
import legend.game.inventory.ItemIcon;
import legend.game.inventory.ItemStack;
import legend.game.scripting.ScriptState;

public class SpiritPotionItem extends RecoverHpItem {
  public SpiritPotionItem() {
    super(ItemIcon.RED_POTION, 10, false, 100);
  }

  public SpiritPotionItem(final ItemIcon icon, final int price, final boolean targetAll, final int percentage) {
    super(icon, price, targetAll, percentage);
  }

  @Override
  public boolean canBeUsed(final ItemStack stack, final UsageLocation location) {
    return location == UsageLocation.BATTLE;
  }

  @Override
  protected int getUseItemScriptEntrypoint() {
    return 2;
  }

  @Override
  protected void useItemScriptLoaded(final ScriptState<BattleEntity27c> user, final int targetBentIndex) {
    user.storage_44[8] = 0x6868ff; // Colour
    user.storage_44[28] = targetBentIndex;
    user.storage_44[30] = user.index;
  }
}
