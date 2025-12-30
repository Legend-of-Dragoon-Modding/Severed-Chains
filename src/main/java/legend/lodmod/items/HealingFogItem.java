package legend.lodmod.items;

import legend.game.combat.bent.BattleEntity27c;
import legend.game.inventory.ItemIcon;
import legend.game.scripting.ScriptState;
import legend.lodmod.LodDeffs;
import org.legendofdragoon.modloader.registries.RegistryId;

public class HealingFogItem extends RecoverHpItem {
  public HealingFogItem() {
    super(ItemIcon.BLUE_POTION, 15, false, 100);
  }

  @Override
  protected int getUseItemScriptEntrypoint() {
    return 2;
  }

  @Override
  protected RegistryId getDeffId() {
    return LodDeffs.HEALING_POTION.getId();
  }

  @Override
  protected void useItemScriptLoaded(final ScriptState<BattleEntity27c> user, final int targetBentIndex) {
    user.setStor(8, 0xd7fff5); // Colour
    user.setStor(28, targetBentIndex);
    user.setStor(30, user.index);
  }
}
