package legend.core.audio.sequencer.assets.sequence.bgm;

import legend.core.audio.sequencer.assets.Channel;
import legend.core.audio.sequencer.assets.sequence.Command;

public final class PitchBendChange implements Command {
  private final Channel channel;
  private final int pitchAmount;
  private final int deltaTime;

  PitchBendChange(final Channel channel, final int pitchAmount, final int deltaTime) {
    this.channel = channel;
    this.pitchAmount = pitchAmount;
    this.deltaTime = deltaTime;
  }

  public Channel getChannel() {
    return this.channel;
  }

  public int getPitchAmount() {
    return this.pitchAmount;
  }

  @Override
  public int getDeltaTime() {
    return this.deltaTime;
  }
}
