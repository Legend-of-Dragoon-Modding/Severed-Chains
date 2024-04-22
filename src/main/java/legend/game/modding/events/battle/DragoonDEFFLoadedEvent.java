package legend.game.modding.events.battle;

import legend.game.combat.bent.BattleEvent;

public class DragoonDEFFLoadedEvent extends BattleEvent {
  public final int scriptId;

  public DragoonDEFFLoadedEvent(final int scriptId) {
    this.scriptId = scriptId;
  }
}
