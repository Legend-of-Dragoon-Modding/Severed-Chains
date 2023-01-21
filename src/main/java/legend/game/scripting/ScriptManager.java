package legend.game.scripting;

import legend.game.modding.events.EventManager;
import legend.game.modding.events.scripting.ScriptAllocatedEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import javax.annotation.Nullable;

import static legend.game.Scus94491BpeSegment_800b.scriptStatePtrArr_800bc1c0;

public class ScriptManager {
  private static final Logger LOGGER = LogManager.getFormatterLogger(ScriptManager.class);
  private static final Marker SCRIPT_MARKER = MarkerManager.getMarker("SCRIPT");

  private boolean stopped;
  private boolean paused;
  private int upperBound;

  public void tick() {
    this.executeScriptFrame();
    this.executeScriptTickers();
    this.upperBound = 9;
    this.executeScriptRenderers();
  }

  /** Stops the script engine (will not render) */
  public void stop() {
    this.stopped = true;
  }

  /** Restart after stopping */
  public void start() {
    this.stopped = false;
  }

  /** Pauses the script engine (stops ticks but still renders) */
  public void pause() {
    this.paused = true;
  }

  /** Resume after pausing */
  public void resume() {
    this.paused = false;
  }

  public void clear() {
    for(int i = 0; i < 72; i++) {
      scriptStatePtrArr_800bc1c0[i] = null;
    }
  }

  private int findFreeScriptState() {
    this.upperBound++;

    if(this.upperBound >= 72) {
      this.upperBound = 9;
    }

    //LAB_80015824
    //LAB_8001584c
    for(int i = this.upperBound; i < 72; i++) {
      if(scriptStatePtrArr_800bc1c0[i] == null) {
        //LAB_800158c0
        this.upperBound = i;
        return i;
      }
    }

    //LAB_8001586c
    //LAB_80015898
    for(int i = 9; i < this.upperBound; i++) {
      if(scriptStatePtrArr_800bc1c0[i] == null) {
        //LAB_800158c0
        this.upperBound = i;
        return i;
      }
    }

    //LAB_800158b8
    throw new RuntimeException("Ran out of script states");
  }

  public <T> ScriptState<T> allocateScriptState(@Nullable final T type) {
    return this.allocateScriptState(this.findFreeScriptState(), null, 0, type);
  }

  public <T> ScriptState<T> allocateScriptState(final int index, @Nullable final String typeName, final int a4, @Nullable final T type) {
    LOGGER.info(SCRIPT_MARKER, "Allocating script index %d (%s)", index, type != null ? type.getClass().getSimpleName() : "empty");

    final ScriptState<T> scriptState = new ScriptState<>(this, index, type);
    scriptStatePtrArr_800bc1c0[index] = scriptState;

    //LAB_800159c0
    for(int i = 1; i < 25; i++) {
      scriptState.storage_44[i] = -1;
    }

    scriptState.storage_44[0] = index;
    scriptState.storage_44[7] = 0x80f_0000;

    //LAB_800159f8
    //LAB_80015a14
    scriptState.type_f8 = typeName;
    scriptState.ui_fc = a4;

    EventManager.INSTANCE.postEvent(new ScriptAllocatedEvent(index));

    //LAB_80015a34
    return scriptState;
  }

  private void executeScriptFrame() {
    if(this.paused || this.stopped) {
      return;
    }

    //LAB_80015fd8
    for(int index = 0; index < 72; index++) {
      final ScriptState<?> state = scriptStatePtrArr_800bc1c0[index];

      if(state != null) {
        try {
          state.executeFrame();
        } catch(final Throwable t) {
          final RunningScript<?> context = state.context;

          LOGGER.error("Script %d crashed!", index);
          LOGGER.error("File %s[%d]", state.scriptPtr_14.name, state.offset_18);
          LOGGER.error("Parameters:");
          LOGGER.error("  Op param: 0x%x", context.opParam_18);
          for(int i = 0; i < context.paramCount_14; i++) {
            LOGGER.error("  %d: %s", i + 1, context.params_20[i]);
          }

          LOGGER.error("Storage:");
          for(int i = 0; i < state.storage_44.length; i++) {
            LOGGER.error("  %d: 0x%x", i + 1, state.storage_44[i]);
          }

          LOGGER.error("Call stack:");
          for(int i = 0; i < state.callStack_1c.length; i++) {
            if(state.callStack_1c[i] == -1) {
              break;
            }

            LOGGER.error("  %d: %d", i + 1, state.callStack_1c[i]);
          }

          throw new RuntimeException("An error occurred while ticking script " + index, t);
        }
      }

      //LAB_80016614
    }

    //LAB_80016624
  }

  private void executeScriptTickers() {
    if(this.paused || this.stopped) {
      return;
    }

    //LAB_80017750
    for(int i = 0; i < 72; i++) {
      final ScriptState<?> scriptState = scriptStatePtrArr_800bc1c0[i];
      if(scriptState != null) {
        scriptState.tick();
      }
    }

    //LAB_800177ac
    for(int i = 0; i < 72; i++) {
      final ScriptState<?> scriptState = scriptStatePtrArr_800bc1c0[i];
      if(scriptState != null) {
        scriptState.tempTick();
      }
    }
  }

  private void executeScriptRenderers() {
    if(this.stopped) {
      return;
    }

    //LAB_80017854
    for(int i = 0; i < 72; i++) {
      final ScriptState<?> scriptState = scriptStatePtrArr_800bc1c0[i];
      if(scriptState != null) {
        scriptState.render();
      }
    }
  }
}
