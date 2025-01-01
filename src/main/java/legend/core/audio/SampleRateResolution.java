package legend.core.audio;

public enum SampleRateResolution {
  Quarter(4, 5),
  Half(8, 4),
  Retail(16, 3),
  Double(32, 2),
  Quadruple(64, 1),
  Octuple(128, 0);
  
  public final int value;
  public final int sampleRateShift;
  
  SampleRateResolution(final int value, final int shifts) {
    this.value = value;
    this.sampleRateShift = shifts;
  }
}
