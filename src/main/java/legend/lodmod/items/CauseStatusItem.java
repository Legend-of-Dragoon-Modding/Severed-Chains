package legend.lodmod.items;

import legend.game.combat.bent.BattleEntity27c;
import legend.game.inventory.ItemIcon;
import legend.game.inventory.ItemStack;
import legend.game.scripting.ScriptState;

import static legend.core.GameEngine.CONFIG;
import static legend.game.Scus94491BpeSegment.simpleRand;
import static legend.lodmod.LodConfig.ITEM_STACK_SIZE;

public class CauseStatusItem extends BattleItem {
  private final int useItemColour;
  private final int status;

  public CauseStatusItem(final int useItemColour, final ItemIcon icon, final int price, final int status) {
    super(icon, price);
    this.useItemColour = useItemColour;
    this.status = status;
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
  public boolean isStatMod(final ItemStack stack) {
    return true;
  }

  @Override
  public int calculateStatMod(final ItemStack stack, final BattleEntity27c user, final BattleEntity27c target) {
    return 0;
  }

  @Override
  public int getSpecialEffect(final ItemStack stack, final BattleEntity27c user, final BattleEntity27c target) {
    int effect = -1;
    if(simpleRand() * 101 >> 16 < 101) {
      final int statusType = this.status;

      if((statusType & 0xff) != 0) {
        int statusIndex;
        for(statusIndex = 0; statusIndex < 8; statusIndex++) {
          if((statusType & (0x80 >> statusIndex)) != 0) {
            break;
          }
        }

        effect = 0x80 >> statusIndex;
      }
    }

    return effect;
  }

  @Override
  protected int getUseItemScriptEntrypoint() {
    return 12;
  }

  @Override
  protected void useItemScriptLoaded(final ScriptState<BattleEntity27c> user, final int targetBentIndex) {
    user.setStor(8, this.useItemColour);
    user.setStor(28, targetBentIndex);
    user.setStor(30, user.index);
  }
}
