package legend.game.sound2;

import legend.game.unpacker.FileData;

public class MidiState {
  public final FileData sequence;
  public int offset;

  public int tempo;
  public int ticksPerQuarterNote;
  public float msPerTick;
  public float deltaMs;
  public boolean endOfTrack;

  public int event;
  public int previousEvent;

  /** The high nibble of event */
  public MidiCommand command;
  /** The low nibble of event */
  public int channel;

  public MidiState(final FileData sequence) {
    this.sequence = sequence;
  }
}
