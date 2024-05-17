package legend.core.audio;

public class GenericSource extends AudioSource {
  private final int format;
  private final int sampleRate;

  public GenericSource(final int format, final int sampleRate) {
    super(8);
    this.format = format;
    this.sampleRate = sampleRate;
  }

  public void bufferOutput(final short[] buffer) {
    this.setPlaying(true);
    super.bufferOutput(this.format, buffer, this.sampleRate);
  }
}
