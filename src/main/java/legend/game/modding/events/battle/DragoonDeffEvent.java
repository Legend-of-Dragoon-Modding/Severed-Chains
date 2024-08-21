package legend.game.modding.events.battle;

import legend.game.combat.bent.BattleEvent;

public class DragoonDeffEvent extends BattleEvent {
  public final int scriptId;

  public DragoonDeffEvent(final int scriptId) {
    this.scriptId = scriptId;
  }
}
