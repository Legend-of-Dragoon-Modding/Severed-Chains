package legend.core.audio.sequencer.assets.sequence.bgm;

import legend.core.audio.sequencer.assets.Channel;
import legend.core.audio.sequencer.assets.sequence.Command;

public final class ProgramChange implements Command {
  private final Channel channel;
  private final int instrumentIndex;
  private final int deltaTime;

  ProgramChange(final Channel channel, final int programIndex, final int deltaTime) {
    this.channel = channel;
    this.instrumentIndex = programIndex;
    this.deltaTime = deltaTime;
  }

  public Channel getChannel() {
    return this.channel;
  }

  public int getInstrumentIndex() {
    return this.instrumentIndex;
  }

  @Override
  public int getDeltaTime() {
    return this.deltaTime;
  }
}
