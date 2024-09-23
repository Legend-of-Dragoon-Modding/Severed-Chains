package legend.game.scripting;

public class ScriptStackFrame {
  public final ScriptFile file;
  public int offset;

  public ScriptStackFrame(final ScriptFile file, final int offset) {
    this.file = file;
    this.offset = offset;
  }

  public ScriptStackFrame copy() {
    return new ScriptStackFrame(this.file, this.offset);
  }
}
