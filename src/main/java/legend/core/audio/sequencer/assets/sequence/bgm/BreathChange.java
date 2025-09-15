package legend.core.audio.sequencer.assets.sequence.bgm;

import legend.core.audio.sequencer.assets.Breath;
import legend.core.audio.sequencer.assets.Channel;
import legend.core.audio.sequencer.assets.sequence.Command;

public final class BreathChange implements Command {

  private final Channel channel;
  private final long breath;
  private final int deltaTime;

  BreathChange(final Channel channel, final int breath, final int deltaTime) {
    this.channel = channel;
    this.breath = Breath.convert(breath);
    this.deltaTime = deltaTime;
  }

  public Channel getChannel() {
    return this.channel;
  }

  public void apply() {
    this.channel.setBreath(this.breath);
  }

  public long getBreath() {
    return this.breath;
  }

  @Override
  public int getDeltaTime() {
    return this.deltaTime;
  }
}
