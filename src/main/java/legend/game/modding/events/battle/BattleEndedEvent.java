package legend.game.modding.events.battle;

import legend.game.combat.Battle;

public class BattleEndedEvent extends BattleStateEvent {
  public BattleEndedEvent(final Battle battle) {
    super(battle);
  }
}
