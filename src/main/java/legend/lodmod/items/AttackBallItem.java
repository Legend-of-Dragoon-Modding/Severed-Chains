package legend.lodmod.items;

import legend.game.inventory.Item;

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
}
