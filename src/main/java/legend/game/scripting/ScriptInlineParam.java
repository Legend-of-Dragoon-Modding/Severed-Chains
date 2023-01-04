package legend.game.scripting;

import legend.game.types.RunningScript;
import legend.game.types.ScriptState;

public class ScriptInlineParam extends Param {
  private final ScriptState<?> state;
  private final int offset;

  public ScriptInlineParam(final ScriptState<?> state, final int offset) {
    this.state = state;
    this.offset = offset;
  }

  @Override
  public void jump(final RunningScript script) {
    script.commandOffset_0c = this.offset;
  }

  @Override
  public void jump(final ScriptState<?> script) {
    script.offset_18 = this.offset;
  }

  @Override
  public int get() {
    return this.state.scriptPtr_14.getOp(this.offset);
  }

  @Override
  public Param set(final int val) {
    // Apparently this is possible
    this.state.scriptPtr_14.setOp(this.offset, val);
    return this;
  }

  @Override
  public Param array(final int index) {
    return new ScriptInlineParam(this.state, this.offset + index * 4);
  }

  @Override
  public String toString() {
    return "script[0x%x] 0x%x".formatted(this.offset, this.get());
  }
}
