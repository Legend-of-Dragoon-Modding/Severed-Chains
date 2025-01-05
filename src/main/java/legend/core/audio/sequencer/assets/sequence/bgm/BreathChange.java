package legend.core.audio.sequencer.assets.sequence.bgm;

import legend.core.audio.sequencer.assets.Channel;
import legend.core.audio.sequencer.assets.sequence.Command;

public final class BreathChange implements Command {

  private final Channel channel;
  private final int breath;
  private final int deltaTime;

  BreathChange(final Channel channel, final int breath, final int deltaTime) {
    this.channel = channel;
    this.breath = breath;
    this.deltaTime = deltaTime;
  }

  public Channel getChannel() {
    return this.channel;
  }

  public int getBreath() {
    return this.breath;
  }

  @Override
  public int getDeltaTime() {
    return this.deltaTime;
  }
}
