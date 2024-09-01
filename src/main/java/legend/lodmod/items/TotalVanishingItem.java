package legend.lodmod.items;

import legend.game.combat.bent.BattleEntity27c;
import legend.game.inventory.Item;

public class TotalVanishingItem extends Item {
  public TotalVanishingItem() {
    super(46, 10);
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
  public int getSpecialEffect(final BattleEntity27c user, final BattleEntity27c target) {
    if((target.specialEffectFlag_14 & 0x80) != 0) { // Resistance
      return -1;
    }

    return 0;
  }
}
