package legend.core.audio.sequencer.assets.sequence.sfx;

import legend.core.audio.sequencer.assets.Channel;
import legend.core.audio.sequencer.assets.sequence.Command;

public final class ModulationChange implements Command {
  private final Channel channel;
  private final int modulation;
  private final int note;
  private final int deltaTime;

  ModulationChange(final int modulation, final Channel channel, final int note, final int deltaTime) {
    this.channel = channel;
    this.modulation = modulation;
    this.note = note;
    this.deltaTime = deltaTime;
  }

  public Channel getChannel() {
    return this.channel;
  }

  public int getModulation() {
    return this.modulation;
  }

  public int getNote() {
    return this.note;
  }

  @Override
  public int getDeltaTime() {
    return this.deltaTime;
  }
}
