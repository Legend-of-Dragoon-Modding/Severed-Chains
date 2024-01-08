package legend.core.audio.sequencer.assets.sequence.bgm;

import legend.core.audio.sequencer.assets.Channel;
import legend.core.audio.sequencer.assets.sequence.Command;

public final class KeyOff implements Command, Key {
  private final Channel channel;
  private final int note;
  private final int deltaTime;

  KeyOff(final Channel channel, final int note, final int deltaTime) {
    this.channel = channel;
    this.note = note;
    this.deltaTime = deltaTime;
  }

  public Channel getChannel() {
    return this.channel;
  }

  public int getNote() {
    return this.note;
  }

  @Override
  public int getDeltaTime() {
    return this.deltaTime;
  }
}
