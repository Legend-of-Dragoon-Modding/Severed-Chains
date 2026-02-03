package legend.game.modding.events.battle;

import legend.game.combat.Battle;
import legend.game.combat.bent.BattleEvent;
import legend.game.combat.encounters.Encounter;

public class BattleStateEvent extends BattleEvent {
  public final Encounter encounter;

  public BattleStateEvent(final Battle battle, final Encounter encounter) {
    super(battle);
    this.encounter = encounter;
  }
}
