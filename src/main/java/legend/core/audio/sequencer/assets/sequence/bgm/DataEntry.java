package legend.core.audio.sequencer.assets.sequence.bgm;

import legend.core.audio.sequencer.assets.sequence.Command;

public final class DataEntry implements Command {
  private final int value;
  private final int deltaTime;

  DataEntry(final int value, final int deltaTime) {
    this.value = value;
    this.deltaTime = deltaTime;
  }

  public int getValue() {
    return this.value;
  }

  @Override
  public int getDeltaTime() {
    return this.deltaTime;
  }
}
