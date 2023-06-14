package legend.game.modding.events.battle;

import legend.game.combat.bobj.BattleEvent;

public class DragoonDEFFLoadedEvent extends BattleEvent {
  public final int scriptId;

  public DragoonDEFFLoadedEvent(int scriptId) {
    this.scriptId = scriptId;
  }
}
