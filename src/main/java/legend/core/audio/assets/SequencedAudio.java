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
}
