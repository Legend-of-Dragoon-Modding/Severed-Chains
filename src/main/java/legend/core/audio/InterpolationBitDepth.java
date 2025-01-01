package legend.core.audio;

public enum InterpolationBitDepth {
  Quarter(6),
  Half(7),
  Retail(8),
  Double(9),
  Quadruple(10);

  public final int value;

  InterpolationBitDepth(final int value) {
    this.value = value;
  }
}
