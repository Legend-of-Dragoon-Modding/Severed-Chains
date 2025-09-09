package legend.game.scripting;

public class ScriptTempParam extends Param {
  public static final ScriptTempParam ZERO = new ScriptTempParam(0);

  private final int value;

  public ScriptTempParam(final int value) {
    this.value = value;
  }

  @Override
  public int get() {
    return this.value;
  }

  @Override
  public Param set(final int val) {
    throw new IllegalStateException("Can't set " + this.getClass().getSimpleName());
  }

  @Override
  public Param array(final int index) {
    throw new IllegalStateException("Can't use " + this.getClass().getSimpleName() + " as array");
  }
}
