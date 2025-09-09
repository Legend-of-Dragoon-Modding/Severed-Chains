package legend.game.scripting;

import legend.core.DebugHelper;
import legend.core.MathHelper;
import legend.core.memory.Method;
import legend.game.Scus94491BpeSegment_8004;
import legend.game.combat.bent.BattleEntity27c;
import legend.game.modding.events.scripting.ScriptDeallocatedEvent;
import legend.game.modding.events.scripting.ScriptTickEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.legendofdragoon.modloader.registries.RegistryId;
import org.legendofdragoon.scripting.Disassembler;
import org.legendofdragoon.scripting.Translator;
import org.legendofdragoon.scripting.tokens.Script;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Objects;
import java.util.function.BiConsumer;

import static legend.core.GameEngine.EVENTS;
import static legend.core.GameEngine.SCRIPTS;
import static legend.game.Scus94491BpeSegment.rcos;
import static legend.game.Scus94491BpeSegment.rsin;
import static legend.game.Scus94491BpeSegment.scriptFunctionDescriptions;
import static legend.game.Scus94491BpeSegment.scriptLog;
import static legend.game.Scus94491BpeSegment.simpleRand;
import static legend.game.Scus94491BpeSegment_8004.engineStateFunctions_8004e29c;
import static legend.game.Scus94491BpeSegment_8004.scriptSubFunctions_8004e29c;
import static legend.game.Scus94491BpeSegment_800b.scriptStatePtrArr_800bc1c0;

/** Holds persistent data for scripts */
public class ScriptState<T extends ScriptedObject> {
  private static final Logger LOGGER = LogManager.getFormatterLogger(ScriptState.class);
  private static final Marker SCRIPT_MARKER = MarkerManager.getMarker("SCRIPT");

  private final ScriptManager manager;
  public final RunningScript<T> context = new RunningScript<>(this);

  /** This script's index */
  public final int index;
  public final String name;
  public final T innerStruct_00;
  public BiConsumer<ScriptState<T>, T> ticker_04;
  public BiConsumer<ScriptState<T>, T> renderer_08;
  public BiConsumer<ScriptState<T>, T> destructor_0c;
  /** If the callback returns non-zero, it's set to null */
  public TempTicker<T> tempTicker_10;
  private final LinkedList<ScriptStackFrame> callStack = new LinkedList<>();
  /**
   * <ul>
   *   <li>0 - my script index</li>
   *   <li>5 - parent script index</li>
   *   <li>6 - child script index</li>
   *   <li>
   *     <p>7 - flags bit set - which of the pointers at the start of the struct are set</p>
   *
   *     <ul>
   *       <li>Bit 17 - {@link ScriptState#callStack}.{@link ScriptStackFrame#file} is unset</li>
   *       <li>Bit 18 - {@link ScriptState#ticker_04} is unset</li>
   *       <li>Bit 19 - {@link ScriptState#renderer_08} is unset</li>
   *       <li>Bit 20 - Child script</li>
   *       <li>Bit 21 - Parent script</li>
   *       <li>Bit 26 - {@link ScriptState#tempTicker_10} is set (note: not sure why this is backwards from the others)</li>
   *       <li>Bit 27 - {@link ScriptState#destructor_0c} is unset</li>
   *     </ul>
   *
   *     <ul>
   *       <li>If bits 17 and 20 are not set, the script will be executed</li>
   *       <li>If bits 18 and 20 are not set, {@link ScriptState#ticker_04} will be executed</li>
   *       <li>If bits 19 and 20 are not set, {@link ScriptState#renderer_08} will be executed</li>
   *       <li>If bit 26 is set and bit 20 is not set, {@link ScriptState#tempTicker_10} will be executed</li>
   *       <li>If bits 27 and 20 are not set, {@link ScriptState#destructor_0c} will be executed</li>
   *     </ul>
   *
   *     <p>In combat this variable is used for a few different things:</p>
   *     <ul>
   *       <li>0x1 - {@link BattleEntity27c#FLAG_1}</li>
   *       <li>0x2 - {@link BattleEntity27c#FLAG_DRAGOON}</li>
   *       <li>0x4 - {@link BattleEntity27c#FLAG_MONSTER}</li>
   *       <li>0x8 - {@link BattleEntity27c#FLAG_CURRENT_TURN}</li>
   *       <li>0x10 - {@link BattleEntity27c#FLAG_HIDE}</li>
   *       <li>0x20 - {@link BattleEntity27c#FLAG_TAKE_FORCED_TURN}</li>
   *       <li>0x40 - {@link BattleEntity27c#FLAG_DEAD}</li>
   *       <li>0x80 - {@link BattleEntity27c#FLAG_ANIMATE_ONCE}</li>
   *       <li>0x100 - {@link BattleEntity27c#FLAG_100}</li>
   *       <li>0x200 - {@link BattleEntity27c#FLAG_200}</li>
   *       <li>0x400 - {@link BattleEntity27c#FLAG_400}</li>
   *       <li>0x800 - {@link BattleEntity27c#FLAG_NO_SCRIPT}</li>
   *       <li>0x1000 - {@link BattleEntity27c#FLAG_1000}</li>
   *       <li>0x2000 - {@link BattleEntity27c#FLAG_NO_LOOT}</li>
   *       <li>0x4000 - {@link BattleEntity27c#FLAG_CANT_TARGET}</li>
   *       <li>0x8000 - {@link BattleEntity27c#FLAG_8000}</li>
   *       <li>0x20_0000 - ? used in scripts</li>
   *     </ul>
   *   </li>
   *   <li>
   *     8 - Battle Menu flag - controls which options you can choose from.
   *     <ul>
   *       <li>0x01 Attack</li>
   *       <li>0x02 Guard</li>
   *       <li>0x04 Items</li>
   *       <li>0x08 Escape</li>
   *       <li>0x10 Dragoon</li>
   *       <li>0x20 D-Attack</li>
   *       <li>0x40 Magic</li>
   *       <li>0x80 Special</li>
   *     <ul>
   *   </li>
   *   <li>9 - is dragoon</li>
   * </ul>
   */
  public final int[] storage_44 = new int[33];
  public final RegistryId[] registryIds = new RegistryId[100];

  private boolean paused;
  private int ticks;

  public static <T extends ScriptedObject> Class<ScriptState<T>> classFor(final Class<T> cls) {
    return (Class<ScriptState<T>>)(Class<?>)ScriptState.class;
  }

  public ScriptState(final ScriptManager manager, final int index, final String name, @Nullable final T innerStruct) {
    this.manager = manager;
    this.index = index;
    this.name = name;
    this.innerStruct_00 = innerStruct;
  }

  public void setTicker(@Nullable final BiConsumer<ScriptState<T>, T> callback) {
    if(callback == null) {
      this.ticker_04 = null;
      this.storage_44[7] |= 0x4_0000;
    } else {
      this.ticker_04 = callback;
      this.storage_44[7] &= ~0x4_0000;
    }
  }

  public void setRenderer(@Nullable final BiConsumer<ScriptState<T>, T> callback) {
    if(callback == null) {
      this.renderer_08 = null;
      this.storage_44[7] |= 0x8_0000;
    } else {
      this.renderer_08 = callback;
      this.storage_44[7] &= ~0x8_0000;
    }
  }

  public void setDestructor(@Nullable final BiConsumer<ScriptState<T>, T> callback) {
    if(callback == null) {
      this.destructor_0c = null;
      this.storage_44[7] |= 0x800_0000;
    } else {
      this.destructor_0c = callback;
      this.storage_44[7] &= ~0x800_0000;
    }
  }

  public void setTempTicker(@Nullable final TempTicker<T> callback) {
    if(callback == null) {
      this.tempTicker_10 = null;
      this.storage_44[7] &= ~0x400_0000;
    } else {
      this.tempTicker_10 = callback;
      this.storage_44[7] |= 0x400_0000;
    }
  }

  public void pause() {
    this.paused = true;
  }

  public void resume() {
    this.paused = false;
  }

  public boolean isPaused() {
    return this.paused;
  }

  /** Script has run at least one script tick */
  public boolean hasExecuted() {
    return this.ticks != 0;
  }

  void tick() {
    if((this.storage_44[7] & 0x14_0000) == 0) {
      this.ticker_04.accept(this, this.innerStruct_00);
    }
  }

  void tempTick() {
    if((this.storage_44[7] & 0x410_0000) == 0x400_0000) {
      if(this.tempTicker_10.run(this, this.innerStruct_00)) {
        this.setTempTicker(null);
      }
    }
  }

  void render() {
    if((this.storage_44[7] & 0x18_0000) == 0) {
      this.renderer_08.accept(this, this.innerStruct_00);
    }
  }

  public void loadScriptFile(@Nullable final ScriptFile script) {
    this.loadScriptFile(script, 0);
  }

  public void loadScriptFile(@Nullable final ScriptFile script, final int entrypointIndex) {
    if(script != null) {
      LOGGER.info(SCRIPT_MARKER, "Loading script %s into index %d (entry point 0x%x)", script.name, this.index, entrypointIndex);

      this.callStack.clear();
      this.pushFrame(new ScriptStackFrame(script, script.getEntry(entrypointIndex)));
      this.storage_44[7] &= ~0x2_0000;
    } else {
      LOGGER.info(SCRIPT_MARKER, "Clearing script index %d", this.index);

      this.callStack.clear();
      this.storage_44[7] |= 0x2_0000;
    }
  }

  public void deallocate() {
    LOGGER.info(SCRIPT_MARKER, "Deallocating script state %d", this.index);

    EVENTS.postEvent(new ScriptDeallocatedEvent(this.index));

    if((this.storage_44[7] & 0x810_0000) == 0) {
      try {
        this.destructor_0c.accept(this, this.innerStruct_00);
      } catch(final NullPointerException e) {
        LOGGER.error("Script %d destructor was null", this.index);
        throw e;
      }
    }

    //LAB_80015c70
    scriptStatePtrArr_800bc1c0[this.index] = null;
  }

  public void deallocateChildren() {
    LOGGER.info(SCRIPT_MARKER, "Deallocating script %d children", this.index);

    int childIndex = this.storage_44[6];

    //LAB_80015cdc
    while(childIndex >= 0) {
      final ScriptState<?> childState = scriptStatePtrArr_800bc1c0[childIndex];
      final int childChildIndex = childState.storage_44[6];
      childState.deallocate();
      childIndex = childChildIndex;
    }

    //LAB_80015d04
    this.storage_44[6] = -1;
    this.storage_44[7] &= ~0x20_0000;
  }

  public void deallocateWithChildren() {
    this.deallocateChildren();
    this.deallocate();
  }

  public ScriptState<?> fork() {
    final ScriptState<?> childScript = this.manager.allocateScriptState("Forked " + this.name, null);

    if(LOGGER.isInfoEnabled(SCRIPT_MARKER)) {
      final StackWalker.StackFrame frame = DebugHelper.getCallerFrame();
      LOGGER.info(SCRIPT_MARKER, "Forking script %d to %d %s.%s(%s:%d)", this.index, childScript.index, frame.getClassName(), frame.getMethodName(), frame.getFileName(), frame.getLineNumber());
    }

    //LAB_80015ddc
    childScript.storage_44[7] = this.storage_44[7] | 0x10_0000; // Child
    this.storage_44[7] |= 0x20_0000; // Parent

    //LAB_80015e0c
    System.arraycopy(this.storage_44, 8, childScript.storage_44, 8, 25);
    System.arraycopy(this.registryIds, 0, childScript.registryIds, 0, childScript.registryIds.length);

    childScript.storage_44[5] = this.index;
    childScript.storage_44[6] = this.storage_44[6];
    this.storage_44[6] = childScript.index;

    childScript.callStack.clear();
    for(int i = this.callStack.size() - 1; i >= 0; i--) {
      childScript.pushFrame(this.callStack.get(i).copy());
    }

    //LAB_80015e4c
    return childScript;
  }

  /** Deallocates child and assumes its identity? */
  public int consumeChild() {
    final int childIndex = this.storage_44[6];
    if(childIndex < 0) {
      throw new RuntimeException("Null command");
    }

    LOGGER.info("Consuming script %d child %d", this.index, childIndex);

    final ScriptState<?> child = scriptStatePtrArr_800bc1c0[childIndex];
    if((child.storage_44[7] & 0x20_0000) != 0) { // Is parent
      this.storage_44[7] |= 0x20_0000;
      this.storage_44[6] = child.storage_44[6];
      scriptStatePtrArr_800bc1c0[child.storage_44[6]].storage_44[5] = this.index;
    } else {
      //LAB_80015ef0
      this.storage_44[6] = -1;
      this.storage_44[7] &= ~0x20_0000;
    }

    //LAB_80015f08
    //LAB_80015f14
    System.arraycopy(child.storage_44, 8, this.storage_44, 8, 25);

    this.callStack.clear();
    this.callStack.addAll(child.callStack);
    child.deallocate();

    //LAB_80015f54
    return this.frame().offset;
  }

  public ScriptStackFrame frame() {
    return this.callStack.peek();
  }

  public ScriptStackFrame frame(final int index) {
    return this.callStack.get(index);
  }

  public ScriptStackFrame replaceFrame(final ScriptStackFrame frame) {
    this.callStack.pop();
    this.callStack.push(frame);
    return frame;
  }

  public ScriptStackFrame pushFrame(final ScriptStackFrame frame) {
    this.callStack.push(frame);
    return frame;
  }

  public ScriptStackFrame pushFrame() {
    return this.pushFrame(this.frame().copy());
  }

  public ScriptStackFrame popFrame() {
    return this.callStack.pop();
  }

  public int callStackDepth() {
    return this.callStack.size();
  }

  void executeFrame() {
    this.ticks++;

    if((this.storage_44[7] & 0x12_0000) == 0 && !this.paused) {
      this.context.commandOffset_0c = this.frame().offset;
      this.context.opOffset_08 = this.context.commandOffset_0c;

      if(scriptLog[this.index]) {
        LOGGER.info(SCRIPT_MARKER, "Exec script index %d", this.index);
      }

      FlowControl ret;
      //LAB_80016018
      do {
        final int opCommand = this.context.getOp();
        this.context.opIndex_10 = OpType.byOpcode(opCommand & 0xff);
        this.context.paramCount_14 = opCommand >>> 8 & 0xff;
        this.context.opParam_18 = opCommand >>> 16;

        if(scriptLog[this.index]) {
          LOGGER.info(SCRIPT_MARKER, "0x%x (%d)", this.context.commandOffset_0c, this.context.commandOffset_0c);
          LOGGER.info(SCRIPT_MARKER, "param[p] = %x", opCommand >>> 16);
        }

        if(this.context.paramCount_14 > 10) {
          throw new RuntimeException("Too many parameters!");
        }

        this.context.commandOffset_0c++;

        for(int paramIndex = 0; paramIndex < this.context.paramCount_14; paramIndex++) {
          this.context.params_20[paramIndex] = this.parseParam();

          if(scriptLog[this.index]) {
            LOGGER.info(SCRIPT_MARKER, "params[%d] = %s", paramIndex, this.context.params_20[paramIndex]);
          }
        }

        EVENTS.postEvent(new ScriptTickEvent(this.index));

        if(scriptLog[this.index]) {
          if(scriptFunctionDescriptions.containsKey(this.context.opIndex_10)) {
            LOGGER.info(SCRIPT_MARKER, scriptFunctionDescriptions.get(this.context.opIndex_10).apply(this.context));
          } else {
            LOGGER.info(SCRIPT_MARKER, "Running callback %s", this.context.opIndex_10);
          }
        }

        //LAB_80016598
        ret = this.runOp(this.context.opIndex_10, this.context);

        if(scriptLog[this.index]) {
          if(ret == FlowControl.PAUSE) {
            LOGGER.info(SCRIPT_MARKER, "Pausing");
          } else if(ret == FlowControl.PAUSE_AND_REWIND) {
            LOGGER.info(SCRIPT_MARKER, "Rewinding and pausing");
          }
        }

        // Returning 0 continues execution
        // Returning 1 pauses execution until the next frame
        // Returning anything else pauses execution and repeats the same instruction next frame
        if(ret == FlowControl.CONTINUE || ret == FlowControl.PAUSE) {
          //LAB_800165e8
          this.context.opOffset_08 = this.context.commandOffset_0c;
        }

        Arrays.fill(this.context.params_20, null);
      } while(ret == FlowControl.CONTINUE && !this.paused);

      //LAB_800165f4
      this.frame().offset = this.context.opOffset_08;
    }
  }

  private Param parseParam() {
    final int childCommand = this.context.getOp();
    final int paramType = childCommand >>> 24;
    final int cmd2 = childCommand >>> 16 & 0xff;
    final int cmd1 = childCommand >>> 8 & 0xff;
    final int cmd0 = childCommand & 0xff;

    this.context.commandOffset_0c++;

    if(paramType == 0x1) { // Push next value after this param
      final Param param = new ScriptInlineParam(this, this.context.commandOffset_0c);
      this.context.commandOffset_0c++;
      return param;
    }

    if(paramType == 0x2) { // Push storage[cmd0]
      return new ScriptStorageParam(this, cmd0);
    }

    if(paramType == 0x3) { // Push script[script[script[this].storage[cmd0]].storage[cmd1]].storage[cmd2]
      final int otherScriptIndex1 = this.storage_44[cmd0];
      final int otherScriptIndex2 = scriptStatePtrArr_800bc1c0[otherScriptIndex1].storage_44[cmd1];
      return new ScriptStorageParam(scriptStatePtrArr_800bc1c0[otherScriptIndex2], cmd2);
    }

    if(paramType == 0x4) { // Push script[script[this].storage[cmd0]].storage[cmd1 + script[this].storage[cmd2]]
      final int otherScriptIndex = this.storage_44[cmd0];
      final int storageIndex = cmd1 + this.storage_44[cmd2];
      return new ScriptStorageParam(scriptStatePtrArr_800bc1c0[otherScriptIndex], storageIndex);
    }

    if(paramType == 0x5) { // Push gameVar[cmd0]
      return new GameVarParam(cmd0);
    }

    if(paramType == 0x6) { // Push gameVar[cmd0 + script[this].storage[cmd1]]
      return new GameVarParam(cmd0 + this.storage_44[cmd1]);
    }

    if(paramType == 0x7) { // Push gameVar[cmd0][script[this].storage[cmd1]]
      final int arrIndex = this.storage_44[cmd1];
      return new GameVarArrayParam(cmd0, arrIndex);
    }

    if(paramType == 0x8) { // Push gameVar[cmd0 + script[this].storage[cmd1]][script[this].storage[cmd2]]
      final int storage1 = this.storage_44[cmd1];
      final int storage2 = this.storage_44[cmd2];
      return new GameVarArrayParam(cmd0 + storage1, storage2);
    }

    if(paramType == 0x9) { // INLINE_1 Push (commandStart + (cmd0 | cmd1 << 8) * 4)
      return new ScriptInlineParam(this, this.context.opOffset_08 + (short)childCommand);
    }

    if(paramType == 0xa) { // INLINE_2 Push (commandStart + (script[this].storage[cmd2] + (cmd0 | cmd1 << 8)) * 4)
      final int storage = this.storage_44[cmd2];
      return new ScriptInlineParam(this, this.context.opOffset_08 + ((short)childCommand + storage));
    }

    if(paramType == 0xb) { // INLINE_TABLE_1 Push (commandStart[commandStart[script[this].storage[cmd2] + (cmd0 | cmd1 << 8)] + (cmd0 | cmd1 << 8)])
      final int storage = this.storage_44[cmd2];
      return new ScriptInlineParam(this, this.context.opOffset_08).array((short)childCommand + new ScriptInlineParam(this, this.context.opOffset_08).array((short)childCommand + storage).get());
    }

    if(paramType == 0xc) { // INLINE_TABLE_2 Push commandStart[commandStart[script[this].storage[cmd0]] + script[this].storage[cmd1]]
      final Param param = new ScriptInlineParam(this, this.context.commandOffset_0c).array(new ScriptInlineParam(this, this.context.commandOffset_0c).array(this.storage_44[cmd0]).get() + this.storage_44[cmd1]);
      this.context.commandOffset_0c++;
      return param;
    }

    if(paramType == 0xd) { // Push script[script[this].storage[cmd0]].storage[cmd1 + cmd2]
      return new ScriptStorageParam(scriptStatePtrArr_800bc1c0[this.storage_44[cmd0]], cmd1 + cmd2);
    }

    if(paramType == 0xe) { // Push gameVar[cmd0 + cmd1]
      return new GameVarParam(cmd0 + cmd1);
    }
    if(paramType == 0xf) { // Push gameVar[cmd0][cmd1]
      return new GameVarArrayParam(cmd0, cmd1);
    }

    if(paramType == 0x10) { // Push gameVar[cmd0 + script[this].storage[cmd1]][cmd2]
      return new GameVarArrayParam(cmd0 + this.storage_44[cmd1], cmd2);
    }

    if(paramType == 0x11) {
      return new GameVarArrayParam(cmd0 + cmd1, this.storage_44[cmd2]); // Haven't verified this, afaik it's never used
    }

    if(paramType == 0x12) {
      throw new RuntimeException("Not implemented");
    }

    if(paramType == 0x13) { // INLINE_3
      return new ScriptInlineParam(this, this.context.opOffset_08).array((short)childCommand + cmd2);
    }

    if(paramType == 0x14) { // INLINE_TABLE_3 Push commandStart[(cmd0 | cmd1 << 8) + commandStart[(cmd0 | cmd1 << 8) + cmd2]]
      return new ScriptInlineParam(this, this.context.opOffset_08).array((short)childCommand + new ScriptInlineParam(this, this.context.opOffset_08).array((short)childCommand + cmd2).get());
    }

    if(paramType == 0x15) {
      final Param param = new ScriptInlineParam(this, this.context.commandOffset_0c).array(new ScriptInlineParam(this, this.context.commandOffset_0c).array(this.storage_44[cmd0]).get() + cmd1);
      this.context.commandOffset_0c++;
      return param;
    }

    if(paramType == 0x16) {
      final Param param = new ScriptInlineParam(this, new ScriptInlineParam(this, this.context.commandOffset_0c).array(cmd0).get() + this.storage_44[cmd1]);
      this.context.commandOffset_0c++;
      return param;
    }

    if(paramType == 0x17) { // INLINE_TABLE_4
      final Param param = new ScriptInlineParam(this, this.context.commandOffset_0c).array(new ScriptInlineParam(this, this.context.commandOffset_0c).array(cmd0).get() + cmd1);
      this.context.commandOffset_0c++;
      return param;
    }

    if(paramType == 0x20) { // Script state registry ID pointer
      return new ScriptStateRegistryIdParam(this, cmd0);
    }

    if(paramType == 0x21) { // String registry ID
      final Param param = new ScriptInlineRegistryIdParam(this, this.context.commandOffset_0c, cmd2);
      this.context.commandOffset_0c += (cmd2 + 3) / 4;
      return param;
    }

    if(paramType == 0x22) { // Null
      return new ScriptStateNullRegistryIdParam();
    }

    if(paramType == 0x23) { // Var registry ID
      return new ScriptStateVarRegistryIdParam(this, cmd0);
    }

    // Treated as an immediate if not a valid op
    return new ScriptInlineParam(this, this.context.commandOffset_0c - 1);
  }

  private FlowControl runOp(final OpType op, final RunningScript<?> script) {
    return switch(op) {
      case YIELD -> this.scriptPause();
      case REWIND -> this.scriptRewindAndPause();
      case WAIT -> this.scriptWait();
      case WAIT_CMP -> this.scriptCompare();
      case WAIT_CMP_0 -> this.scriptCompare0();

      case MOV -> this.scriptMove();
      case SWAP_BROKEN -> this.FUN_80016790();
      case MEMCPY -> this.scriptMemCopy();

      case MOV_0 -> this.scriptSetZero();

      case AND -> this.scriptAnd(script);
      case OR -> this.scriptOr(script);
      case XOR -> this.scriptXor();
      case ANDOR -> this.scriptAndOr();
      case NOT -> this.scriptNot();
      case SHL -> this.scriptShiftLeft(script);
      case SHR -> this.scriptShiftRightArithmetic();

      case ADD -> this.scriptAdd();
      case SUB -> this.scriptSubtract();
      case SUB_REV -> this.scriptSubtract2();
      case INCR -> this.scriptIncrementBy1();
      case DECR -> this.scriptDecrementBy1();
      case NEG -> this.scriptNegate();
      case ABS -> this.scriptAbs();

      case MUL -> this.scriptMultiply();
      case DIV -> this.scriptDivide();
      case DIV_REV -> this.scriptDivide2();
      case MOD, MOD43 -> this.scriptMod();
      case MOD_REV, MOD_REV44 -> this.scriptMod2();

      case MUL_12 -> this.scriptMultiply12();
      case DIV_12 -> this.scriptDivide12();
      case DIV_12_REV -> this.scriptDivide2_12();

      case SQRT -> this.scriptSquareRoot();
      case RAND -> this.scriptRandom();
      case SIN_12 -> this.scriptSin();
      case COS_12 -> this.scriptCos();
      case ATAN2_12 -> this.scriptRatan2();

      case CALL -> this.scriptExecuteSubFunc();

      case JMP -> this.scriptJump();
      case JMP_CMP -> this.scriptConditionalJump();
      case JMP_CMP_0 -> this.scriptConditionalJump0();
      case WHILE -> this.scriptWhile();
      case JMP_TABLE -> this.scriptJumpTable();

      case GOSUB -> this.scriptJumpAndLink();
      case RETURN -> this.scriptJumpReturn();
      case GOSUB_TABLE -> this.scriptJumpAndLinkTable();

      case DEALLOCATE -> this.scriptDeallocateSelf();

      case DEALLOCATE82 -> this.scriptDeallocateChildren();
      case DEALLOCATE_OTHER -> this.scriptDeallocateOther();

      case FORK -> this.scriptForkAndJump();
      case FORK_REENTER -> this.scriptForkAndReenter();
      case CONSUME -> this.scriptConsumeChild();

      case NOOP_96 -> this.FUN_800172f4();
      case NOOP_97 -> this.FUN_800172fc();
      case NOOP_98 -> this.FUN_80017304();
      case DEPTH -> this.scriptGetCallStackDepth();

      default -> throw new IllegalArgumentException("Unknown script op " + op);
    };
  }

  @Method(0x8001664cL)
  public boolean scriptCompare(final Param operandA, final Param operandB, final int op) {
    // Check A for null
    if(operandA.isRegistryId() && !operandB.isRegistryId() && operandB.get() == 0) {
      return switch(op) {
        case 2 -> operandA.getRegistryId() == null;
        case 3 -> operandA.getRegistryId() != null;
        default -> throw new IllegalArgumentException("Registry IDs can only be compared using == or !=");
      };
    }

    // Check B for null
    if(operandB.isRegistryId() && !operandA.isRegistryId() && operandA.get() == 0) {
      return switch(op) {
        case 2 -> operandB.getRegistryId() == null;
        case 3 -> operandB.getRegistryId() != null;
        default -> throw new IllegalArgumentException("Registry IDs can only be compared using == or !=");
      };
    }

    // Compare registry IDs
    if(operandA.isRegistryId() && operandB.isRegistryId()) {
      return switch(op) {
        case 2 -> Objects.equals(operandA.getRegistryId(), operandB.getRegistryId());
        case 3 -> !Objects.equals(operandA.getRegistryId(), operandB.getRegistryId());
        default -> throw new IllegalArgumentException("Registry IDs can only be compared using == or !=");
      };
    }

    // Standard compare
    return switch(op) {
      case 0 -> operandA.get() <= operandB.get();
      case 1 -> operandA.get() < operandB.get();
      case 2 -> operandA.get() == operandB.get();
      case 3 -> operandA.get() != operandB.get();
      case 4 -> operandA.get() > operandB.get();
      case 5 -> operandA.get() >= operandB.get();
      case 6 -> (operandA.get() & operandB.get()) != 0;
      case 7 -> (operandA.get() & operandB.get()) == 0;
      default -> false;
    };
  }

  /** Stop execution for this frame, resume next frame */
  @Method(0x800166d0L)
  public FlowControl scriptPause() {
    return FlowControl.PAUSE;
  }

  /** Stop execution for this frame, resume next frame and repeat same command */
  @Method(0x800166d8L)
  public FlowControl scriptRewindAndPause() {
    return FlowControl.PAUSE_AND_REWIND;
  }

  /**
   * Subtracts 1 from work array value 0 if nonzero
   *
   * @return 0 if value is already 0; 2 if value was decremented
   */
  @Method(0x800166e0L)
  public FlowControl scriptWait() {
    if(this.context.params_20[0].get() != 0) {
      this.context.params_20[0].decr();
      return FlowControl.PAUSE_AND_REWIND;
    }

    return FlowControl.CONTINUE;
  }

  /**
   * <p>Compares work array values 0 and 2 based on an operand stored in the parent param</p>
   *
   * <p>
   *   Operations:
   *   <ol start="0">
   *     <li>Less than or equal to</li>
   *     <li>Less than</li>
   *     <li>Equal</li>
   *     <li>Inequal</li>
   *     <li>Greater than</li>
   *     <li>Greater than or equal to</li>
   *     <li>And</li>
   *     <li>Nand</li>
   *   </ol>
   * </p>
   *
   * @return 0 if comparison succeeds, otherwise return 2
   */
  @Method(0x8001670cL)
  public FlowControl scriptCompare() {
    return this.scriptCompare(this.context.params_20[0], this.context.params_20[1], this.context.opParam_18) ? FlowControl.CONTINUE : FlowControl.PAUSE_AND_REWIND;
  }

  /** Same as {@link #scriptCompare()} with first param set to 0 */
  @Method(0x80016744L)
  public FlowControl scriptCompare0() {
    return this.scriptCompare(ScriptTempParam.ZERO, this.context.params_20[0], this.context.opParam_18) ? FlowControl.CONTINUE : FlowControl.PAUSE_AND_REWIND;
  }

  /**
   * Set work array value 1 to value 0
   *
   * @return 0
   */
  @Method(0x80016774L)
  public FlowControl scriptMove() {
    this.context.params_20[1].set(this.context.params_20[0]);
    return FlowControl.CONTINUE;
  }

  /** Pretty sure this is _supposed_ to be a swap */
  @Method(0x80016790L)
  public FlowControl FUN_80016790() {
    final int v1 = this.context.params_20[0].get();
    this.context.params_20[1].set(v1);
    this.context.params_20[0].set(v1);
    return FlowControl.CONTINUE;
  }

  /**
   * Copy block of memory at work array parameter 1 to block of memory at work array parameter 2. Word count is at work array parameter 0.
   */
  @Method(0x800167bcL)
  public FlowControl scriptMemCopy() {
    // There are hundreds of nearly- (and sometimes entirely-) identical scripts that perform memcopies at the
    // end of the script, running out of bounds. This is a retail bug and seems to be inconsequential. We're
    // going to take the traditional LoD approach and just pretend it's not happening. I think these are generic
    // NPC controller scripts, but I'm not sure. It tends to happen when NPCs are disappearing (walking into Dart,
    // Lloyd walking into a cave in Snow Field, etc.)
    // See: GH#230, GH#236, GH#237, GH#240
    try {
      for(int i = 0; i < this.context.params_20[0].get(); i++) {
        this.context.params_20[2].array(i).set(this.context.params_20[1].array(i));
      }
    } catch(final IndexOutOfBoundsException e) {
      LOGGER.warn(SCRIPT_MARKER, "Script %d attempted to read out of bounds", this.index);
    }

    return FlowControl.CONTINUE;
  }

  @Method(0x80016854L)
  public FlowControl scriptSetZero() {
    this.context.params_20[0].set(0);
    return FlowControl.CONTINUE;
  }

  @Method(0x80016868L)
  public FlowControl scriptAnd(final RunningScript<?> script) {
    this.context.params_20[1].and(script.params_20[0].get());
    return FlowControl.CONTINUE;
  }

  @Method(0x8001688cL)
  public FlowControl scriptOr(final RunningScript<?> script) {
    this.context.params_20[1].or(script.params_20[0].get());
    return FlowControl.CONTINUE;
  }

  @Method(0x800168b0L)
  public FlowControl scriptXor() {
    this.context.params_20[1].xor(this.context.params_20[0].get());
    return FlowControl.CONTINUE;
  }

  @Method(0x800168d4L)
  public FlowControl scriptAndOr() {
    this.context.params_20[2]
      .and(this.context.params_20[0].get())
      .or(this.context.params_20[1].get());
    return FlowControl.CONTINUE;
  }

  @Method(0x80016900L)
  public FlowControl scriptNot() {
    this.context.params_20[0].not();
    return FlowControl.CONTINUE;
  }

  /**
   * Shift work array value 1 left by value 0 bits
   */
  @Method(0x80016920L)
  public FlowControl scriptShiftLeft(final RunningScript<?> script) {
    this.context.params_20[1].shl(script.params_20[0].get());
    return FlowControl.CONTINUE;
  }

  /**
   * Shift work array value 1 right (arithmetic) by value 0 bits
   */
  @Method(0x80016944L)
  public FlowControl scriptShiftRightArithmetic() {
    this.context.params_20[1].set(this.context.params_20[1].get() >> this.context.params_20[0].get());
    return FlowControl.CONTINUE;
  }

  /**
   * Increment work array value 1 by value 0 (overflow allowed)
   */
  @Method(0x80016968L)
  public FlowControl scriptAdd() {
    this.context.params_20[1].add(this.context.params_20[0].get());
    return FlowControl.CONTINUE;
  }

  /**
   * Decrement work array value 1 by value 0 (overflow allowed)
   */
  @Method(0x8001698cL)
  public FlowControl scriptSubtract() {
    this.context.params_20[1].sub(this.context.params_20[0].get());
    return FlowControl.CONTINUE;
  }

  @Method(0x800169b0L)
  public FlowControl scriptSubtract2() {
    this.context.params_20[1].set(this.context.params_20[0].get() - this.context.params_20[1].get());
    return FlowControl.CONTINUE;
  }

  /**
   * Increment work array value 0 by 1
   */
  @Method(0x800169d4L)
  public FlowControl scriptIncrementBy1() {
    this.context.params_20[0].incr();
    return FlowControl.CONTINUE;
  }

  /**
   * Decrement work array value 0 by 1
   *
   * @return 0
   */
  @Method(0x800169f4L)
  public FlowControl scriptDecrementBy1() {
    this.context.params_20[0].decr();
    return FlowControl.CONTINUE;
  }

  @Method(0x80016a14L)
  public FlowControl scriptNegate() {
    this.context.params_20[0].neg();
    return FlowControl.CONTINUE;
  }

  @Method(0x80016a34L)
  public FlowControl scriptAbs() {
    //LAB_80016a50
    this.context.params_20[0].abs();
    return FlowControl.CONTINUE;
  }

  /**
   * Multiply work array value 1 by value 0 (overflow allowed)
   */
  @Method(0x80016a5cL)
  public FlowControl scriptMultiply() {
    this.context.params_20[1].mul(this.context.params_20[0].get());
    return FlowControl.CONTINUE;
  }

  /**
   * Divide work array value 1 by value 0
   */
  @Method(0x80016a84L)
  public FlowControl scriptDivide() {
    final int divisor = this.context.params_20[0].get();

    if(divisor == 0) {
      if(this.context.params_20[1].get() >= 0) {
        this.context.params_20[1].set(-1);
      } else {
        this.context.params_20[1].set(1);
      }

      return FlowControl.CONTINUE;
    }

    this.context.params_20[1].div(divisor);
    return FlowControl.CONTINUE;
  }

  @Method(0x80016ab0L)
  public FlowControl scriptDivide2() {
    this.context.params_20[1].set(MathHelper.safeDiv(this.context.params_20[0].get(), this.context.params_20[1].get()));
    return FlowControl.CONTINUE;
  }

  @Method(0x80016adcL)
  public FlowControl scriptMod() {
    this.context.params_20[1].mod(this.context.params_20[0].get());
    return FlowControl.CONTINUE;
  }

  @Method(0x80016b04L)
  public FlowControl scriptMod2() {
    this.context.params_20[1].set(this.context.params_20[0].get() % this.context.params_20[1].get());
    return FlowControl.CONTINUE;
  }

  /** Note: reduces likelihood of overflow at cost of precision */
  @Method(0x80016b2cL)
  public FlowControl scriptMultiply12() {
    this.context.params_20[1].set((this.context.params_20[1].get() >> 4) * (this.context.params_20[0].get() >> 4) >> 4);
    return FlowControl.CONTINUE;
  }

  /** Note: reduces likelihood of overflow at cost of precision */
  @Method(0x80016b5cL)
  public FlowControl scriptDivide12() {
    if(this.context.params_20[0].get() == 0) {
      this.context.params_20[1].set(0);
      return FlowControl.CONTINUE;
    }

    this.context.params_20[1].set(((this.context.params_20[1].get() << 4) / this.context.params_20[0].get()) << 8);
    return FlowControl.CONTINUE;
  }

  /** Note: reduces likelihood of overflow at cost of precision */
  @Method(0x80016b8cL)
  public FlowControl scriptDivide2_12() {
    if(this.context.params_20[1].get() == 0) {
      this.context.params_20[1].set(0);
      return FlowControl.CONTINUE;
    }

    this.context.params_20[1].set(((this.context.params_20[0].get() << 4) / this.context.params_20[1].get()) << 8);
    return FlowControl.CONTINUE;
  }

  /**
   * Calculate square root of work array value 0 and store in value 1
   */
  @Method(0x80016bbcL)
  public FlowControl scriptSquareRoot() {
    this.context.params_20[1].set((int)Math.sqrt(this.context.params_20[0].get()));
    return FlowControl.CONTINUE;
  }

  @Method(0x80016c00L)
  public FlowControl scriptRandom() {
    this.context.params_20[1].set(this.context.params_20[0].get() * simpleRand() >>> 16);
    return FlowControl.CONTINUE;
  }

  @Method(0x80016c4cL)
  public FlowControl scriptSin() {
    this.context.params_20[1].set(rsin(this.context.params_20[0].get()));
    return FlowControl.CONTINUE;
  }

  @Method(0x80016c80L)
  public FlowControl scriptCos() {
    this.context.params_20[1].set(rcos(this.context.params_20[0].get()));
    return FlowControl.CONTINUE;
  }

  @Method(0x80016cb4L)
  public FlowControl scriptRatan2() {
    this.context.params_20[2].set(MathHelper.radToPsxDeg(MathHelper.atan2(this.context.params_20[0].get(), this.context.params_20[1].get())));
    return FlowControl.CONTINUE;
  }

  /**
   * Executes the sub-function at {@link Scus94491BpeSegment_8004#scriptSubFunctions_8004e29c} denoted by the parent param
   *
   * @return The value that the sub-function returns
   */
  @Method(0x80016cfcL)
  public FlowControl scriptExecuteSubFunc() {
    try {
      if(engineStateFunctions_8004e29c[this.context.opParam_18] != null) {
        return engineStateFunctions_8004e29c[this.context.opParam_18].apply(this.context);
      }

      return scriptSubFunctions_8004e29c[this.context.opParam_18].apply(this.context);
    } catch(final UnsupportedOperationException e) {
      throw new RuntimeException("Script subfunc %d error".formatted(this.context.opParam_18), e);
    }
  }

  /**
   * Jump to the value at work array element 0
   *
   * @return 0
   */
  @Method(0x80016d38L)
  public FlowControl scriptJump() {
    this.context.params_20[0].jump(this.context);
    return FlowControl.CONTINUE;
  }

  /**
   * <p>Compares value at work array element 0 to element 1 using operation denoted by parent param. If true, jumps to value at element 2.</p>
   *
   * <p>
   *   Operations:
   *   <ol start="0">
   *     <li>Less than or equal to</li>
   *     <li>Less than</li>
   *     <li>Equal</li>
   *     <li>Inequal</li>
   *     <li>Greater than</li>
   *     <li>Greater than or equal to</li>
   *     <li>And</li>
   *     <li>Nand</li>
   *   </ol>
   * </p>
   *
   * @return 0
   */
  @Method(0x80016d4cL)
  public FlowControl scriptConditionalJump() {
    if(this.scriptCompare(this.context.params_20[0], this.context.params_20[1], this.context.opParam_18)) {
      this.context.params_20[2].jump(this.context);
    }

    //LAB_80016d8c
    return FlowControl.CONTINUE;
  }


  /**
   * <p>Compares constant 0 to work array element 0 using operation denoted by parent param. If true, jumps to value at element 1.</p>
   *
   * <p>
   *   Operations:
   *   <ol start="0">
   *     <li>Less than or equal to</li>
   *     <li>Less than</li>
   *     <li>Equal</li>
   *     <li>Inequal</li>
   *     <li>Greater than</li>
   *     <li>Greater than or equal to</li>
   *     <li>And</li>
   *     <li>Nand</li>
   *   </ol>
   * </p>
   *
   * @return 0
   */
  @Method(0x80016da0L)
  public FlowControl scriptConditionalJump0() {
    if(this.scriptCompare(ScriptTempParam.ZERO, this.context.params_20[0], this.context.opParam_18)) {
      this.context.params_20[1].jump(this.context);
    }

    //LAB_80016dd8
    return FlowControl.CONTINUE;
  }

  /**
   * Decrements param0 and jumps to param1 if param0 > 0... maybe used for do...while loops?
   */
  @Method(0x80016decL)
  public FlowControl scriptWhile() {
    this.context.params_20[0].decr();

    if(this.context.params_20[0].get() != 0) {
      this.context.params_20[1].jump(this.context);
    }

    //LAB_80016e14
    return FlowControl.CONTINUE;
  }

  @Method(0x80016e1cL)
  public FlowControl scriptJumpTable() {
    this.context.params_20[1].array(this.context.params_20[1].array(this.context.params_20[0].get()).get()).jump(this.context);
    return FlowControl.CONTINUE;
  }

  /**
   * Pushes the current command to the command stack and jumps to the value at work array element 0.
   *
   * @return 0
   */
  @Method(0x80016e50L)
  public FlowControl scriptJumpAndLink() {
    this.frame().offset = this.context.commandOffset_0c;
    this.pushFrame();
    this.context.params_20[0].jump(this.context);
    return FlowControl.CONTINUE;
  }

  /**
   * Return from a JumpAndLink
   *
   * @return 0
   */
  @Method(0x80016f28L)
  public FlowControl scriptJumpReturn() {
    this.popFrame();
    this.context.commandOffset_0c = this.frame().offset;
    return FlowControl.CONTINUE;
  }

  @Method(0x80016ffcL)
  public FlowControl scriptJumpAndLinkTable() {
    this.frame().offset = this.context.commandOffset_0c;
    this.pushFrame();

    // p1[p1[p0]]
    this.context.params_20[1].array(this.context.params_20[1].array(this.context.params_20[0].get()).get()).jump(this.context);
    return FlowControl.CONTINUE;
  }

  @Method(0x800170f4L)
  public FlowControl scriptDeallocateSelf() {
    this.context.scriptState_04.deallocateWithChildren();
    return FlowControl.PAUSE_AND_REWIND;
  }

  @Method(0x80017138L)
  public FlowControl scriptDeallocateChildren() {
    this.context.scriptState_04.deallocateWithChildren();
    return FlowControl.PAUSE_AND_REWIND;
  }

  @Method(0x80017160L)
  public FlowControl scriptDeallocateOther() {
    final ScriptState<?> state = scriptStatePtrArr_800bc1c0[this.context.params_20[0].get()];
    state.deallocateWithChildren();
    return state == this.context.scriptState_04 ? FlowControl.PAUSE_AND_REWIND : FlowControl.CONTINUE;
  }

  /** Forks the script and jumps to an address */
  @Method(0x800171c0L)
  public FlowControl scriptForkAndJump() {
    LOGGER.info(SCRIPT_MARKER, "Script %d forking script %s and jumping to %s", this.context.scriptState_04.index, this.context.params_20[0], this.context.params_20[1]);

    final ScriptState<?> stateThatWasForked = scriptStatePtrArr_800bc1c0[this.context.params_20[0].get()];
    stateThatWasForked.fork();
    this.context.params_20[1].jump(stateThatWasForked);
    stateThatWasForked.storage_44[32] = this.context.params_20[2].get();
    return FlowControl.CONTINUE;
  }

  /** Forks the script and jumps to an entry point */
  @Method(0x80017234L)
  public FlowControl scriptForkAndReenter() {
    LOGGER.info(SCRIPT_MARKER, "Script %d forking script %s and re-entering at offset %s", this.context.scriptState_04.index, this.context.params_20[0], this.context.params_20[1]);

    final ScriptState<?> stateThatWasForked = scriptStatePtrArr_800bc1c0[this.context.params_20[0].get()];
    stateThatWasForked.fork();
    stateThatWasForked.frame().offset = stateThatWasForked.frame().file.getEntry(this.context.params_20[1].get());
    stateThatWasForked.storage_44[32] = this.context.params_20[2].get();
    return FlowControl.CONTINUE;
  }

  @Method(0x800172c0L)
  public FlowControl scriptConsumeChild() {
    this.context.commandOffset_0c = this.context.scriptState_04.consumeChild();
    return FlowControl.CONTINUE;
  }

  @Method(0x800172f4L)
  public FlowControl FUN_800172f4() {
    return FlowControl.CONTINUE;
  }

  @Method(0x800172fcL)
  public FlowControl FUN_800172fc() {
    return FlowControl.CONTINUE;
  }

  @Method(0x80017304L)
  public FlowControl FUN_80017304() {
    LOGGER.error("DEBUG: %s", this.context.params_20[0]);
    return FlowControl.CONTINUE;
  }

  @Method(0x8001730cL)
  public FlowControl scriptGetCallStackDepth() {
    this.context.params_20[0].set(this.callStackDepth());
    return FlowControl.CONTINUE;
  }

  public void dump() {
    LOGGER.error("%s crashed!", this);
    LOGGER.error("File %s %s @ 0x%x", this.frame().file.name, this.context.opIndex_10, this.context.opOffset_08 * 4);
    LOGGER.error("Parameters:");
    LOGGER.error("  Op param: 0x%x", this.context.opParam_18);
    for(int i = 0; i < this.context.paramCount_14; i++) {
      LOGGER.error("  %d: %s", i, this.context.params_20[i]);
    }

    LOGGER.error("Storage:");
    for(int i = 0; i < this.storage_44.length; i++) {
      LOGGER.error("  %d: 0x%x", i, this.storage_44[i]);
    }

    LOGGER.error("Registry IDs:");
    for(int i = 0; i < this.registryIds.length; i++) {
      if(this.registryIds[i] != null) {
        LOGGER.error("  %d: %s", i, this.registryIds[i]);
      }
    }

    LOGGER.error("Call stack:");
    for(int i = 0; i < this.callStack.size(); i++) {
      LOGGER.error("  %d: %s 0x%x", i, this.callStack.get(i).file.name, this.callStack.get(i).offset * 4);
    }

    LOGGER.error("Disassembly:");
    try {
      this.dumpDisassembly();
    } catch(final Throwable t) {
      LOGGER.warn("Failed to disassemble script");
    }
  }

  private void dumpDisassembly() {
    final Disassembler disassembler = new Disassembler(SCRIPTS.meta());
    final Script tokens = disassembler.disassemble(this.frame().file.data);

    final Translator translator = new Translator();
    translator.lineNumbers = true;
    final String decompiled = translator.translate(tokens, SCRIPTS.meta());

    final String[] split = decompiled.split("\n");
    for(int i = 0; i < split.length; i++) {
      if(split[i].startsWith(Integer.toHexString(this.context.opOffset_08 * 4))) {
        for(int n = Math.max(0, i - 5); n < i; n++) {
          LOGGER.error("  %s", split[n]);
        }

        LOGGER.error("  %s", split[i]);
        LOGGER.error("  %s", "~".repeat(split[i].length()));

        for(int n = i + 1; n < Math.min(split.length - 1, i + 6); n++) {
          LOGGER.error("  %s", split[n]);
        }

        break;
      }
    }
  }

  public void renderDebugInfo() {
    if(this.innerStruct_00 != null) {
      this.innerStruct_00.renderScriptDebug((ScriptState<ScriptedObject>)this);
    }
  }

  @Override
  public String toString() {
    return this.getClass().getSimpleName() + '[' + this.index + "] " + this.name;
  }
}
