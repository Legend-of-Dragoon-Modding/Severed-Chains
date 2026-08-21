package legend.core.audio.sequencer.assets.sequence.sfx;

import legend.core.audio.sequencer.assets.Channel;
import legend.core.audio.sequencer.assets.sequence.Command;

public final class VolumeChange implements Command {
  private final Channel channel;
  private final int ticks;
  private final float volume;
  private final int note;
  private final int deltaTime;

  VolumeChange(final int ticks, final int volume, final Channel channel, final int note, final int deltaTime) {
    this.channel = channel;
    this.ticks = ticks;
    this.volume = volume / 128.0f;
    this.note = note;
    this.deltaTime = deltaTime;
  }

  public Channel getChannel() {
    return this.channel;
  }

  public int getTicks() {
    return this.ticks;
  }

  public float getVolume() {
    return this.volume;
  }

  public int getNote() {
    return this.note;
  }

  @Override
  public int getDeltaTime() {
    return this.deltaTime;
  }
}
