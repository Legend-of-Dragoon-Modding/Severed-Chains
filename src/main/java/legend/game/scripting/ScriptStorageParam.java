package legend.game.scripting;

public class ScriptStorageParam extends Param {
  private final ScriptState<?> state;
  private final int index;

  public ScriptStorageParam(final ScriptState<?> state, final int index) {
    this.state = state;
    this.index = index;
  }

  @Override
  public int get() {
    return this.state.storage_44[this.index];
  }

  @Override
  public Param set(final int val) {
    this.state.storage_44[this.index] = val;
    return this;
  }

  @Override
  public Param array(final int index) {
    return new ScriptStorageParam(this.state, this.index + index);
  }

  @Override
  public String toString() {
    return "script[%d].storage[%d] %d".formatted(this.state.index, this.index, this.get());
  }
}
