package legend.game.modding.events.battle;

import legend.game.combat.bent.BattleEvent;
import legend.game.combat.bent.PlayerBattleEntity;

public class CombatMenuBlockedEvent extends BattleEvent {
  public final PlayerBattleEntity bent;
  public int combatBarBlocked;

  public CombatMenuBlockedEvent(final PlayerBattleEntity bent, final int bar) {
    this.bent = bent;
    this.combatBarBlocked = bar;
  }
}
