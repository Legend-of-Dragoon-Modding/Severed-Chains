package legend.game.modding.events.battle;

import legend.game.combat.bent.BattleEvent;
import legend.game.combat.bent.PlayerBattleEntity;

public class CombatMenuEvent extends BattleEvent {
  public final PlayerBattleEntity bent;
  public int combatBar;

  public CombatMenuEvent(final PlayerBattleEntity bent, final int bar) {
    this.bent = bent;
    this.combatBar = bar;
  }
}
