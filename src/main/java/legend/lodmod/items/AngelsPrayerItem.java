package legend.lodmod.items;

import legend.game.combat.bent.BattleEntity27c;
import legend.game.inventory.ItemIcon;
import legend.game.inventory.ItemStack;
import legend.game.scripting.ScriptState;

import static legend.core.GameEngine.CONFIG;
import static legend.lodmod.LodConfig.ITEM_STACK_SIZE;

public class AngelsPrayerItem extends BattleItem {
  public AngelsPrayerItem() {
    super(ItemIcon.ANGELS_PRAYER, 15);
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
    return type == TargetType.ALLIES;
  }

  @Override
  public boolean isStatMod(final ItemStack stack) {
    return true;
  }

  @Override
  public int calculateStatMod(final ItemStack stack, final BattleEntity27c user, final BattleEntity27c target) {
    return 0;
  }

  @Override
  public boolean alwaysHits(final ItemStack stack) {
    return true;
  }

  @Override
  protected int getUseItemScriptEntrypoint() {
    return 34;
  }

  @Override
  protected void useItemScriptLoaded(final ScriptState<BattleEntity27c> user, final int targetBentIndex) {
    user.setStor(8, 0xffffff); // Colour
    user.setStor(28, targetBentIndex);
    user.setStor(30, user.index);
  }
}
