package legend.core.audio.assets;

import legend.game.unpacker.FileData;
import legend.game.unpacker.UnpackerException;

public final class Sssq {
  private final Channel[] channels;
  private final byte[][] sequences;
  private final int ticksPerQuarterNote;
  private int tempo;
  private double ticksPerMs;

  /** Background Music */
  Sssq(final FileData data, final SoundFont soundFont) {
    if(data.readInt(12) != 0x71735353) { //SSsq
      throw new UnpackerException("Not a SSsq file!");
    }

    this.ticksPerQuarterNote = data.readUShort(2);
    final int tempo = data.readUShort(4);
    this.setTempo(tempo);

    this.channels = new Channel[16];
    for(int channel = 0; channel < this.channels.length; channel++) {
      this.channels[channel] = new Channel(data.slice(16 + channel * 16, 16), soundFont);
    }


    this.sequences = new byte[1][];
    this.sequences[0] = data.slice(0x110, data.size() - 0x110).getBytes();
  }

  void setTempo(final int tempo) {
    this.tempo = tempo;
    this.ticksPerMs = (this.tempo * this.ticksPerQuarterNote) / 60_000d;
  }

  Channel getChannel(final int channelIndex) {
    return this.channels[channelIndex];
  }

  byte[] getSequence(final int index) {
    return this.sequences[index];
  }

  double getTicksPerMs() {
    return this.ticksPerMs;
  }
}
