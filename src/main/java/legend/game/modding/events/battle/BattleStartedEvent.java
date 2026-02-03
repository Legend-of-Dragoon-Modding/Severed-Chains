package legend.game.modding.events.battle;

import legend.game.combat.Battle;
import legend.game.combat.encounters.Encounter;

public class BattleStartedEvent extends BattleStateEvent {
  public BattleStartedEvent(final Battle battle, final Encounter encounter) {
    super(battle, encounter);
  }
}
