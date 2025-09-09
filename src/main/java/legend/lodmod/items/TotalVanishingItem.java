package legend.lodmod.items;

import legend.game.combat.bent.BattleEntity27c;
import legend.game.inventory.ItemIcon;
import legend.game.scripting.ScriptState;

public class TotalVanishingItem extends BattleItem {
  public TotalVanishingItem() {
    super(ItemIcon.SACK, 10);
  }

  @Override
  public boolean canBeUsed(final UsageLocation location) {
    return location == UsageLocation.BATTLE;
  }

  @Override
  public boolean canTarget(final TargetType type) {
    return type == TargetType.ENEMIES;
  }

  @Override
  public int getSpecialEffect(final BattleEntity27c user, final BattleEntity27c target) {
    if((target.specialEffectFlag_14 & 0x80) != 0) { // Resistance
      return -1;
    }

    return 0;
  }

  @Override
  protected int getUseItemScriptEntrypoint() {
    return 10;
  }

  @Override
  protected void loadDeff(final ScriptState<? extends BattleEntity27c> user, final int entrypoint, final int param) {
    // no-op
  }

  @Override
  protected void useItemScriptLoaded(final ScriptState<BattleEntity27c> user, final int targetBentIndex) {
    user.storage_44[28] = targetBentIndex;
    user.storage_44[30] = user.index;
  }
}
