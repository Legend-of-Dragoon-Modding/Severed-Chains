package legend.game.modding.events.battle;

import legend.game.combat.Battle;
import legend.game.combat.bent.BattleEvent;

public class BattleStateEvent extends BattleEvent {
  public BattleStateEvent(final Battle battle) {
    super(battle);
  }
}
