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
    return this.state.getStor(this.index);
  }

  @Override
  public Param set(final int val) {
    this.state.setStor(this.index, val);
    return this;
  }

  @Override
  public Param array(final int index) {
    return new ScriptStorageParam(this.state, this.index + index);
  }

  @Override
  public float getFloat() {
    return this.state.getStorFloat(this.index);
  }

  @Override
  public Param set(final float val) {
    this.state.setStor(this.index, val);
    return this;
  }

  @Override
  public boolean isFloat() {
    return this.state.isStorFloat(this.index);
  }

  @Override
  public String toString() {
    return "script[%d].stor[%d] 0x%x".formatted(this.state.index, this.index, this.get());
  }
}
