package legend.core.audio.assets;

import legend.core.audio.Sequence;

public final class SequencedAudio {
  private final Sssq sssq;
  private final byte[][] breathControls;
  private final byte[] velocityRamp;

  private int lsbType;
  private int nrpn;
  private int dataInstrumentIndex;
  public int repeatCount;
  public int repeatCounter;
  public Sequence.Command repeatCommand;
  public boolean repeat;

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

  public int getVelocity(final int index) {
    return this.velocityRamp[index];
  }

  public byte[][] getBreathControls() {
    return this.breathControls;
  }

  public int getLsbType() {
    return this.lsbType;
  }

  public void setLsbType(final int value) {
    this.lsbType = value;
  }

  public int getNrpn() {
    return this.nrpn;
  }

  public void setNrpn(final int value) {
    this.nrpn = value;
  }

  public int getDataInstrumentIndex() {
    return this.dataInstrumentIndex;
  }

  public void setDataInstrumentIndex(final int value) {
    this.dataInstrumentIndex = value;
  }
}
