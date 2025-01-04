package legend.core.audio;

public enum PitchResolution {
  Quarter(4),
  Half(8),
  Retail(16),
  Double(32),
  Quadruple(64),
  Octuple(128);
  
  public final int value;
  public final int sampleRateShift;
  
  PitchResolution(final int value) {
    this.value = value;
    this.sampleRateShift = 7 - Integer.numberOfTrailingZeros(value);
  }
}
