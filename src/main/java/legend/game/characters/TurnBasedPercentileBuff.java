package legend.game.characters;

import legend.game.combat.bobj.BattleObject27c;

public class TurnBasedPercentileBuff implements StatMod<UnaryStat> {
  private final int percent;
  private int turns;

  public TurnBasedPercentileBuff(final int percent, final int turns) {
    this.percent = percent;
    this.turns = turns;
  }

  @Override
  public int apply(final StatCollection stats, final StatType<UnaryStat> type) {
    return stats.getStat(type).getRaw() * this.percent / 100;
  }

  @Override
  public void turnFinished(final StatCollection stats, final StatType<UnaryStat> type, final BattleObject27c bobj) {
    this.turns--;
  }

  @Override
  public boolean isFinished() {
    return this.turns <= 0;
  }
}
