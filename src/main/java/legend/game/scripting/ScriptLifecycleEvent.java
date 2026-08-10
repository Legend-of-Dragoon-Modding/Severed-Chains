package legend.game.scripting;

import legend.game.modding.events.scripting.ScriptEvent;

public class ScriptLifecycleEvent extends ScriptEvent {
  Lifecycle lifecycle;

  public ScriptLifecycleEvent(final int scriptIndex) {
    super(scriptIndex);
  }

  public Lifecycle getLifecycle() {
    return this.lifecycle;
  }

  public enum Lifecycle {
    /** Called when this script state is allocated */
    ALLOCATED,
    /** Called before this script state's script ticks */
    PRE_SCRIPT_VM_TICK,
    /** Called after this script state's script ticks */
    POST_SCRIPT_VM_TICK,
    /** Called before this script state's tick callback runs */
    PRE_TICK_CALLBACK,
    /** Called after this script state's tick callback runs */
    POST_TICK_CALLBACK,
    /** Called before this script state's render callback runs */
    PRE_RENDER_CALLBACK,
    /** Called after this script state's render callback runs */
    POST_RENDER_CALLBACK,
    /** Called before this script state is deallocated */
    PRE_DEALLOCATE,
    /** Called after this script state is deallocated */
    POST_DEALLOCATE,
  }
}
