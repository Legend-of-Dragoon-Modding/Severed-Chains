package legend.lodmod.items;

import legend.game.combat.bent.BattleEntity27c;
import legend.game.inventory.Item;
import legend.game.modding.events.inventory.GatherRecoveryItemsEvent;
import legend.game.scripting.FlowControl;
import legend.game.scripting.ScriptState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static legend.core.GameEngine.EVENTS;
import static legend.game.combat.Battle.seed_800fa754;

public class RecoveryBallItem extends Item {
  private static final Logger LOGGER = LogManager.getFormatterLogger(RecoveryBallItem.class);

  public RecoveryBallItem() {
    super(46, 50);
  }

  @Override
  public boolean canBeUsed(final UsageLocation location) {
    return location == UsageLocation.BATTLE;
  }

  @Override
  public boolean canTarget(final TargetType type) {
    return type == TargetType.ALLIES;
  }

  @Override
  public FlowControl useInBattle(final ScriptState<BattleEntity27c> user, final int targetBentIndex) {
    final Item[] items = EVENTS.postEvent(new GatherRecoveryItemsEvent()).getItems();
    final Item item = items[seed_800fa754.nextInt(items.length)];
    LOGGER.info("%s has selected %s", this, item);

    user.innerStruct_00.item_d4 = item;
    user.registryIds[0] = item.getRegistryId();

    return item.useInBattle(user, item.canTarget(TargetType.ALL) ? -1 : targetBentIndex);
  }
}
