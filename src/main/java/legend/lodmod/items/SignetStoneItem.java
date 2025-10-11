package legend.lodmod.items;

import legend.game.characters.Element;
import legend.game.combat.bent.BattleEntity27c;
import legend.game.inventory.ItemIcon;
import legend.game.inventory.ItemStack;
import legend.game.scripting.ScriptState;
import legend.lodmod.LodMod;

import static legend.core.GameEngine.CONFIG;
import static legend.lodmod.LodConfig.ITEM_STACK_SIZE;

public class SignetStoneItem extends BattleItem {
  public SignetStoneItem() {
    super(ItemIcon.SACK, 200);
  }

  public SignetStoneItem(final ItemIcon icon, final int price) {
    super(icon, price);
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
    return type == TargetType.ENEMIES;
  }

  @Override
  public Element getAttackElement(final ItemStack stack) {
    return LodMod.NO_ELEMENT.get();
  }

  @Override
  protected int getUseItemScriptEntrypoint() {
    return 1;
  }

  @Override
  protected void useItemScriptLoaded(final ScriptState<BattleEntity27c> user, final int targetBentIndex) {
    user.storage_44[28] = targetBentIndex;
    user.storage_44[30] = user.index;
  }
}
