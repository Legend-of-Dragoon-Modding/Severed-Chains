package legend.game.types;

import legend.game.scripting.Param;

public class RunningScript {
  public int scriptStateIndex_00;
  public ScriptState<?> scriptState_04;
  /** Pointer to the start of the current command (i.e. the op) */
  public int opOffset_08;
  /** Pointer to the current element in the packet (may be op or param) */
  public int commandOffset_0c;
  public int opIndex_10;
  public int paramCount_14;
  public int opParam_18;
  public int ui_1c;
  public Param[] params_20 = new Param[10];

  public int getOp() {
    return this.scriptState_04.scriptPtr_14.getOp(this.commandOffset_0c);
  }
}
