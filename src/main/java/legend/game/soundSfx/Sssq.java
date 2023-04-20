package legend.game.soundSfx;

import legend.game.unpacker.FileData;
import legend.game.unpacker.UnpackerException;

final class Sssq {
  private final Channel[] channels;
  private final Sequence[][] sequences;
  private final int ticksPerQuarterNote;
  private int tempo;
  private double ticksPerMs;

  /** Background Music */
  Sssq(final FileData sssqData, final SoundFont soundFont) {
    this.ticksPerQuarterNote = sssqData.readUShort(2);
    final int tempo = sssqData.readUShort(4);
    this.setTempo(tempo);

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
    this.ticksPerQuarterNote = sssqData.readUShort(2); // This is 0??
    this.setTempo(sssqData.readUShort(4)); // Also 0??

    this.channels = new Channel[0x10];

    for(int channel = 0; channel < this.channels.length; channel++) {
      this.channels[channel] = new Channel(sssqData.slice(16 + channel * 16, 16), soundFont);
    }

    this.sequences = sequences;
  }

  public void setTempo(final int tempo) {
    this.tempo = tempo;
    this.ticksPerMs = (this.tempo * this.ticksPerQuarterNote) / 60_000d;
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
