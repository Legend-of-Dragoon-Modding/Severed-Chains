package legend.game.characters;

import legend.game.combat.bent.BattleEntity27c;
import legend.lodmod.LodMod;

public class FractionalStatMod implements StatMod<FractionalStat> {
  protected int amount;
  protected boolean percentile;
  protected int turns;

  protected FractionalStatMod(final int amount, final boolean percentile, final int turns) {
    this.amount = amount;
    this.percentile = percentile;
    this.turns = turns;
  }

  @Override
  public StatModType<FractionalStat, FractionalStatMod, FractionalStatModConfig> getType() {
    return LodMod.FRACTIONAL_STAT_MOD_TYPE.get();
  }

  @Override
  public int apply(final StatCollection stats, final StatType<FractionalStat> type) {
    if(this.percentile) {
      return stats.getStat(type).getMaxRaw() * this.amount / 100;
    }

    return this.amount;
  }

  @Override
  public void turnFinished(final StatCollection stats, final StatType<FractionalStat> type, final BattleEntity27c bent) {
    if(this.turns > 0) {
      this.turns--;
    }
  }

  @Override
  public boolean isFinished(final StatCollection stats, final StatType<FractionalStat> type, final BattleEntity27c bent) {
    return this.turns == 0;
  }
}
