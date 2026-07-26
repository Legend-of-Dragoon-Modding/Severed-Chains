package legend.game.scripting;

import legend.game.modding.events.scripting.ScriptLifecycleEvent;

public class ScriptLifecycleEventMutable extends ScriptLifecycleEvent {
  public ScriptLifecycleEventMutable(final int scriptIndex) {
    super(scriptIndex);
  }

  void setLifecycle(final Lifecycle lifecycle) {
    this.lifecycle = lifecycle;
  }
}
