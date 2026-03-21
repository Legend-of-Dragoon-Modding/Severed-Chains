package legend.lodmod.items;

import legend.core.memory.Method;
import legend.game.combat.bent.BattleEntity27c;
import legend.game.inventory.ItemIcon;
import legend.game.inventory.ItemStack;
import legend.game.inventory.UseItemResponse;

import static legend.core.GameEngine.CONFIG;
import static legend.game.SItem.addSp;
import static legend.game.Scus94491BpeSegment_800b.characterIndices_800bdbb8;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.lodmod.LodConfig.ITEM_STACK_SIZE;
import static legend.lodmod.LodMod.SP_STAT;

public class RecoverSpItem extends BattleItem {
  private final boolean targetAll;
  private final int amount;

  public RecoverSpItem(final ItemIcon icon, final int price, final boolean targetAll, final int amount) {
    super(icon, price);
    this.targetAll = targetAll;
    this.amount = amount;
  }

  @Override
  public int getMaxStackSize(final ItemStack stack) {
    return CONFIG.getConfig(ITEM_STACK_SIZE.get());
  }

  @Override
  public boolean canBeUsed(final ItemStack stack, final UsageLocation location) {
    return true;
  }

  @Override
  public boolean canBeUsedNow(final ItemStack stack, final UsageLocation location) {
    for(int i = 0; i < characterIndices_800bdbb8.size(); i++) {
      if(!gameState_800babc8.charData_32c.get(characterIndices_800bdbb8.getInt(i)).stats.getStat(SP_STAT.get()).isFull()) {
        return true;
      }
    }

    return false;
  }

  @Override
  public boolean canTarget(final ItemStack stack, final TargetType type) {
    return type == TargetType.ALLIES || type == TargetType.ALL && this.targetAll;
  }

  @Override
  @Method(0x80022d88L)
  public void useInMenu(final ItemStack stack, final UseItemResponse response, final int charId) {
    addSp(charId, this.amount);
    response.success();
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
