package legend.game.scripting;

import legend.game.types.LodString;

public class StringParam extends Param {
  private final LodString value;

  public StringParam(final LodString value) {
    this.value = value;
  }

  @Override
  public int get() {
    return this.value.charAt(0);
  }

  @Override
  public Param set(final int val) {
    throw new IllegalStateException("Can't set value of IntParam");
  }

  @Override
  public Param array(final int index) {
    return new StringParam(this.value.slice(1));
  }

  @Override
  public String toString() {
    return "String %s".formatted(this.value);
  }
}
