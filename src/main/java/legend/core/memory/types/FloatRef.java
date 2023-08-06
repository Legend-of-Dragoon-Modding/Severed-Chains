package legend.core.memory.types;

public class FloatRef {
  private float val;

  public float get() {
    return this.val;
  }

  public FloatRef set(final float val) {
    this.val = val;
    return this;
  }

  @Override
  public String toString() {
    return Float.toString(this.get());
  }
}
