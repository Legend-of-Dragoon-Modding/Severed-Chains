package legend.lodmod.items;

import legend.game.combat.bent.BattleEntity27c;
import legend.game.inventory.Item;
import legend.game.modding.events.inventory.GatherAttackItemsEvent;
import legend.game.scripting.FlowControl;
import legend.game.scripting.ScriptState;

import static legend.core.GameEngine.EVENTS;
import static legend.game.combat.Battle.seed_800fa754;

public class AttackBallItem extends Item {
  public AttackBallItem() {
    super(46, 50);
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
  public FlowControl useInBattle(final ScriptState<BattleEntity27c> user, final int targetBentIndex) {
    final Item[] items = EVENTS.postEvent(new GatherAttackItemsEvent()).getItems();
    final Item item = items[seed_800fa754.nextInt(items.length)];
    user.innerStruct_00.item_d4 = item;
    user.registryIds[0] = item.getRegistryId();
    return item.useInBattle(user, item.canTarget(TargetType.ALL) ? -1 : targetBentIndex);
  }
}
