package legend.lodmod.items;

import legend.game.inventory.Item;

public class PandemoniumItem extends Item {
  public PandemoniumItem() {
    super(0x2d, 200);
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
