package legend.core.audio.assets;

public final class SequencedAudio {
  private final Sssq sssq;
  private final byte[][] breathControls;
  private final byte[] velocityRamp;

  SequencedAudio(final Sssq sssq, final byte[][] breathControls, final byte[] velocityRamp) {
    this.sssq = sssq;
    this.breathControls = breathControls;
    this.velocityRamp = velocityRamp;
  }

  public Channel getChannel(final int channelIndex) {
    return this.sssq.getChannel(channelIndex);
  }

  public byte[] getSequence(final int index) {
    return this.sssq.getSequence(index);
  }

  public double getTicksPerMs() {
    return this.sssq.getTicksPerMs();
  }

  public void setTempo(final int value) {
    this.sssq.setTempo(value);
  }
}
