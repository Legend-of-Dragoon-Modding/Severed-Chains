package legend.game.modding.events.scripting;

import legend.game.modding.events.Event;

public class ScriptEvent extends Event {
  public final int scriptIndex;

  public ScriptEvent(final int scriptIndex) {
    this.scriptIndex = scriptIndex;
  }
}
