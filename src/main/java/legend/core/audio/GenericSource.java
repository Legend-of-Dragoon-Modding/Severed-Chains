package legend.core.audio;

import java.nio.ByteBuffer;

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
    this.setActive(true);
    super.init();
  }

  public void bufferOutput(final ByteBuffer buffer) {
    this.setActive(true);
    super.bufferOutput(this.format, buffer, this.sampleRate);
  }

  public void bufferOutput(final short[] buffer) {
    this.setActive(true);
    super.bufferOutput(this.format, buffer, this.sampleRate);
  }
}
