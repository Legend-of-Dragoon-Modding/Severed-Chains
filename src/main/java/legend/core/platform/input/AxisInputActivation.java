package legend.core.platform.input;

public class AxisInputActivation extends InputActivation {
  public final InputAxis axis;
  public final InputAxisDirection direction;
  /** Inner deadzone */
  public final float threshold;
  /** Outer deadzone */
  public final float deadzone;

  public AxisInputActivation(final InputAxis axis, final InputAxisDirection direction, final float threshold, final float deadzone) {
    this.axis = axis;
    this.direction = direction;
    this.threshold = threshold;
    this.deadzone = deadzone;
  }
}
