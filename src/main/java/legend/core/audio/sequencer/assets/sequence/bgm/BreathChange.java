package legend.core.audio.sequencer.assets.sequence.bgm;

import legend.core.audio.sequencer.assets.Channel;
import legend.core.audio.sequencer.assets.sequence.Command;

public final class BreathChange implements Command {
  public static final int BREATH_BASE_SHIFT = 22;
  /** Represents all 60 positions in a breath control table */
  public static final int BREATH_BASE_VALUE = 0xf0 << (BREATH_BASE_SHIFT - 2);

  private final Channel channel;
  private final int breath;
  private final int deltaTime;

  BreathChange(final Channel channel, final int breath, final int deltaTime) {
    this.channel = channel;
    this.breath = Math.round(BREATH_BASE_VALUE / (60 - breath * 58 / 127.0f));
    this.deltaTime = deltaTime;
  }

  public Channel getChannel() {
    return this.channel;
  }

  public int getBreath() {
    return this.breath;
  }

  @Override
  public int getDeltaTime() {
    return this.deltaTime;
  }
}
