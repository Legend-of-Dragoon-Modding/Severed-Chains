package legend.core.audio;

import static legend.core.audio.Constants.BREATH_BIT_SHIFT;
import static legend.core.audio.Constants.PITCH_BIT_SHIFT;

public enum InterpolationPrecision {
  Quarter(6),
  Half(7),
  Retail(8),
  Double(9),
  Quadruple(10);

  public final int value;
  public final int sampleShift;
  public final int breathShift;
  public final int interpolationAnd;

  InterpolationPrecision(final int value) {
    this.value = value;
    this.sampleShift = PITCH_BIT_SHIFT - value;
    this.breathShift = BREATH_BIT_SHIFT - value;
    this.interpolationAnd = (1 << value) - 1;

  }
}
