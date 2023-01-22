package legend.game.scripting;

public class IntParam extends Param {
  private final int value;

  public IntParam(final int value) {
    this.value = value;
  }

  @Override
  public int get() {
    return this.value;
  }

  @Override
  public Param set(final int val) {
    throw new IllegalStateException("Can't set value of IntParam");
  }

  @Override
  public Param array(final int index) {
    throw new IllegalStateException("Can't use IntParam as array");
  }

  @Override
  public String toString() {
    return "Int %d".formatted(this.value);
  }
}
