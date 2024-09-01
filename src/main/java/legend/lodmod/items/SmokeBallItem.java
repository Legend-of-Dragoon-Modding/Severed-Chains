package legend.lodmod.items;

import legend.game.inventory.Item;

public class SmokeBallItem extends Item {
  public SmokeBallItem() {
    super(46, 200);
  }

  @Override
  public boolean canBeUsed(final UsageLocation location) {
    return location == UsageLocation.BATTLE;
  }

  @Override
  public boolean canTarget(final TargetType type) {
    return false;
  }
}
