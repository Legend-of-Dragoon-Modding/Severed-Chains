package legend.game.modding.events.battle;

import legend.game.combat.bent.BattleEvent;

public class BattleBarEvent extends BattleEvent {
  public int batttleBar;

  public BattleBarEvent(final int bar) {
    this.batttleBar = bar;
  }
}
