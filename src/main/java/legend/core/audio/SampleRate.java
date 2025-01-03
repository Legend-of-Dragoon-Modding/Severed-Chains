package legend.core.audio;

public enum SampleRate {
  _44100(44100, "44.1 kHz"),
  _48000(48000, "48 kHz");

  public final int value;
  public final String description;

  SampleRate(final int sampleRate, final String description) {
    this.value = sampleRate;
    this.description = description;
  }
}
