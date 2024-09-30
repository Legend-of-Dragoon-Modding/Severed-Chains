package legend.game.scripting;

import com.opencsv.exceptions.CsvException;
import legend.game.modding.events.scripting.ScriptAllocatedEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.legendofdragoon.scripting.Compiler;
import org.legendofdragoon.scripting.Lexer;
import org.legendofdragoon.scripting.meta.Meta;
import org.legendofdragoon.scripting.meta.MetaManager;
import org.legendofdragoon.scripting.meta.NoSuchVersionException;
import org.legendofdragoon.scripting.tokens.Script;

import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.file.Path;

import static legend.core.GameEngine.EVENTS;
import static legend.core.IoHelper.intsToBytes;
import static legend.game.Scus94491BpeSegment_800b.input_800bee90;
import static legend.game.Scus94491BpeSegment_800b.press_800bee94;
import static legend.game.Scus94491BpeSegment_800b.repeat_800bee98;
import static legend.game.Scus94491BpeSegment_800b.scriptStatePtrArr_800bc1c0;

public class ScriptManager {
  private static final Logger LOGGER = LogManager.getFormatterLogger(ScriptManager.class);
  private static final Marker SCRIPT_MARKER = MarkerManager.getMarker("SCRIPT");

  private Meta meta;
  private final Compiler compiler = new Compiler();
  private Lexer lexer;
  private final Path patchDir;

  private boolean stopped;
  private boolean paused;
  private int upperBound;

  private int framesPerTick;
  private int currentTicks;

  /** Accumulates joypad input on script engine off frames */
  public int joypadInput;
  /** Accumulates joypad press on script engine off frames */
  public int joypadPress;
  /** Accumulates joypad repeat on script engine off frames */
  public int joypadRepeat;

  public ScriptManager(final Path patchDir) {
    this.patchDir = patchDir;
  }

  public boolean tick() {
    boolean ticked = false;
    this.joypadInput |= input_800bee90;
    this.joypadPress |= press_800bee94;
    this.joypadRepeat |= repeat_800bee98;

    if(this.currentTicks == 0) {
      this.executeScriptFrame();

      this.joypadInput = 0;
      this.joypadPress = 0;
      this.joypadRepeat = 0;

      ticked = true;
    }

    this.executeScriptTickers(ticked);
    this.upperBound = 9;
    this.executeScriptRenderers(ticked);

    this.currentTicks = (this.currentTicks + 1) % this.framesPerTick;
    return ticked;
  }

  /** When running at higher frame rates, the number of frames to wait before ticking the script engine */
  public void setFramesPerTick(final int ticks) {
    this.framesPerTick = ticks;
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
    for(int i = 0; i < scriptStatePtrArr_800bc1c0.length; i++) {
      scriptStatePtrArr_800bc1c0[i] = null;
    }
  }

  private int findFreeScriptState() {
    this.upperBound++;

    if(this.upperBound >= scriptStatePtrArr_800bc1c0.length) {
      this.upperBound = 9;
    }

    //LAB_80015824
    //LAB_8001584c
    for(int i = this.upperBound; i < scriptStatePtrArr_800bc1c0.length; i++) {
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

  public ScriptState<?> getState(final int index) {
    if(index == -1) {
      return null;
    }

    return scriptStatePtrArr_800bc1c0[index];
  }

  public <T> ScriptState<T> getState(final int index, final Class<T> type) {
    if(index == -1) {
      return null;
    }

    return ScriptState.classFor(type).cast(scriptStatePtrArr_800bc1c0[index]);
  }

  public <T> T getObject(final int index, final Class<T> type) {
    if(index < 0 || scriptStatePtrArr_800bc1c0[index] == null) {
      return null;
    }

    return type.cast(scriptStatePtrArr_800bc1c0[index].innerStruct_00);
  }

  public <T> ScriptState<T> allocateScriptState(final String name, @Nullable final T type) {
    return this.allocateScriptState(this.findFreeScriptState(), name, type);
  }

  public <T> ScriptState<T> allocateScriptState(final int index, final String name, @Nullable final T type) {
    LOGGER.info(SCRIPT_MARKER, "Allocating script index %d (%s)", index, name);

    final ScriptState<T> scriptState = new ScriptState<>(this, index, name, type);
    scriptStatePtrArr_800bc1c0[index] = scriptState;

    //LAB_800159c0
    for(int i = 1; i < 33; i++) {
      scriptState.storage_44[i] = -1;
    }

    scriptState.storage_44[0] = index;
    scriptState.storage_44[7] = 0x80f_0000;

    EVENTS.postEvent(new ScriptAllocatedEvent(index));

    //LAB_80015a34
    return scriptState;
  }

  private void executeScriptFrame() {
    if(this.paused || this.stopped) {
      return;
    }

    //LAB_80015fd8
    for(int index = 0; index < scriptStatePtrArr_800bc1c0.length; index++) {
      final ScriptState<?> state = scriptStatePtrArr_800bc1c0[index];

      if(state != null) {
        try {
          state.executeFrame();
        } catch(final Throwable t) {
          state.dump();

          throw new RuntimeException("An error occurred while ticking script " + index, t);
        }
      }

      //LAB_80016614
    }

    //LAB_80016624
  }

  private void executeScriptTickers(final boolean isScriptFrame) {
    if(this.paused || this.stopped) {
      return;
    }

    //LAB_80017750
    for(int i = 0; i < scriptStatePtrArr_800bc1c0.length; i++) {
      final ScriptState<?> scriptState = scriptStatePtrArr_800bc1c0[i];
      // hasExecuted - script has already had a chance to run some of its code, so we can start ticking/rendering
      // isScriptFrame - some effects allocate other effects and expect them to tick/render once they finish (#1530)
      if(scriptState != null && (scriptState.hasExecuted() || isScriptFrame)) {
        scriptState.tick();
      }
    }

    //LAB_800177ac
    for(int i = 0; i < scriptStatePtrArr_800bc1c0.length; i++) {
      final ScriptState<?> scriptState = scriptStatePtrArr_800bc1c0[i];
      if(scriptState != null && (scriptState.hasExecuted() || isScriptFrame)) {
        scriptState.tempTick();
      }
    }
  }

  private void executeScriptRenderers(final boolean isScriptFrame) {
    if(this.stopped) {
      return;
    }

    //LAB_80017854
    for(int i = 0; i < scriptStatePtrArr_800bc1c0.length; i++) {
      final ScriptState<?> scriptState = scriptStatePtrArr_800bc1c0[i];
      // hasExecuted - script has already had a chance to run some of its code, so we can start ticking/rendering
      // isScriptFrame - some effects allocate other effects and expect them to tick/render once they finish (#1530)
      if(scriptState != null && (scriptState.hasExecuted() || isScriptFrame)) {
        scriptState.render();
      }
    }
  }

  public Meta meta() {
    if(this.meta == null) {
      try {
        this.meta = new MetaManager(null, this.patchDir).loadMeta("meta");
      } catch(final IOException | CsvException | NoSuchVersionException e) {
        throw new RuntimeException("Failed to load script patches", e);
      }

      this.lexer = new Lexer(this.meta);
    }

    return this.meta;
  }

  public byte[] compile(final Path path, final String source) {
    this.meta();
    final Script lexed = this.lexer.lex(path, source);
    return intsToBytes(this.compiler.compile(lexed));
  }
}
