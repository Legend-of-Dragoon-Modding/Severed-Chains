package legend.game.modding.events.battle;

import legend.game.combat.bent.BattleEvent;

public class CombatBarEvent extends BattleEvent {
  public int combatBar;

  public CombatBarEvent(final int bar) {
    this.combatBar = bar;
  }
}
