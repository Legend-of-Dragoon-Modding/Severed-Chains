package legend.game.types;

import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;

public class RunningScript {
  public int scriptStateIndex_00;
  public ScriptState<? extends MemoryRef> scriptState_04;
  /** Pointer to the start of the current command (i.e. the op) */
  public IntRef opPtr_08;
  /** Pointer to the current element in the packet (may be op or param) */
  public IntRef commandPtr_0c;
  public int opIndex_10;
  public int paramCount_14;
  public int opParam_18;
  public int ui_1c;
  public IntRef[] params_20 = new IntRef[10];
}
