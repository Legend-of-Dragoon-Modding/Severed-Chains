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
import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.Function;

import static legend.core.GameEngine.EVENTS;
import static legend.core.IoHelper.intsToBytes;
import static legend.game.scripting.ScriptState.FLAG_1_0000;
import static legend.game.scripting.ScriptState.FLAG_FILE_NOT_SET;
import static legend.game.scripting.ScriptState.FLAG_TICKER_NOT_SET;
import static legend.game.scripting.ScriptState.FLAG_DESTRUCTOR_NOT_SET;
import static legend.game.scripting.ScriptState.FLAG_RENDERER_NOT_SET;

public class ScriptManager {
  private static final Logger LOGGER = LogManager.getFormatterLogger(ScriptManager.class);
  private static final Marker SCRIPT_MARKER = MarkerManager.getMarker("SCRIPT");

  public static boolean[] scriptLog = new boolean[108];

  public static final Map<OpType, Function<RunningScript<?>, String>> scriptFunctionDescriptions = new EnumMap<>(OpType.class);

  static {
    scriptFunctionDescriptions.put(OpType.YIELD, r -> "pause;");
    scriptFunctionDescriptions.put(OpType.REWIND, r -> "rewind;");
    scriptFunctionDescriptions.put(OpType.WAIT, r -> {
      final int waitFrames = r.params_20[0].get();

      if(waitFrames != 0) {
        return "wait %d (p0) frames;".formatted(waitFrames);
      }

      return "wait complete - continue;";
    });
    scriptFunctionDescriptions.put(OpType.WAIT_CMP, r -> {
      final Param operandA = r.params_20[0];
      final Param operandB = r.params_20[1];
      final int op = r.opParam_18;

      return (switch(op) {
        case 0 -> "if %s (p0) <= %s (p1)? %s;";
        case 1 -> "if %s (p0) < %s (p1)? %s;";
        case 2 -> "if %s (p0) == %s (p1)? %s;";
        case 3 -> "if %s (p0) != %s (p1)? %s;";
        case 4 -> "if %s (p0) > %s (p1)? %s;";
        case 5 -> "if %s (p0) >= %s (p1)? %s;";
        case 6 -> "if %s (p0) & %s (p1)? %s;";
        case 7 -> "if %s (p0) !& %s (p1)? %s;";
        default -> "illegal cmp 3";
      }).formatted(operandA, operandB, r.scriptState_04.scriptCompare(operandA, operandB, op) ? "yes - continue" : "no - rewind");
    });
    scriptFunctionDescriptions.put(OpType.WAIT_CMP_0, r -> {
      final Param operandB = r.params_20[0];
      final int op = r.opParam_18;

      return (switch(op) {
        case 0 -> "if 0 <= %s (p0)? %s;";
        case 1 -> "if 0 < %s (p0)? %s;";
        case 2 -> "if 0 == %s (p0)? %s;";
        case 3 -> "if 0 != %s (p0)? %s;";
        case 4 -> "if 0 > %s (p0)? %s;";
        case 5 -> "if 0 >= %s (p0)? %s;";
        case 6 -> "if 0 & %s (p0)? %s;";
        case 7 -> "if 0 !& %s (p0)? %s;";
        default -> "illegal cmp 4";
      }).formatted(operandB, r.scriptState_04.scriptCompare(ScriptTempParam.ZERO, operandB, op) ? "yes - continue" : "no - rewind");
    });
    scriptFunctionDescriptions.put(OpType.MOV, r -> "*%s (p1) = 0x%x (p0);".formatted(r.params_20[1], r.params_20[0].get()));
    scriptFunctionDescriptions.put(OpType.SWAP_BROKEN, r -> "tmp = 0x%x (p0); *%s (p1) = tmp; *%s (p0) = tmp; // Broken swap".formatted(r.params_20[0].get(), r.params_20[1], r.params_20[0]));
    scriptFunctionDescriptions.put(OpType.MEMCPY, r -> "memcpy(%s (p1), %s (p2), %d (p0));".formatted(r.params_20[1], r.params_20[2], r.params_20[0].get()));
    scriptFunctionDescriptions.put(OpType.MOV_0, r -> "*%s (p0) = 0;".formatted(r.params_20[0]));
    scriptFunctionDescriptions.put(OpType.AND, r -> "*%s (p1) &= 0x%x (p0);".formatted(r.params_20[1], r.params_20[0].get()));
    scriptFunctionDescriptions.put(OpType.OR, r -> "*%s (p1) |= 0x%x (p0);".formatted(r.params_20[1], r.params_20[0].get()));
    scriptFunctionDescriptions.put(OpType.XOR, r -> "*%s (p1) ^= 0x%x (p0);".formatted(r.params_20[1], r.params_20[0].get()));
    scriptFunctionDescriptions.put(OpType.ANDOR, r -> "*%s (p2) &|= 0x%x (p0), 0x%x (p1);".formatted(r.params_20[2], r.params_20[0].get(), r.params_20[1].get()));
    scriptFunctionDescriptions.put(OpType.NOT, r -> "~*%s (p0);".formatted(r.params_20[0]));
    scriptFunctionDescriptions.put(OpType.SHL, r -> "*%s (p1) <<= 0x%x (p0);".formatted(r.params_20[1], r.params_20[0].get()));
    scriptFunctionDescriptions.put(OpType.SHR, r -> "*%s (p1) >>= 0x%x (p0);".formatted(r.params_20[1], r.params_20[0].get()));
    scriptFunctionDescriptions.put(OpType.ADD, r -> "*%s (p1) += 0x%x (p0);".formatted(r.params_20[1], r.params_20[0].get()));
    scriptFunctionDescriptions.put(OpType.SUB, r -> "*%s (p1) -= 0x%x (p0);".formatted(r.params_20[1], r.params_20[0].get()));
    scriptFunctionDescriptions.put(OpType.SUB_REV, r -> "*%s (p1) = 0x%x (p0) - 0x%x (p1);".formatted(r.params_20[1], r.params_20[0].get(), r.params_20[1].get()));
    scriptFunctionDescriptions.put(OpType.INCR, r -> "*%s (p0) ++;".formatted(r.params_20[0]));
    scriptFunctionDescriptions.put(OpType.DECR, r -> "*%s (p0) --;".formatted(r.params_20[0]));
    scriptFunctionDescriptions.put(OpType.NEG, r -> "-*%s (p0);".formatted(r.params_20[0]));
    scriptFunctionDescriptions.put(OpType.ABS, r -> "|*%s| (p0);".formatted(r.params_20[0]));
    scriptFunctionDescriptions.put(OpType.MUL, r -> "*%s (p1) *= 0x%x (p0);".formatted(r.params_20[1], r.params_20[0].get()));
    scriptFunctionDescriptions.put(OpType.DIV, r -> "*%s (p1) /= 0x%x (p0);".formatted(r.params_20[1], r.params_20[0].get()));
    scriptFunctionDescriptions.put(OpType.DIV_REV, r -> "*%s (p1) = 0x%x (p0) / 0x%x (p1);".formatted(r.params_20[1], r.params_20[0].get(), r.params_20[1].get()));
    scriptFunctionDescriptions.put(OpType.MOD, r -> "*%s (p1) %%= 0x%x (p0);".formatted(r.params_20[1], r.params_20[0].get()));
    scriptFunctionDescriptions.put(OpType.MOD_REV, r -> "*%s (p1) = 0x%x (p0) %% 0x%x (p1);".formatted(r.params_20[1], r.params_20[0].get(), r.params_20[1].get()));
    scriptFunctionDescriptions.put(OpType.MOD43, scriptFunctionDescriptions.get(OpType.MOD));
    scriptFunctionDescriptions.put(OpType.MOD_REV44, scriptFunctionDescriptions.get(OpType.MOD_REV));
    scriptFunctionDescriptions.put(OpType.SQRT, r -> "*%s (p1) = sqrt(0x%x (p0));".formatted(r.params_20[1], r.params_20[0].get()));
    scriptFunctionDescriptions.put(OpType.SIN_12, r -> "*%s (p1) = sin(0x%x (p0));".formatted(r.params_20[1], r.params_20[0].get()));
    scriptFunctionDescriptions.put(OpType.COS_12, r -> "*%s (p1) = cos(0x%x (p0));".formatted(r.params_20[1], r.params_20[0].get()));
    scriptFunctionDescriptions.put(OpType.ATAN2_12, r -> "*%s (p2) = ratan2(0x%x (p0), 0x%x (p1));".formatted(r.params_20[2], r.params_20[0].get(), r.params_20[1].get()));
    scriptFunctionDescriptions.put(OpType.CALL, r -> "subfunc(%d (pp));".formatted(r.opParam_18));
    scriptFunctionDescriptions.put(OpType.JMP, r -> "jmp %s (p0);".formatted(r.params_20[0]));
    scriptFunctionDescriptions.put(OpType.JMP_CMP, r -> {
      final Param operandA = r.params_20[0];
      final Param operandB = r.params_20[1];
      final int op = r.opParam_18;
      final Param dest = r.params_20[2];

      return (switch(op) {
        case 0 -> "if %s (p0) <= %s (p1)? %s;";
        case 1 -> "if %s (p0) < %s (p1)? %s;";
        case 2 -> "if %s (p0) == %s (p1)? %s;";
        case 3 -> "if %s (p0) != %s (p1)? %s;";
        case 4 -> "if %s (p0) > %s (p1)? %s;";
        case 5 -> "if %s (p0) >= %s (p1)? %s;";
        case 6 -> "if %s (p0) & %s (p1)? %s;";
        case 7 -> "if %s (p0) !& %s (p1)? %s;";
        default -> "illegal cmp 65";
      }).formatted(operandA, operandB, r.scriptState_04.scriptCompare(operandA, operandB, op) ? "yes - jmp %s (p2)".formatted(dest) : "no - continue");
    });
    scriptFunctionDescriptions.put(OpType.JMP_CMP_0, r -> {
      final Param operandB = r.params_20[0];
      final int op = r.opParam_18;
      final Param dest = r.params_20[1];

      return (switch(op) {
        case 0 -> "if 0 <= %s (p0)? %s;";
        case 1 -> "if 0 < %s (p0)? %s;";
        case 2 -> "if 0 == %s (p0)? %s;";
        case 3 -> "if 0 != %s (p0)? %s;";
        case 4 -> "if 0 > %s (p0)? %s;";
        case 5 -> "if 0 >= %s (p0)? %s;";
        case 6 -> "if 0 & %s (p0)? %s;";
        case 7 -> "if 0 !& %s (p0)? %s;";
        default -> "illegal cmp 66";
      }).formatted(operandB, r.scriptState_04.scriptCompare(ScriptTempParam.ZERO, operandB, op) ? "yes - jmp %s (p1)".formatted(dest) : "no - continue");
    });
    scriptFunctionDescriptions.put(OpType.WHILE, r -> "if(--%s (p0) != 0) jmp %s (p1)".formatted(r.params_20[0], r.params_20[1]));
    scriptFunctionDescriptions.put(OpType.GOSUB, r -> "gosub %s (p0);".formatted(r.params_20[0]));
    scriptFunctionDescriptions.put(OpType.RETURN, r -> "return;");
    scriptFunctionDescriptions.put(OpType.GOSUB_TABLE, r -> {
      final Param a = r.params_20[1];
      final Param b = r.params_20[0];
      final Param ptr = a.array(a.array(b.get()).get());
      return "gosub %s (p1[p1[p0]]);".formatted(ptr);
    });

    scriptFunctionDescriptions.put(OpType.DEALLOCATE, r -> "deallocate; pause; rewind;");

    scriptFunctionDescriptions.put(OpType.DEALLOCATE82, r -> "deallocate children; pause; rewind;");
    scriptFunctionDescriptions.put(OpType.DEALLOCATE_OTHER, r -> "deallocate %s (p0);%s".formatted(r.params_20[0], r.scriptState_04.index == r.params_20[0].get() ? "; pause; rewind;" : ""));
  }

  private Meta meta;
  private final Compiler compiler = new Compiler();
  private Lexer lexer;
  private final Path patchDir;

  private boolean stopped;
  private boolean paused;
  private int upperBound;

  private int framesPerTick;
  private int currentTicks;

  private final ScriptState<?>[] scriptStatePtrArr_800bc1c0 = new ScriptState[108];

  public ScriptManager(final Path patchDir) {
    this.patchDir = patchDir;
  }

  public boolean willTick() {
    return this.currentTicks == 0;
  }

  public boolean tick() {
    boolean ticked = false;

    if(this.currentTicks == 0) {
      this.executeScriptFrame();
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

  public boolean isPaused() {
    return this.paused;
  }

  public void clear() {
    Arrays.fill(this.scriptStatePtrArr_800bc1c0, null);
  }

  private int findFreeScriptState() {
    this.upperBound++;

    if(this.upperBound >= this.scriptStatePtrArr_800bc1c0.length) {
      this.upperBound = 9;
    }

    //LAB_80015824
    //LAB_8001584c
    for(int i = this.upperBound; i < this.scriptStatePtrArr_800bc1c0.length; i++) {
      if(this.scriptStatePtrArr_800bc1c0[i] == null) {
        //LAB_800158c0
        this.upperBound = i;
        return i;
      }
    }

    //LAB_8001586c
    //LAB_80015898
    for(int i = 9; i < this.upperBound; i++) {
      if(this.scriptStatePtrArr_800bc1c0[i] == null) {
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

    return this.scriptStatePtrArr_800bc1c0[index];
  }

  public <T extends ScriptedObject> ScriptState<T> getState(final int index, final Class<T> type) {
    if(index == -1) {
      return null;
    }

    return ScriptState.classFor(type).cast(this.scriptStatePtrArr_800bc1c0[index]);
  }

  public <T> T getObject(final int index, final Class<T> type) {
    if(index < 0 || this.scriptStatePtrArr_800bc1c0[index] == null) {
      return null;
    }

    return type.cast(this.scriptStatePtrArr_800bc1c0[index].innerStruct_00);
  }

  public int count() {
    return this.scriptStatePtrArr_800bc1c0.length;
  }

  public <T extends ScriptedObject> ScriptState<T> allocateScriptState(final String name, @Nullable final T type) {
    return this.allocateScriptState(this.findFreeScriptState(), name, type);
  }

  public <T extends ScriptedObject> ScriptState<T> allocateScriptState(final int index, final String name, @Nullable final T type) {
    LOGGER.info(SCRIPT_MARKER, "Allocating script index %d (%s)", index, name);

    final ScriptState<T> scriptState = new ScriptState<>(this, index, name, type);
    this.scriptStatePtrArr_800bc1c0[index] = scriptState;

    //LAB_800159c0
    for(int i = 1; i < ScriptState.STORAGE_COUNT; i++) {
      scriptState.setStor(i, -1);
    }

    scriptState.setStor(0, index);
    scriptState.setFlag(FLAG_1_0000 | FLAG_FILE_NOT_SET | FLAG_TICKER_NOT_SET | FLAG_RENDERER_NOT_SET | FLAG_DESTRUCTOR_NOT_SET);

    EVENTS.postEvent(new ScriptAllocatedEvent(index));

    //LAB_80015a34
    return scriptState;
  }

  void deallocate(final int index) {
    this.scriptStatePtrArr_800bc1c0[index] = null;
  }

  private void executeScriptFrame() {
    if(this.paused || this.stopped) {
      return;
    }

    //LAB_80015fd8
    for(int index = 0; index < this.scriptStatePtrArr_800bc1c0.length; index++) {
      final ScriptState<?> state = this.scriptStatePtrArr_800bc1c0[index];

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
    for(int i = 0; i < this.scriptStatePtrArr_800bc1c0.length; i++) {
      final ScriptState<?> scriptState = this.scriptStatePtrArr_800bc1c0[i];
      // hasExecuted - script has already had a chance to run some of its code, so we can start ticking/rendering
      // isScriptFrame - some effects allocate other effects and expect them to tick/render once they finish (#1530)
      if(scriptState != null && (scriptState.hasExecuted() || isScriptFrame)) {
        try {
          scriptState.tick();
        } catch(final Throwable t) {
          scriptState.dump();

          throw new RuntimeException("An error occurred while ticking scripted object " + i, t);
        }
      }
    }

    //LAB_800177ac
    for(int i = 0; i < this.scriptStatePtrArr_800bc1c0.length; i++) {
      final ScriptState<?> scriptState = this.scriptStatePtrArr_800bc1c0[i];
      if(scriptState != null && (scriptState.hasExecuted() || isScriptFrame)) {
        try {
          scriptState.tempTick();
        } catch(final Throwable t) {
          scriptState.dump();

          throw new RuntimeException("An error occurred while temp-ticking scripted object " + i, t);
        }
      }
    }
  }

  private void executeScriptRenderers(final boolean isScriptFrame) {
    if(this.stopped) {
      return;
    }

    //LAB_80017854
    for(int i = 0; i < this.scriptStatePtrArr_800bc1c0.length; i++) {
      final ScriptState<?> scriptState = this.scriptStatePtrArr_800bc1c0[i];
      // hasExecuted - script has already had a chance to run some of its code, so we can start ticking/rendering
      // isScriptFrame - some effects allocate other effects and expect them to tick/render once they finish (#1530)
      if(scriptState != null && (scriptState.hasExecuted() || isScriptFrame)) {
        try {
          scriptState.render();
        } catch(final Throwable t) {
          scriptState.dump();

          throw new RuntimeException("An error occurred while rendering scripted object " + i, t);
        }
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
