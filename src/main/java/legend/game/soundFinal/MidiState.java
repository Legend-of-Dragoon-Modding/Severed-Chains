package legend.game.soundFinal;

import legend.game.unpacker.FileData;

final class MidiState {
  public final FileData sequence;
  public int offset;

  public int tempo;
  public int ticksPerQuarterNote;
  public float msPerTick;
  public double ticksPerMs;
  public float deltaMs;
  public boolean endOfTrack;

  public int event;
  public int previousEvent;

  /** The high nibble of event */
  public MidiCommand command;
  /** The low nibble of event */
  public int channel;

  MidiState(final FileData sequence) {
    this.sequence = sequence;
  }
}

