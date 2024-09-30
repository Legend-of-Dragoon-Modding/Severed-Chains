package legend.lodmod.items;

import legend.game.combat.bent.BattleEntity27c;
import legend.game.scripting.ScriptState;
import legend.lodmod.LodDeffs;
import org.legendofdragoon.modloader.registries.RegistryId;

public class HealingFogItem extends RecoverHpItem {
  public HealingFogItem() {
    super(33, 15, false, 100);
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
    user.storage_44[8] = 0xd7fff5; // Colour
    user.storage_44[28] = targetBentIndex;
    user.storage_44[30] = user.index;
  }
}
