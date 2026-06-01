package legend.lodmod.items;

import legend.game.combat.bent.BattleEntity27c;
import legend.game.inventory.ItemIcon;
import legend.game.inventory.ItemStack;
import legend.game.scripting.ScriptState;

import static legend.core.GameEngine.CONFIG;
import static legend.lodmod.LodConfig.ITEM_STACK_SIZE;

public class SmokeBallItem extends BattleItem {
  public SmokeBallItem() {
    super(ItemIcon.SACK, 200);
  }

  @Override
  public int getMaxStackSize(final ItemStack stack) {
    return CONFIG.getConfig(ITEM_STACK_SIZE.get());
  }

  @Override
  public boolean isRepeat(final ItemStack stack) {
    return true;
  }

  @Override
  public boolean isProtected(final ItemStack stack) {
    return true;
  }

  @Override
  public boolean canBeUsed(final ItemStack stack, final UsageLocation location) {
    return location == UsageLocation.BATTLE;
  }

  @Override
  public boolean canTarget(final ItemStack stack, final TargetType type) {
    return false;
  }

  @Override
  protected int getUseItemScriptEntrypoint() {
    return 41;
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
