package legend.game.modding.events.battle;

import legend.game.combat.bent.BattleEvent;
import legend.game.combat.bent.PlayerBattleEntity;

public class CombatBarEvent extends BattleEvent {
  public final PlayerBattleEntity bent;
  public int combatBar;

  public CombatBarEvent(final PlayerBattleEntity bent, final int bar) {
    this.bent = bent;
    this.combatBar = bar;
  }
}
