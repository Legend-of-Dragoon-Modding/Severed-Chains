package legend.game.modding.events.battle;

import legend.game.combat.bent.BattleEvent;

public class GuardHealEvent extends BattleEvent {
  public final int bentIndex;
  public int heal;

  public GuardHealEvent(final int bentIndex, final int heal) {
    this.bentIndex = bentIndex;
    this.heal = heal;
  }
}
