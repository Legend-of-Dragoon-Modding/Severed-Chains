package legend.game.modding.events.battle;

import legend.game.combat.Battle;

public class BattleStartedEvent extends BattleStateEvent {
  public BattleStartedEvent(final Battle battle) {
    super(battle);
  }
}
