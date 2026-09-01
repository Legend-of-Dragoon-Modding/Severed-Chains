package legend.game.characters;

import legend.game.combat.bent.BattleEntity27c;
import legend.lodmod.LodMod;

public class UnaryStatMod implements StatMod<UnaryStat> {
  protected int amount;
  protected boolean percentile;
  protected int turns;
  protected boolean contributesToOtherMods = false;

  protected UnaryStatMod(final int amount, final boolean percentile, final int turns, final boolean contributesToOtherMods) {
    this.amount = amount;
    this.percentile = percentile;
    this.turns = turns;
    this.contributesToOtherMods = contributesToOtherMods;
  }

  @Override
  public StatModType<UnaryStat, UnaryStatMod, UnaryStatModConfig> getType() {
    return LodMod.UNARY_STAT_MOD_TYPE.get();
  }

  @Override
  public StatMod<UnaryStat> copy() {
    return new UnaryStatMod(this.amount, this.percentile, this.turns, this.contributesToOtherMods);
  }

  @Override
  public int apply(final StatCollection stats, final StatType<UnaryStat> type) {
    if(this.percentile) {
      return stats.getStat(type).getRawWithContributingMods() * this.amount / 100;
    }

    return this.amount;
  }

  @Override
  public void turnFinished(final StatCollection stats, final StatType<UnaryStat> type, final BattleEntity27c bent) {
    if(this.turns > 0) {
      this.turns--;
    }
  }

  @Override
  public boolean isFinished(final StatCollection stats, final StatType<UnaryStat> type, final BattleEntity27c bent) {
    return this.turns == 0;
  }

  @Override
  public boolean contributesToOtherMods(final StatCollection stats, final StatType<UnaryStat> type) {
    return this.contributesToOtherMods;
  }
}
