package legend.core.audio.sequencer.assets.sequence.sfx;

import legend.core.audio.sequencer.assets.Channel;
import legend.core.audio.sequencer.assets.sequence.Command;

public final class PortamentoChange implements Command {
  private final Channel channel;
  private final int ticks;
  private final int portamento;
  private final int note;
  private final int deltaTime;

  PortamentoChange(final int ticks, final int portamento, final Channel channel, final int note, final int deltaTime) {
    this.channel = channel;
    this.ticks = ticks;
    this.portamento = portamento;
    this.note = note;
    this.deltaTime = deltaTime;
  }

  public Channel getChannel() {
    return this.channel;
  }

  public int getTicks() {
    return this.ticks;
  }

  public int getPortamento() {
    return this.portamento;
  }

  public int getNote() {
    return this.note;
  }

  @Override
  public int getDeltaTime() {
    return this.deltaTime;
  }
}
