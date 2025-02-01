package legend.lodmod.items;

import legend.core.memory.Method;
import legend.game.Scus94491BpeSegment_8002;
import legend.game.combat.bent.BattleEntity27c;
import legend.game.inventory.UseItemResponse;
import legend.lodmod.LodMod;

import static legend.game.Scus94491BpeSegment_800b.stats_800be5f8;

public class RecoverHpItem extends BattleItem {
  private final boolean targetAll;
  private final int percentage;

  public RecoverHpItem(final int icon, final int price, final boolean targetAll, final int percentage) {
    super(icon, price);
    this.targetAll = targetAll;
    this.percentage = percentage;
  }

  @Override
  public boolean canBeUsed(final UsageLocation location) {
    return true;
  }

  @Override
  public boolean canTarget(final TargetType type) {
    return type == TargetType.ALLIES || type == TargetType.ALL && this.targetAll;
  }

  @Override
  @Method(0x80022d88L)
  public void useInMenu(final UseItemResponse response, final int charId) {
    final int amount;
    if(this.percentage == 100) {
      amount = -1;
    } else {
      amount = stats_800be5f8[charId].maxHp_66 * this.percentage / 100;
    }

    response._00 = this.canTarget(TargetType.ALL) ? 3 : 2;
    response.value_04 = this.recover(charId, amount);
  }

  protected int recover(final int charId,final int amount) {
    return Scus94491BpeSegment_8002.addHp(charId, amount);
  }

  @Override
  public boolean isStatMod() {
    return true;
  }

  @Override
  public int calculateStatMod(final BattleEntity27c user, final BattleEntity27c target) {
    user.status_0e |= 0x800;
    return target.stats.getStat(LodMod.HP_STAT.get()).getMax() * this.percentage / 100;
  }

  @Override
  public boolean alwaysHits() {
    return true;
  }
}
