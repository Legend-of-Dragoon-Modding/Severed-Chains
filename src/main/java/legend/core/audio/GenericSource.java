package legend.core.audio;

public class GenericSource extends AudioSource {
  private final int format;
  private final int sampleRate;

  public GenericSource(final int format, final int sampleRate) {
    super(16);
    this.format = format;
    this.sampleRate = sampleRate;
  }

  @Override
  protected void init() {
    this.setPlaying(true);
    super.init();
  }

  public void bufferOutput(final short[] buffer) {
    this.setPlaying(true);
    super.bufferOutput(this.format, buffer, this.sampleRate);
  }
}
