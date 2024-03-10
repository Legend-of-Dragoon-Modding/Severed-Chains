package legend.core.audio.sequencer.assets.sequence.bgm;

import legend.core.audio.sequencer.assets.Channel;
import legend.core.audio.sequencer.assets.sequence.Command;

public final class ModulationChange implements Command {
  private final Channel channel;
  private final int modulation;
  private final int deltaTime;

  ModulationChange(final Channel channel, final int modulation, final int deltaTime) {
    this.channel = channel;
    this.modulation = modulation;
    this.deltaTime = deltaTime;
  }

  public Channel getChannel() {
    return this.channel;
  }

  public int getModulation() {
    return this.modulation;
  }

  @Override
  public int getDeltaTime() {
    return this.deltaTime;
  }
}
