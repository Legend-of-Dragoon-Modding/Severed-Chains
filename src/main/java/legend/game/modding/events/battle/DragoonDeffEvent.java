package legend.game.modding.events.battle;

import legend.game.combat.Battle;
import legend.game.combat.bent.BattleEvent;

public class DragoonDeffEvent extends BattleEvent {
  public final int scriptId;

  public DragoonDeffEvent(final Battle battle, final int scriptId) {
    super(battle);
    this.scriptId = scriptId;
  }
}
