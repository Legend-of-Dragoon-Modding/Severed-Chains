package legend.game.modding.events.battle;

import legend.game.combat.bent.BattleEvent;

public class CombatBarBlockedEvent extends BattleEvent {
  public int combatBarBlocked;

  public CombatBarBlockedEvent(final int bar) {
    this.combatBarBlocked = bar;
  }
}
