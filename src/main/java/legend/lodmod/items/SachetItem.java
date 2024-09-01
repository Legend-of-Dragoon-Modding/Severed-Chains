package legend.lodmod.items;

import legend.game.inventory.Item;

public class SachetItem extends Item {
  public SachetItem() {
    super(46, 200);
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
