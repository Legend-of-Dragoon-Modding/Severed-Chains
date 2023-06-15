package legend.game.modding.events.battle;

public class DragoonDEFFLoadedEvent extends BattleEvent {
  public final int scriptId;

  public DragoonDEFFLoadedEvent(int scriptId) {
    this.scriptId = scriptId;
  }
}
