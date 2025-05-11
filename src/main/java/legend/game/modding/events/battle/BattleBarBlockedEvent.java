package legend.game.modding.events.battle;

import legend.game.combat.bent.BattleEvent;

public class BattleBarBlockedEvent extends BattleEvent {
  public int batttleBarBlocked;

  public BattleBarBlockedEvent(final int bar) {
    this.batttleBarBlocked = bar;
  }
}
