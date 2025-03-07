package legend.core.platform.input;

public class AxisInputActivation extends InputActivation {
  public final InputAxis axis;
  public final InputAxisDirection direction;

  public AxisInputActivation(final InputAxis axis, final InputAxisDirection direction) {
    this.axis = axis;
    this.direction = direction;
  }
}
