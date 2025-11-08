package legend.lodmod.items;

import legend.core.memory.Method;
import legend.game.combat.bent.BattleEntity27c;
import legend.game.inventory.Item;
import legend.game.inventory.ItemIcon;
import legend.game.inventory.ItemStack;
import legend.game.inventory.UseItemResponse;

import static legend.core.GameEngine.CONFIG;
import static legend.game.SItem.addSp;
import static legend.lodmod.LodConfig.ITEM_STACK_SIZE;

public class RecoverSpItem extends Item {
  private final boolean targetAll;
  private final int percentage;

  public RecoverSpItem(final int price, final boolean targetAll, final int percentage) {
    super(ItemIcon.RED_POTION, price);
    this.targetAll = targetAll;
    this.percentage = percentage;
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
    return type == TargetType.ALLIES || type == TargetType.ALL && this.targetAll;
  }

  @Override
  @Method(0x80022d88L)
  public void useInMenu(final ItemStack stack, final UseItemResponse response, final int charId) {
    final int amount;
    if(this.percentage == 100) {
      amount = -1;
    } else {
      amount = this.percentage;
    }

    response._00 = 6;
    response.value_04 = addSp(charId, amount);
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
}
