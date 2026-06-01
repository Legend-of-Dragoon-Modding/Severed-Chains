package legend.lodmod.items;

import legend.game.combat.bent.BattleEntity27c;
import legend.game.inventory.ItemIcon;
import legend.game.scripting.ScriptState;

public class SpiritPotionItem extends RecoverSpItem {
  public SpiritPotionItem() {
    super(ItemIcon.RED_POTION, 10, false, 100);
  }

  @Override
  protected int getUseItemScriptEntrypoint() {
    return 34;
  }

  @Override
  protected void useItemScriptLoaded(final ScriptState<BattleEntity27c> user, final int targetBentIndex) {
    user.setStor(8, 0x6868ff); // Colour
    user.setStor(28, targetBentIndex);
    user.setStor(30, user.index);
  }
}
