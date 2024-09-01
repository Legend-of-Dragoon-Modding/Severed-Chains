package legend.lodmod.items;

import legend.core.memory.Method;
import legend.game.combat.bent.BattleEntity27c;
import legend.game.inventory.Item;
import legend.game.inventory.UseItemResponse;

import static legend.game.SItem.characterCount_8011d7c4;
import static legend.game.Scus94491BpeSegment.simpleRand;
import static legend.game.Scus94491BpeSegment_800b.characterIndices_800bdbb8;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;

public class RecoverStatusItem extends Item {
  private final int status;

  public RecoverStatusItem(final int price, final int status) {
    super(37, price);
    this.status = status;
  }

  @Override
  public boolean canBeUsed(final UsageLocation location) {
    return true;
  }

  @Override
  public boolean canTarget(final TargetType type) {
    return type == TargetType.ALLIES;
  }

  @Override
  public boolean canBeUsedNow(final UsageLocation location) {
    if(location == UsageLocation.MENU) {
      int allStatus = 0;
      for(int i = 0; i < characterCount_8011d7c4; i++) {
        allStatus |= gameState_800babc8.charData_32c[characterIndices_800bdbb8[i]].status_10;
      }

      return (this.status & allStatus) != 0;
    }

    throw new RuntimeException("Not yet implemented");
  }

  @Override
  @Method(0x80022d88L)
  public void useInMenu(final UseItemResponse response, final int charId) {
    final int status = gameState_800babc8.charData_32c[charId].status_10;

    if((this.status & status) != 0) {
      response.value_04 = status;
      gameState_800babc8.charData_32c[charId].status_10 &= ~status;
    }

    response._00 = 7;
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

  /** TODO I dunno if this does anything for status recovery */
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
