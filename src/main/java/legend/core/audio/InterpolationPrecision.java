package legend.core.audio;

public enum InterpolationPrecision {
  Quarter(6),
  Half(7),
  Retail(8),
  Double(9),
  Quadruple(10);

  public final int value;

  InterpolationPrecision(final int value) {
    this.value = value;
  }
}
