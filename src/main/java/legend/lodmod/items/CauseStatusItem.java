package legend.lodmod.items;

import legend.game.combat.bent.BattleEntity27c;
import legend.game.inventory.Item;

import static legend.game.Scus94491BpeSegment.simpleRand;

public class CauseStatusItem extends Item {
  private final int status;

  public CauseStatusItem(final int icon, final int price, final int status) {
    super(icon, price);
    this.status = status;
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
  public boolean isStatMod() {
    return true;
  }

  @Override
  public int calculateStatMod(final BattleEntity27c user, final BattleEntity27c target) {
    return 0;
  }

  @Override
  public int getSpecialEffect(final BattleEntity27c user, final BattleEntity27c target) {
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
}
