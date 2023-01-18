package legend.game.scripting;

public class RunningScript<T> {
  public final ScriptState<T> scriptState_04;
  /** Pointer to the start of the current command (i.e. the op) */
  public int opOffset_08;
  /** Pointer to the current element in the packet (may be op or param) */
  public int commandOffset_0c;
  public int opIndex_10;
  public int paramCount_14;
  public int opParam_18;

  public final Param[] params_20 = new Param[10];

  public RunningScript(final ScriptState<T> state) {
    this.scriptState_04 = state;
  }

  public int getOp() {
    return this.scriptState_04.scriptPtr_14.getOp(this.commandOffset_0c);
  }
}
