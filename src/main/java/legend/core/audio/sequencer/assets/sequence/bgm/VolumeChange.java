package legend.core.audio.sequencer.assets.sequence.bgm;

import legend.core.audio.sequencer.assets.Channel;
import legend.core.audio.sequencer.assets.sequence.Command;

public final class VolumeChange implements Command {
  private final Channel channel;
  private final int volume;
  private final int deltaTime;

  VolumeChange(final Channel channel, final int volume, final int deltaTime) {
    this.channel = channel;
    this.volume = volume;
    this.deltaTime = deltaTime;
  }

  public Channel getChannel() {
    return this.channel;
  }

  public int getVolume() {
    return this.volume;
  }

  @Override
  public int getDeltaTime() {
    return this.deltaTime;
  }
}
