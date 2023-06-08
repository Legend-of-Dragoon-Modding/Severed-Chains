package legend.core.audio.assets;

import legend.game.unpacker.FileData;
import legend.game.unpacker.UnpackerException;

public final class Sssq {
  //TODO channels
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

    //TODO channels

    this.sequences = new byte[1][];
    this.sequences[0] = data.slice(0x110, data.size() - 0x110).getBytes();
  }

  public void setTempo(final int tempo) {
    this.tempo = tempo;
    this.ticksPerMs = (this.tempo * this.ticksPerQuarterNote) / 60_000d;
  }
}
