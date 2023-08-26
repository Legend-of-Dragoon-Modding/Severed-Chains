package legend.game.characters;

import legend.game.combat.bent.BattleEntity27c;

public class TurnBasedFlatBuff implements StatMod<UnaryStat> {
  private final int amount;
  private int turns;

  public TurnBasedFlatBuff(final int amount, final int turns) {
    this.amount = amount;
    this.turns = turns;
  }

  @Override
  public int apply(final StatCollection stats, final StatType<UnaryStat> type) {
    return this.amount;
  }

  @Override
  public void turnFinished(final StatCollection stats, final StatType<UnaryStat> type, final BattleEntity27c bent) {
    this.turns--;
  }

  @Override
  public boolean isFinished() {
    return this.turns <= 0;
  }
}
