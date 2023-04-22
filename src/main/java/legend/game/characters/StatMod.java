package legend.game.characters;

import legend.game.combat.bobj.BattleObject27c;

public interface StatMod<T extends Stat> {
  /** Return the amount you want added to the stat */
  int apply(final StatCollection stats, final StatType<T> type);
  void turnFinished(final StatCollection stats, final StatType<T> type, final BattleObject27c bobj);
  boolean isFinished();
}
