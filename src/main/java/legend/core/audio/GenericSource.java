package legend.core.audio;

import java.nio.ByteBuffer;

public class GenericSource extends AudioSource {
  private final int format;
  private final int sampleRate;

  public GenericSource(final int format, final int sampleRate) {
    this.format = format;
    this.sampleRate = sampleRate;
    super(format, sampleRate, 1, AudioTag.Generic);
  }

  @Override
  protected void fillBuffer() {

  }

  public void bufferOutput(final ByteBuffer buffer) {
    this.setActive(true);
    //super.bufferOutput(buffer);
  }
}
