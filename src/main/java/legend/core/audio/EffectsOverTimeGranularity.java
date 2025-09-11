package legend.core.audio;

import static legend.core.audio.Constants.ENGINE_SAMPLE_RATE;

public enum EffectsOverTimeGranularity {
  Retail(0),
  Double(1),
  Quadruple(2),
  Octuple(3),
  Sexdecuple(4),
  Duotrigintuple(5);

  public final int scale;
  public final int shift;
  public final int samples;

  EffectsOverTimeGranularity(final int shift) {
    this.scale = 1 << shift;
    this.shift = shift;
    this.samples = ENGINE_SAMPLE_RATE / (60 * this.scale);
  }
}
