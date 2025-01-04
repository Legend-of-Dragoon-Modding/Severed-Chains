package legend.core.audio;

public enum SampleRate {
  _44100(44100),
  _48000(48000);

  public final int value;

  SampleRate(final int sampleRate) {
    this.value = sampleRate;
  }
}
