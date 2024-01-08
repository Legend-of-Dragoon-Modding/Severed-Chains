package legend.core.audio.sequencer.assets.sequence.bgm;

import legend.core.audio.sequencer.assets.Channel;
import legend.core.audio.sequencer.assets.sequence.Command;

public final class KeyOn implements Command, Key {
  private final Channel channel;
  private final int note;
  private final int velocity;
  private final int deltaTime;

  KeyOn(final Channel channel, final int note, final int velocity, final int deltaTime) {
    this.channel = channel;
    this.note = note;
    this.velocity = velocity;
    this.deltaTime = deltaTime;
  }

  public Channel getChannel() {
    return this.channel;
  }

  public int getNote() {
    return this.note;
  }

  public int getVelocity() {
    return this.velocity;
  }

  @Override
  public int getDeltaTime() {
    return this.deltaTime;
  }
}
