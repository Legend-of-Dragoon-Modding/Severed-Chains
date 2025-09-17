package legend.core.audio.sequencer.assets.sequence.bgm;

import legend.core.audio.sequencer.assets.Channel;
import legend.core.audio.sequencer.assets.sequence.Command;

public final class PanChange implements Command {
  private final Channel channel;
  private final int pan;
  private final int deltaTime;

  PanChange(final Channel channel, final int pan, final int deltaTime) {
    this.channel = channel;
    this.pan = pan;
    this.deltaTime = deltaTime;
  }

  public Channel getChannel() {
    return this.channel;
  }

  public void apply(final boolean isStereo) {
    if(isStereo) {
      this.channel.setPan(this.pan);

      return;
    }

    this.channel.setPan(0x40);
  }

  public int getPan() {
    return this.pan;
  }

  @Override
  public int getDeltaTime() {
    return this.deltaTime;
  }
}
