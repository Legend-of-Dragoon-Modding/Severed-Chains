package legend.game.soundSfx;

import legend.game.unpacker.FileData;
import legend.game.unpacker.UnpackerException;

final class Sssq {
  private final double ticksPerMs;
  private final int tempo;
  private final Channel[] channels;
  private final Sequence[][] sequences;

  /** Background Music */
  Sssq(final FileData sssqData, final SoundFont soundFont) {
    final int ticksPerQuarterNote = sssqData.readUShort(2);
    this.tempo = sssqData.readUShort(4);
    this.ticksPerMs = (this.tempo * ticksPerQuarterNote) / 60_000d;

    if(sssqData.readInt(12) != 0x71735353) {
      throw new UnpackerException("Not a SSsq file!");
    }

    this.channels = new Channel[0x10];

    for(int channel = 0; channel < this.channels.length; channel++) {
      this.channels[channel] = new Channel(sssqData.slice(16 + channel * 16, 16), soundFont);
    }

    this.sequences = new Sequence[1][];
    this.sequences[0] = new Sequence[] { new Sequence(sssqData.slice(0x110, sssqData.size() - 0x110))};
  }

  /** Sound Effects */
  Sssq(final FileData sssqData, final Sequence[][] sequences, final SoundFont soundFont) {
    final int ticksPerQuarterNote = sssqData.readUShort(2); // This is 0??
    this.tempo = sssqData.readUShort(4); // Also 0??
    this.ticksPerMs = (this.tempo * ticksPerQuarterNote) / 60_000d;

    this.channels = new Channel[0x10];

    for(int channel = 0; channel < this.channels.length; channel++) {
      this.channels[channel] = new Channel(sssqData.slice(16 + channel * 16, 16), soundFont);
    }

    this.sequences = sequences;
  }

  int deltaTimeToSampleCount(final int deltaTime) {
    if(deltaTime == 0) {
      return 0;
    }

    final double deltaMs = deltaTime / this.ticksPerMs;
    return (int)(deltaMs * 44.1d);
  }

  Sequence.Command getNextCommand() {
    // TODO support multi-sequence SFX
    return this.sequences[0][0].getNextCommand();
  }

  Channel getChannel(final int channelIndex) {
    return this.channels[channelIndex];
  }
}
