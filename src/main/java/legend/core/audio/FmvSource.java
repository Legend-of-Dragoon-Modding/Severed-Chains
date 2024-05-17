package legend.core.audio;

public class FmvSource extends AudioSource {
  private final int format;
  private final int sampleRate;

  public FmvSource(final int format, final int sampleRate) {
    super(8);
    this.format = format;
    this.sampleRate = sampleRate;
  }

  public void bufferOutput(final short[] buffer) {
    this.setPlaying(true);
    super.bufferOutput(this.format, buffer, this.sampleRate);
  }
}
