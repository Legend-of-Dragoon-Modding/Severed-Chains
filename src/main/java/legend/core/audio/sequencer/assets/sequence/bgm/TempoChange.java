package legend.core.audio.sequencer.assets.sequence.bgm;

import legend.core.audio.sequencer.assets.sequence.Command;

public final class TempoChange implements Command {
  private final int tempo;
  private final int deltaTime;

  TempoChange(final int tempo, final int deltaTime) {
    this.tempo = tempo;
    this.deltaTime = deltaTime;
  }

  public int getTempo() {
    return this.tempo;
  }

  @Override
  public int getDeltaTime() {
    return this.deltaTime;
  }
}
