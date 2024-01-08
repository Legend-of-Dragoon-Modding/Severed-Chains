package legend.core.audio.sequencer.assets.sequence.bgm;

import legend.core.audio.sequencer.assets.sequence.Command;

public final class EndOfTrack implements Command {
  private final int deltaTime;

  EndOfTrack(final int deltaTime) {
    this.deltaTime = deltaTime;
  }

  @Override
  public int getDeltaTime() {
    return this.deltaTime;
  }
}
