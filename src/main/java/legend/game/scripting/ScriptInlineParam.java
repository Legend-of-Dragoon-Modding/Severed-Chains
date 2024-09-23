package legend.game.scripting;

public class ScriptInlineParam extends Param {
  private final ScriptState<?> state;
  private final int offset;

  public ScriptInlineParam(final ScriptState<?> state, final int offset) {
    this.state = state;
    this.offset = offset;
  }

  @Override
  public void jump(final RunningScript<?> script) {
    script.scriptState_04.replaceFrame(this.state.frame().copy()).offset = this.offset;
    script.commandOffset_0c = this.offset;
  }

  @Override
  public void jump(final ScriptState<?> state) {
    state.replaceFrame(this.state.frame().copy()).offset = this.offset;
  }

  @Override
  public int get() {
    return this.state.frame().file.getOp(this.offset);
  }

  @Override
  public Param set(final int val) {
    // Apparently this is possible
    this.state.frame().file.setOp(this.offset, val);
    return this;
  }

  @Override
  public Param array(final int index) {
    return new ScriptInlineParam(this.state, this.offset + index);
  }

  @Override
  public String toString() {
    return "script[%d].inl[0x%x] 0x%x".formatted(this.state.index, this.offset * 4, this.get());
  }
}
