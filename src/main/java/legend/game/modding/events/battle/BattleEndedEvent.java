package legend.game.modding.events.battle;

import legend.game.combat.Battle;
import legend.game.combat.encounters.Encounter;

public class BattleEndedEvent extends BattleStateEvent {
  public BattleEndedEvent(final Battle battle, final Encounter encounter) {
    super(battle, encounter);
  }
}
