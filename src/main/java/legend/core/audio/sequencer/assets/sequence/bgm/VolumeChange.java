package legend.core.audio.sequencer.assets.sequence.bgm;

import legend.core.audio.sequencer.assets.Channel;
import legend.core.audio.sequencer.assets.sequence.Command;

public final class VolumeChange implements Command {
  private final Channel channel;
  private final float volume;
  private final int deltaTime;

  VolumeChange(final Channel channel, final int volume, final int deltaTime) {
    this.channel = channel;
    this.volume = volume / 128.0f;
    this.deltaTime = deltaTime;
  }

  public Channel getChannel() {
    return this.channel;
  }

  public float getVolume() {
    return this.volume;
  }

  @Override
  public int getDeltaTime() {
    return this.deltaTime;
  }
}
