package legend.game.characters;

import legend.game.combat.bent.BattleEntity27c;

public interface StatMod<T extends Stat> {
  StatModType<T, ? extends StatMod<T>, ? extends StatModConfig> getType();

  /** Return the amount you want added to the stat */
  int apply(final StatCollection stats, final StatType<T> type);
  void turnFinished(final StatCollection stats, final StatType<T> type, final BattleEntity27c bent);
  boolean isFinished(final StatCollection stats, final StatType<T> type, final BattleEntity27c bent);
}
