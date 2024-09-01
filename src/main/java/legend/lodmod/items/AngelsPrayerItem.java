package legend.lodmod.items;

import legend.core.memory.Method;
import legend.game.combat.bent.BattleEntity27c;
import legend.game.inventory.Item;
import legend.game.inventory.UseItemResponse;

public class AngelsPrayerItem extends Item {
  public AngelsPrayerItem() {
    super(36, 15);
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
  public boolean isStatMod() {
    return true;
  }

  @Override
  public int calculateStatMod(final BattleEntity27c user, final BattleEntity27c target) {
    return 0;
  }

  @Override
  public boolean alwaysHits() {
    return true;
  }
}
