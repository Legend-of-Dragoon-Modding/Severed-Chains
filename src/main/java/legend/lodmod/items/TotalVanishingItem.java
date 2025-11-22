package legend.lodmod.items;

import legend.game.combat.bent.BattleEntity27c;
import legend.game.inventory.ItemIcon;
import legend.game.inventory.ItemStack;
import legend.game.scripting.ScriptState;

import static legend.core.GameEngine.CONFIG;
import static legend.lodmod.LodConfig.ITEM_STACK_SIZE;

public class TotalVanishingItem extends BattleItem {
  public TotalVanishingItem() {
    super(ItemIcon.SACK, 10);
  }

  public TotalVanishingItem(final ItemIcon icon, final int price) {
    super(icon, price);
  }

  @Override
  public int getMaxStackSize(final ItemStack stack) {
    return CONFIG.getConfig(ITEM_STACK_SIZE.get());
  }

  @Override
  public boolean canBeUsed(final ItemStack stack, final UsageLocation location) {
    return location == UsageLocation.BATTLE;
  }

  @Override
  public boolean canTarget(final ItemStack stack, final TargetType type) {
    return type == TargetType.ENEMIES;
  }

  @Override
  public int getSpecialEffect(final ItemStack stack, final BattleEntity27c user, final BattleEntity27c target) {
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
    user.setStor(28, targetBentIndex);
    user.setStor(30, user.index);
  }
}
