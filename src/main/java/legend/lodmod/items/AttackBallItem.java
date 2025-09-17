package legend.lodmod.items;

import legend.game.combat.bent.BattleEntity27c;
import legend.game.inventory.Item;
import legend.game.inventory.ItemIcon;
import legend.game.inventory.ItemStack;
import legend.game.modding.events.inventory.GatherAttackItemsEvent;
import legend.game.scripting.FlowControl;
import legend.game.scripting.ScriptState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static legend.core.GameEngine.CONFIG;
import static legend.core.GameEngine.EVENTS;
import static legend.game.combat.Battle.seed_800fa754;
import static legend.lodmod.LodConfig.ITEM_STACK_SIZE;

public class AttackBallItem extends Item {
  private static final Logger LOGGER = LogManager.getFormatterLogger(AttackBallItem.class);

  public AttackBallItem() {
    super(ItemIcon.SACK, 50);
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
  public FlowControl useInBattle(final ItemStack stack, final ScriptState<BattleEntity27c> user, final int targetBentIndex) {
    final ItemStack[] stacks = EVENTS.postEvent(new GatherAttackItemsEvent()).getStacks();
    final ItemStack selected = stacks[seed_800fa754.nextInt(stacks.length)];
    LOGGER.info("%s has selected %s", this, selected);

    user.innerStruct_00.item_d4 = selected;
    user.registryIds[0] = selected.getItem().getRegistryId();

    return selected.useInBattle(user, selected.canTarget(TargetType.ALL) ? -1 : targetBentIndex);
  }
}
